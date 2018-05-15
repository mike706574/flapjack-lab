package fun.mike.flapjack.pipeline.lab;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface Pipeline<V> {
    Logger log = LoggerFactory.getLogger(Pipeline.class);

    static InputContextPipelineBuilder fromContext(InputContext context) {
        return new InputContextPipelineBuilder(context);
    }

    static FlatInputFilePipelineBuilder fromFile(String path, Format format) {
        return new FlatInputFilePipelineBuilder(path, format);
    }

    static OperationPipelineBuilder fromIterable(Iterable<Record> list) {
        return new OperationPipelineBuilder(new IterableInputContext(list), new LinkedList<>());
    }

    static OperationPipelineBuilder fromCollection(Collection<Record> collection) {
        return new OperationPipelineBuilder(new CollectionInputContext(collection), new LinkedList<>());
    }

    static OperationPipelineBuilder fromList(List<Record> list) {
        return fromCollection(list);
    }

    PipelineResult<V> execute();

    default <T> PipelineResult<T> runWithOutputChannel(InputContext inputContext,
                                                       Transform transform,
                                                       OutputContext<T> outputContext) {
        log.debug("Running pipeline.");

        log.debug("Input: " + PipelineExplainer.explainInput(inputContext));

        log.debug("Output: " + PipelineExplainer.explainOutput(outputContext));

        int inputCount = 0;
        int outputCount = 0;

        List<Failure> failures = new LinkedList<>();
        int inputFailureCount = 0;
        int transformFailureCount = 0;
        int outputFailureCount = 0;

        long start = System.nanoTime();

        log.debug("Opening channels.");
        try (InputChannel inputChannel = inputContext.buildChannel();
             OutputChannel<T> outputChannel = outputContext.buildChannel()) {

            log.debug("Reading records.");
            while (inputChannel.hasMore()) {
                inputCount++;
                InputResult inputValue = inputChannel.take();

                if (inputValue.isOk()) {
                    String inputLine = inputValue.getLine();
                    Record inputRecord = inputValue.getValue();

                    TransformResult transformResult = transform.run(inputRecord);

                    if (transformResult.isOk()) {
                        Record value = transformResult.getRecord();

                        Optional<Failure> outputFailure = outputChannel.put(inputCount, inputLine, value);

                        if (outputFailure.isPresent()) {
                            outputFailureCount++;
                            failures.add(outputFailure.get());
                        } else {
                            outputCount++;
                        }
                    } else if (transformResult.hasFailure()) {
                        transformFailureCount++;
                        failures.add(TransformFailure.fromResult(inputCount, inputLine, transformResult));
                    }
                } else {
                    inputFailureCount++;
                    failures.add(inputValue.getFailure());
                }
            }

            long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            long s = TimeUnit.MILLISECONDS.toSeconds(ms);

            log.debug(String.format("Processed %d records in %dms (%ds).",
                                    inputCount,
                                    ms,
                                    s));


            int failureCount = failures.size();

            T value = outputChannel.getValue();

            PipelineResult<T> result = PipelineResult.of(value, inputContext, outputContext, inputCount, outputCount, failures);

            log.debug("Input count: " + inputCount);
            log.debug("Output count: " + outputCount);

            if (result.isNotOk()) {
                log.debug("Input failures: " + inputFailureCount);
                log.debug("Transform failures: " + transformFailureCount);
                log.debug("Output failures: " + outputFailureCount);
            }

            if (result.isOk()) {
                log.debug("Pipeline completed successfully with no failures.");
            } else {
                String noun = failureCount == 1 ? "failure" : "failures";
                log.debug(String.format("Pipeline completed with %d %s.", failureCount, noun));
            }

            return result;
        }
    }
}

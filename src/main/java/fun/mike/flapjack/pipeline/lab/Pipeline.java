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

    PipelineResult<V> execute();

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

    default <T> PipelineResult<T> runWithOutputChannel(InputContext inputContext,
                                                       Transform transform,
                                                       OutputContext<T> outputContext) {
        log.debug("Running pipeline.");

        log.debug("Input: " + PipelineExplainer.explainInput(inputContext));

        log.debug("Output: " + PipelineExplainer.explainOutput(outputContext));

        int inputCount = 0;
        int outputCount = 0;

        List<PipelineError> errors = new LinkedList<>();
        int inputErrorCount = 0;
        int transformErrorCount = 0;
        int outputErrorCount = 0;

        long start = System.nanoTime();

        log.debug("Opening channels.");
        try (InputChannel inputChannel = inputContext.buildChannel();
             OutputChannel<T> outputChannel = outputContext.buildChannel()) {

            log.debug("Reading records.");
            while (inputChannel.hasMore()) {
                inputCount++;
                InputResult inputValue = inputChannel.take();

                if(inputValue.isOk()) {
                    String inputLine = inputValue.getLine();
                    Record inputRecord = inputValue.getValue();

                    TransformResult transformResult = transform.run(inputRecord);

                    if (transformResult.isOk()) {
                        Record value = transformResult.getRecord();

                        Optional<PipelineError> outputError = outputChannel.put(inputCount, inputLine, value);

                        if (outputError.isPresent()) {
                            outputErrorCount++;
                            errors.add(outputError.get());
                        }
                        else {
                            outputCount++;
                        }
                    }
                    else if(transformResult.hasError()) {
                        transformErrorCount++;
                        errors.add(TransformPipelineError.fromResult(inputCount, inputLine, transformResult));
                    }
                }
                else {
                    inputErrorCount++;
                    errors.add(inputValue.getError());
                }
            }

            long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            long s = TimeUnit.MILLISECONDS.toSeconds(ms);

            log.debug(String.format("Processed %d records in %dms (%ds).",
                                    inputCount,
                                    ms,
                                    s));


            int errorCount = errors.size();

            T value = outputChannel.getValue();

            PipelineResult<T> result = PipelineResult.of(value, inputContext, outputContext, inputCount, outputCount, errors);

            log.debug("Input count: " + inputCount);
            log.debug("Output count: " + outputCount);

            if (result.isNotOk()) {
                log.debug("Input errors: " + inputErrorCount);
                log.debug("Transform errors: " + transformErrorCount);
                log.debug("Output errors: " + outputErrorCount);
            }

            if (result.isOk()) {
                log.debug("Pipeline completed successfully with no errors.");
            } else {
                String noun = errorCount == 1 ? "error" : "errors";
                log.debug(String.format("Pipeline completed with %d %s.", errorCount, noun));
            }

            return result;
        }
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.io.alpha.IO;
import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface Pipeline<V> {
    Logger log = LoggerFactory.getLogger(Pipeline.class);

    static InputFilePipelineBuilder fromFile(String path, Format format) {
        return new InputFilePipelineBuilder(path, format);
    }

    PipelineResult<V> execute();

    default <T> PipelineResult<T> runWithOutputChannel(FlatInputFile inputFile,
                                                       Transform transform,
                                                       OutputContext<T> outputContext) {
        log.debug("Running pipeline.");

        String inputPath = inputFile.getPath();
        Format inputFormat = inputFile.getFormat();
        int skip = inputFile.getSkip();
        int skipLast = inputFile.getSkipLast();
        boolean logLines = inputFile.logLines();

        int lineIndex = 0;
        int inputCount = 0;
        int outputCount = 0;

        int lineCount;
        try (Stream<String> stream = IO.streamLines(inputPath)) {
            lineCount = (int) stream.count();
        }

        int finalIndex = lineCount - skipLast;


        log.debug("Input: " + PipelineExplainer.explainInput(inputFile));

        log.debug("Output: " + PipelineExplainer.explainOutput(outputContext));

        int parseErrorCount = 0;
        int transformErrorCount = 0;

        List<PipelineError> errors = new LinkedList<>();
        List<Record> values = new LinkedList<>();
        List<PipelineError> outputErrors;

        long start = System.nanoTime();

        log.debug("Opening file.");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath));
             OutputChannel<T> outputChannel = outputContext.buildChannel()) {

            log.debug("Skipping " + skip + " lines.");
            while (lineIndex < skip && lineIndex < lineCount) {
                lineIndex++;
                reader.readLine();
            }

            int number = 0;

            log.debug("Reading records.");
            while (lineIndex < finalIndex) {
                number++;
                inputCount++;
                lineIndex++;
                String inputLine = reader.readLine();

                if (logLines) {
                    log.debug("Processing record #" + number + ":\n" + inputLine);
                }

                ParseResult parseResult = inputFormat.parse(inputLine);

                if (parseResult.hasProblems()) {
                    parseErrorCount++;
                    errors.add(ParsePipelineError.fromResult(number, inputLine, parseResult));
                } else {
                    Record inputRecord = parseResult.getValue();

                    TransformResult transformResult = transform.run(inputRecord);

                    if (transformResult.isOk()) {
                        Record value = transformResult.getRecord();

                        boolean ok = outputChannel.receive(number, inputLine, value);

                        if (ok) {
                            outputCount++;
                        }
                    }

                    if (transformResult.isNotOk() && transformResult.isPresent()) {
                        transformErrorCount++;
                        errors.add(TransformPipelineError.fromResult(number, inputLine, transformResult));
                    }
                }
            }

            long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            long s = TimeUnit.MILLISECONDS.toSeconds(ms);

            log.debug(String.format("Processed %d records in %dms (%ds).",
                                    inputCount,
                                    ms,
                                    s));

            outputErrors = outputChannel.getErrors();

            errors.addAll(outputErrors);

            int errorCount = errors.size();

            T value = outputChannel.getValue();

            PipelineResult<T> result = PipelineResult.of(value, inputFile, outputContext, inputCount, outputCount, errors);

            log.debug("Input count: " + inputCount);
            log.debug("Output count: " + outputCount);

            if (result.isNotOk()) {
                log.debug("Parse errors: " + parseErrorCount);
                log.debug("Transform errors: " + transformErrorCount);
                log.debug("Output errors: " + outputErrors.size());
            }

            if (result.isOk()) {
                log.debug("Pipeline completed successfully with no errors.");
            } else {
                String noun = errorCount == 1 ? "error" : "errors";
                log.debug(String.format("Pipeline completed with %d %s.", errorCount, noun));
            }

            return result;
        } catch (IOException ex) {
            throw new UncheckedIOException("I/O error while running pipeline.", ex);
        }
    }
}

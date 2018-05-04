package fun.mike.flapjack.pipeline.lab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.io.alpha.IO;
import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipelineInternals {
    private static final Logger log = LoggerFactory.getLogger(PipelineInternals.class);

    public static <T> PipelineResult<T> runWithOutputChannel(FlatInputFile flatInputFile,
                                                             Transform transform,
                                                             OutputContext<T> outputContext) {
        String inputPath = flatInputFile.getPath();
        Format inputFormat = flatInputFile.getFormat();
        int skip = flatInputFile.getSkip();
        int skipLast = flatInputFile.getSkipLast();

        log.debug("Input path: " + inputPath);
        log.debug("Input format: " + inputFormat);
        log.debug("Skip: " + inputPath);
        log.debug("Skip Last: " + skipLast);

        long lineIndex = 0;
        long inputCount = 0;
        long outputCount = 0;
        long lineCount = countLines(inputPath);
        long finalIndex = lineCount - skipLast;

        log.debug("Total line count: " + lineCount);

        long parseErrorCount = 0;
        long transformErrorCount = 0;

        List<PipelineError> errors = new LinkedList<>();
        List<Record> values = new LinkedList<>();
        List<PipelineError> outputErrors;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath));
             OutputChannel<T> outputChannel = outputContext.buildChannel()) {

            log.debug("Skipping " + skip + " lines.");
            while (lineIndex < skip && lineIndex < lineCount) {
                lineIndex++;
                reader.readLine();
            }

            long number = 0;

            log.debug("Reading records.");
            while (lineIndex < finalIndex) {
                number++;
                inputCount++;
                lineIndex++;
                String inputLine = reader.readLine();

                log.trace("Processing line: |" + inputLine + "|");
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

            outputErrors = outputChannel.getErrors();

            log.debug("Input count: " + inputCount);
            log.debug("Output count: " + outputCount);
            log.debug("Parse errors: " + parseErrorCount);
            log.debug("Transform errors: " + transformErrorCount);
            log.debug("Output errors: " + outputErrors.size());

            errors.addAll(outputErrors);

            T value = outputChannel.getValue();

            PipelineResult<T> result = PipelineResult.of(value, flatInputFile, outputContext, inputCount, outputCount, errors);

            if (result.isOk()) {
                log.debug("Pipeline completed with no errors.");
            } else {
                log.debug(String.format("Pipeline completed with %d errors.", result.getErrorCount()));
            }

            return result;
        } catch (IOException ex) {
            throw new UncheckedIOException("I/O error while running pipeline.", ex);
        }
    }

    private static long countLines(String path) {
        try (Stream<String> stream = IO.streamLines(path)) {
            return stream.count();
        }
    }
}

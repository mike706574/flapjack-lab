package fun.mike.flapjack.pipeline.lab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.io.alpha.IO;
import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipelineInternals {
    private static final Logger log = LoggerFactory.getLogger(PipelineInternals.class);

    public static CommonPipelineResult runWithOutputChannel(InputFile inputFile,
            List<Operation> operations,
            OutputChannel outputChannel) {
        String inputPath = inputFile.getPath();
        Format inputFormat = inputFile.getFormat();
        int skip = inputFile.getSkip();
        int skipLast = inputFile.getSkipLast();

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

        List<ParseResult> parseErrors = new LinkedList<>();
        List<TransformResult> transformErrors = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {

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
                    parseErrors.add(parseResult);
                } else {
                    Record inputRecord = parseResult.getValue();

                    TransformResult transformResult = process(operations, number, inputLine, inputRecord);

                    if (transformResult.isOk()) {
                        Record value = transformResult.getRecord();

                        boolean ok = outputChannel.receive(value);

                        if (ok) {
                            outputCount++;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            // TODO
            throw new UncheckedIOException(ex);
        }

        log.debug("Input count: " + inputCount);
        log.debug("Output count: " + outputCount);
        log.debug("Parse errors: " + parseErrors.size());
        log.debug("Transform errors: " + transformErrors.size());

        return new CommonPipelineResult(inputCount,
                                        outputCount,
                                        parseErrors,
                                        transformErrors);
    }

    private static TransformResult process(List<Operation> operations, Long number, String line, Record inputRecord) {
        Record outputRecord = inputRecord;
        String operationId = null;
        int operationIndex = 0;
        try {
            for (Operation operation : operations) {
                operationIndex++;
                operationId = operation.getId();
                Optional<Record> result = operation.run(outputRecord);

                if (!result.isPresent()) {
                    return TransformResult.empty(number, operationId, operationIndex, line, outputRecord);
                }

                outputRecord = result.get();
            }
            return TransformResult.ok(number, line, outputRecord);
        } catch (Exception ex) {
            return TransformResult.error(number, operationId, operationIndex, line, outputRecord, ex);
        }
    }

    private static long countLines(String path) {
        try (Stream<String> stream = IO.streamLines(path)) {
            return stream.count();
        }
    }
}

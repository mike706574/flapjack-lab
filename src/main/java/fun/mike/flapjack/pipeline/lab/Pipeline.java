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
import fun.mike.flapjack.alpha.SerializationResult;
import fun.mike.io.alpha.IO;
import fun.mike.io.alpha.Spitter;
import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pipeline {
    private static final Logger log = LoggerFactory.getLogger(Pipeline.class);
    private final InputFile inputFile;
    private final List<Operation> operations;
    private final OutputFile outputFile;

    public Pipeline(InputFile inputFile, List<Operation> operations, OutputFile outputFile) {
        this.inputFile = inputFile;
        this.operations = operations;
        this.outputFile = outputFile;
    }

    public static InputFileBuilder from(String path, Format format) {
        return new InputFileBuilder(path, format);
    }

    private static final class Result {
        public int inputCount;
        public int outputCount;

        public Result() {
            this.inputCount = 0;
            this.outputCount = 0;
        }
    }

    public PipelineResult run() {
        log.debug("Running flow.");

        String inputPath = inputFile.path;
        Format inputFormat = inputFile.format;
        int skip = inputFile.skip;
        int skipLast = inputFile.skipLast;

        String outputPath = outputFile.path;
        Format outputFormat = outputFile.format;

        log.debug("Input path: " + inputPath);
        log.debug("Input format: " + outputFormat);
        log.debug("Skip: " + inputPath);
        log.debug("Skip Last: " + skipLast);

        log.debug("Output path: " + outputPath);
        log.debug("Output format: " + outputFormat);

        long lineIndex = 0;
        long inputCount = 0;
        long outputCount = 0;
        long lineCount = countLines(inputPath);
        long finalIndex = lineCount - skipLast;

        log.debug("Total line count: " + lineCount);

        List<ParseResult> parseErrors = new LinkedList<>();
        List<TransformResult> transformErrors = new LinkedList<>();
        List<SerializationResult> serializationErrors = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(inputPath));
            Spitter spitter = new Spitter(outputPath)) {

            log.debug("Skipping " + skip + " lines.");
            while(lineIndex < skip && lineIndex < lineCount) {
                lineIndex++;
                reader.readLine();
            }

            long number = 0;

            log.debug("Reading records.");
            while(lineIndex < finalIndex) {
                number++;
                inputCount++;
                lineIndex++;
                String inputLine = reader.readLine();

                log.trace("Processing line: |" + inputLine + "|");
                ParseResult parseResult = inputFormat.parse(inputLine);

                if(parseResult.hasProblems()) {
                    parseErrors.add(parseResult);
                }
                else {
                    Record inputRecord = parseResult.getValue();

                    TransformResult transformResult = process(number, inputLine, inputRecord);

                    if(transformResult.isOk()) {
                        Record value = transformResult.getRecord();

                        SerializationResult serializationResult = outputFormat.serialize(value);

                        if(serializationResult.isOk()) {
                            String outputLine = serializationResult.getValue();
                            outputCount++;
                            spitter.spit(outputLine);
                        }
                        else {
                            serializationErrors.add(serializationResult);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        PipelineResult result = new PipelineResult(inputCount,
                                                   outputCount,
                                                   parseErrors,
                                                   transformErrors,
                                                   serializationErrors);

        log.debug("Input count: " + inputCount);
        log.debug("Output count: " + outputCount);
        log.debug("Parse errors: " + parseErrors.size());
        log.debug("Transform errors: " + transformErrors.size());
        log.debug("Serialization errors: " + serializationErrors.size());
        if(result.isOk()) {
            log.debug("Pipeline completed with no errors.");
        } else {
            log.debug(String.format("Pipeline completed with %d errors.", result.getErrorCount()));
        }

        return result;
    }

    private TransformResult process(Long number, String line, Record inputRecord) {
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
        }
        catch(Exception ex) {
            return TransformResult.error(number, operationId, operationIndex, line, outputRecord, ex);
        }
    }

    private static long countLines(String path) {
        try(Stream<String> stream = IO.streamLines(path)) {
            return stream.count();
        }
    }
}

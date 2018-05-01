package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.SerializationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileToFilePipeline {
    private static final Logger log = LoggerFactory.getLogger(FileToFilePipeline.class);
    private final InputFile inputFile;
    private final List<Operation> operations;
    private final OutputFile outputFile;

    public FileToFilePipeline(InputFile inputFile, List<Operation> operations, OutputFile outputFile) {
        this.inputFile = inputFile;
        this.operations = operations;
        this.outputFile = outputFile;
    }

    private static final class Result {
        public int inputCount;
        public int outputCount;

        public Result() {
            this.inputCount = 0;
            this.outputCount = 0;
        }
    }

    public FilePipelineResult run() {
        log.debug("Running flow.");

        String outputPath = outputFile.getPath();
        Format outputFormat = outputFile.getFormat();

        log.debug("Output path: " + outputPath);
        log.debug("Output format: " + outputFormat);

        try (FileOutputChannel outputChannel = new FileOutputChannel(outputFile.getPath(),
                                                                     outputFile.getFormat())) {
            CommonPipelineResult commonResult = PipelineInternals.runWithOutputChannel(inputFile,
                                                                                 operations,
                                                                                 outputChannel);

            List<SerializationResult> serializationErrors = outputChannel.getSerializationErrors();

            FilePipelineResult result = new FilePipelineResult(commonResult, serializationErrors);

            log.debug("Serialization errors: " + serializationErrors.size());

            if (result.isOk()) {
                log.debug("Pipeline completed with no errors.");
            } else {
                log.debug(String.format("Pipeline completed with %d errors.", result.getErrorCount()));
            }

            return result;
        }
    }
}

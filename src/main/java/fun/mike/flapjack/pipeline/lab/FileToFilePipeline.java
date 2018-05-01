package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileToFilePipeline implements Pipeline<Nothing> {
    private static final Logger log = LoggerFactory.getLogger(FileToFilePipeline.class);
    private final InputFile inputFile;
    private final List<Operation> operations;
    private final OutputFile outputFile;

    public FileToFilePipeline(InputFile inputFile, List<Operation> operations, OutputFile outputFile) {
        this.inputFile = inputFile;
        this.operations = operations;
        this.outputFile = outputFile;
    }

    @Override
    public PipelineResult<Nothing> execute() {
        return run();
    }

    public PipelineResult<Nothing> run() {
        log.debug("Running flow.");

        String outputPath = outputFile.getPath();
        Format outputFormat = outputFile.getFormat();

        log.debug("Output path: " + outputPath);
        log.debug("Output format: " + outputFormat);

        try (FileOutputChannel outputChannel = new FileOutputChannel(outputFile.getPath(),
                                                                     outputFile.getFormat())) {
            PipelineResult<?> result = PipelineInternals.runWithOutputChannel(inputFile,
                                                                              operations,
                                                                              outputChannel,
                                                                              false);
            return result.withValue(Nothing.value());
        }
    }

    private static final class Result {
        public int inputCount;
        public int outputCount;

        public Result() {
            this.inputCount = 0;
            this.outputCount = 0;
        }
    }
}

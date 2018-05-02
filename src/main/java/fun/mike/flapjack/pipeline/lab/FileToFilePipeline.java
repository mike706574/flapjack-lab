package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileToFilePipeline implements Pipeline<Nothing> {
    private static final Logger log = LoggerFactory.getLogger(FileToFilePipeline.class);
    private final FlatInputFile flatInputFile;
    private final List<Operation> operations;
    private final OutputContext<Nothing> outputFile;

    private FileToFilePipeline(FlatInputFile flatInputFile, List<Operation> operations, OutputContext<Nothing> outputFile) {
        this.flatInputFile = flatInputFile;
        this.operations = operations;
        this.outputFile = outputFile;
    }

    public static FileToFilePipeline of(FlatInputFile flatInputFile, List<Operation> operations, OutputContext<Nothing> outputFile) {
        return new FileToFilePipeline(flatInputFile, operations, outputFile);
    }

    @Override
    public PipelineResult<Nothing> execute() {
        return run();
    }

    public PipelineResult<Nothing> run() {
        return PipelineInternals.runWithOutputChannel(flatInputFile,
                                                      operations,
                                                      outputFile);
    }
}

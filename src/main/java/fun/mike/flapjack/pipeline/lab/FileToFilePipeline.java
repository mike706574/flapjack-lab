package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileToFilePipeline extends GenericPipeline<Nothing> {
    public FileToFilePipeline(FlatInputFile flatInputFile, List<Operation> operations, OutputContext<Nothing> outputContext) {
        super(flatInputFile, operations, outputContext);
    }

    public static FileToFilePipeline of(FlatInputFile flatInputFile, List<Operation> operations, OutputContext<Nothing> outputFile) {
        return new FileToFilePipeline(flatInputFile, operations, outputFile);
    }

    public PipelineResult<Nothing> run() {
        return execute();
    }
}

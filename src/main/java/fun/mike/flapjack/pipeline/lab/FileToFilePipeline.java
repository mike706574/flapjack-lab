package fun.mike.flapjack.pipeline.lab;

public class FileToFilePipeline extends GenericPipeline<Nothing> {
    public FileToFilePipeline(FlatInputFile flatInputFile, Transform transform, OutputContext<Nothing> outputContext) {
        super(flatInputFile, transform, outputContext);
    }

    public PipelineResult<Nothing> run() {
        return execute();
    }
}

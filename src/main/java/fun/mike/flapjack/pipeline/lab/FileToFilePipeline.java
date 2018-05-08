package fun.mike.flapjack.pipeline.lab;

public class FileToFilePipeline extends GenericPipeline<Nothing> {
    public FileToFilePipeline(InputContext inputContext, Transform transform, OutputContext<Nothing> outputContext) {
        super(inputContext, transform, outputContext);
    }

    public PipelineResult<Nothing> run() {
        return execute();
    }
}

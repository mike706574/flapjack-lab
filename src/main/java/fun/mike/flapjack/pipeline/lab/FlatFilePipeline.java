package fun.mike.flapjack.pipeline.lab;

public class FlatFilePipeline extends GenericPipeline<Nothing> {
    public FlatFilePipeline(InputContext inputContext, Transform transform, OutputContext<Nothing> outputContext) {
        super(inputContext, transform, outputContext);
    }

    public PipelineResult<Nothing> run() {
        return execute();
    }
}

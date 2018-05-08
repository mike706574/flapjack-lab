package fun.mike.flapjack.pipeline.lab;

public class ReducePipeline<T> extends GenericPipeline<T> {
    public ReducePipeline(InputContext inputContext, Transform transform, OutputContext<T> outputContext) {
        super(inputContext, transform, outputContext);
    }

    public PipelineResult<T> run() {
        return execute();
    }
}

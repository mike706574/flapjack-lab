package fun.mike.flapjack.pipeline.lab;

public class ReducePipeline<T> extends GenericPipeline<T> {
    public ReducePipeline(FlatInputFile flatInputFile, Transform transform, OutputContext<T> outputContext) {
        super(flatInputFile, transform, outputContext);
    }

    public PipelineResult<T> run() {
        return execute();
    }
}

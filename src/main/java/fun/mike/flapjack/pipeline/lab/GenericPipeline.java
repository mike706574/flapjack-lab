package fun.mike.flapjack.pipeline.lab;

public class GenericPipeline<V> implements Pipeline<V> {
    private final InputContext inputContext;
    private final Transform transform;
    private final OutputContext<V> outputContext;

    public GenericPipeline(InputContext inputContext,
                           Transform transform,
                           OutputContext<V> outputContext) {
        this.inputContext = inputContext;
        this.transform = transform;
        this.outputContext = outputContext;
    }

    @Override
    public PipelineResult<V> execute() {
        return runWithOutputChannel(inputContext,
                                    transform,
                                    outputContext);
    }
}

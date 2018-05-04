package fun.mike.flapjack.pipeline.lab;

public class GenericPipeline<V> implements Pipeline<V> {
    private final FlatInputFile flatInputFile;
    private final GenericTransform transform;
    private final OutputContext<V> outputContext;

    public GenericPipeline(FlatInputFile flatInputFile,
                           GenericTransform transform,
                           OutputContext<V> outputContext) {
        this.flatInputFile = flatInputFile;
        this.transform = transform;
        this.outputContext = outputContext;
    }

    @Override
    public PipelineResult<V> execute() {
        return PipelineInternals.runWithOutputChannel(flatInputFile,
                                                      transform,
                                                      outputContext);
    }
}

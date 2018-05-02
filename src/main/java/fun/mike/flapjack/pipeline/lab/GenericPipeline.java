package fun.mike.flapjack.pipeline.lab;

import java.util.List;

public class GenericPipeline<V> implements Pipeline<V> {
    private final FlatInputFile flatInputFile;
    private final List<Operation> operations;
    private final OutputContext<V> outputContext;

    public GenericPipeline(FlatInputFile flatInputFile,
                           List<Operation> operations,
                           OutputContext<V> outputContext) {
        this.flatInputFile = flatInputFile;
        this.operations = operations;
        this.outputContext = outputContext;
    }

    @Override
    public PipelineResult<V> execute() {
        return PipelineInternals.runWithOutputChannel(flatInputFile,
                                                      operations,
                                                      outputContext);
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.List;

public class ReducingPipeline<T> implements Pipeline<T> {
    private final FlatInputFile flatInputFile;
    private final List<Operation> operations;
    private final OutputContext<T> outputContext;

    public ReducingPipeline(FlatInputFile flatInputFile,
                            List<Operation> operations,
                            OutputContext<T> outputContext) {
        this.flatInputFile = flatInputFile;
        this.operations = operations;
        this.outputContext = outputContext;
    }

    @Override
    public PipelineResult<T> execute() {
        return run();
    }

    public PipelineResult<T> run() {
        return PipelineInternals.runWithOutputChannel(flatInputFile,
                                                      operations,
                                                      outputContext);
    }
}

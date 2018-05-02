package fun.mike.flapjack.pipeline.lab;

import java.util.List;

public class ReducePipeline<T> extends GenericPipeline<T> {
    public ReducePipeline(FlatInputFile flatInputFile, List<Operation> operations, OutputContext<T> outputContext) {
        super(flatInputFile, operations, outputContext);
    }

    public PipelineResult<T> run() {
        return execute();
    }
}

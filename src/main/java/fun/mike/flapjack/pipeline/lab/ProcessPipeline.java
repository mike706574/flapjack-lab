package fun.mike.flapjack.pipeline.lab;

import java.util.List;

public class ProcessPipeline<T> extends GenericPipeline<List<T>> {
    public ProcessPipeline(FlatInputFile flatInputFile, Transform transform, OutputContext<List<T>> outputContext) {
        super(flatInputFile, transform, outputContext);
    }

    public ProcessPipelineResult<T> run() {
        return new ProcessPipelineResult<>(execute());
    }
}

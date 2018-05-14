package fun.mike.flapjack.pipeline.lab;

import java.util.List;

public class ProcessPipeline<T> extends GenericPipeline<List<T>> {
    public ProcessPipeline(InputContext inputContext, Transform transform, OutputContext<List<T>> outputContext) {
        super(inputContext, transform, outputContext);
    }

    public ProcessResult<T> run() {
        return new ProcessResult<>(execute());
    }
}

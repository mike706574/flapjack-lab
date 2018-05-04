package fun.mike.flapjack.pipeline.lab;

import java.util.List;

public class ProcessPipelineResult<T> extends PipelineResult<List<T>> {
    protected ProcessPipelineResult(PipelineResult<List<T>> result) {
        super(result.getValue(), result.getInputFile(), result.getOutputContext(), result.getInputCount(), result.getOutputCount(), result.getErrors());
    }
}

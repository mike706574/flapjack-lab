package fun.mike.flapjack.pipeline.lab;

import java.util.List;

public class ProcessResult<T> extends PipelineResult<List<T>> {
    protected ProcessResult(PipelineResult<List<T>> result) {
        super(result.getValue(), result.getInputContext(), result.getOutputContext(), result.getInputCount(), result.getOutputCount(), result.getFailures());
    }
}
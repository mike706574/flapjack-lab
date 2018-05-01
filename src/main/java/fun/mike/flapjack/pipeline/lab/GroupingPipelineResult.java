package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;

import fun.mike.record.alpha.Record;

public class GroupingPipelineResult<G> extends PipelineResult<Map<G, List<Record>>> {
    private GroupingPipelineResult(Map<G, List<Record>> value, Long inputCount, Long outputCount, List<PipelineError> errors) {
        super(value, inputCount, outputCount, errors);
    }

    private GroupingPipelineResult(PipelineResult<Map<G, List<Record>>> result) {
        this(result.getValue(), result.getInputCount(), result.getOutputCount(), result.getErrors());
    }

    public static <G> GroupingPipelineResult<G> build(PipelineResult<Map<G, List<Record>>> result) {
        return new GroupingPipelineResult<>(result);
    }
}

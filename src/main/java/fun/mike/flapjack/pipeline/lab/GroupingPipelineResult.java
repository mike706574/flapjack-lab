package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;

import fun.mike.record.alpha.Record;

public class GroupingPipelineResult<G> extends PipelineResult<Map<G, List<Record>>> {
    public GroupingPipelineResult(PipelineResult<Map<G, List<Record>>> result) {
        super(result.getValue(), result.getInputFile(), result.getOutputContext(), result.getInputCount(), result.getOutputCount(), result.getErrors());
    }
}

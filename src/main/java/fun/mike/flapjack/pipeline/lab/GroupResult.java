package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;

import fun.mike.record.alpha.Record;

public class GroupResult<G> extends PipelineResult<Map<G, List<Record>>> {
    public GroupResult(PipelineResult<Map<G, List<Record>>> result) {
        super(result.getValue(), result.getInputContext(), result.getOutputContext(), result.getInputCount(), result.getOutputCount(), result.getErrors());
    }
}

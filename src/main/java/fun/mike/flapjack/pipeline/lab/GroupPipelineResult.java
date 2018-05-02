package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;

import fun.mike.record.alpha.Record;

public class GroupPipelineResult<G> extends PipelineResult<Map<G, List<Record>>> {
    public GroupPipelineResult(PipelineResult<Map<G, List<Record>>> result) {
        super(result.getValue(), result.getInputFile(), result.getOutputContext(), result.getInputCount(), result.getOutputCount(), result.getErrors());
    }
}

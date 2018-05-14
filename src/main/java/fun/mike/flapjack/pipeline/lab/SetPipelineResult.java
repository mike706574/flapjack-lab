package fun.mike.flapjack.pipeline.lab;

import java.util.Set;

import fun.mike.record.alpha.Record;

public class SetPipelineResult extends PipelineResult<Set<Record>> {
    public SetPipelineResult(PipelineResult<Set<Record>> result) {
        super(result.getValue(), result.getInputContext(), result.getOutputContext(), result.getInputCount(), result.getOutputCount(), result.getErrors());
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.record.alpha.Record;

public class SequentialPipelineResult extends PipelineResult<List<Record>> {
    public SequentialPipelineResult(PipelineResult<List<Record>> result) {
        super(result.getValue(), result.getInputFile(), result.getOutputContext(), result.getInputCount(), result.getOutputCount(), result.getErrors());
    }
}

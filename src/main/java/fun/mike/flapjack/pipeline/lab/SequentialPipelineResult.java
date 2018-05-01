package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.record.alpha.Record;

public class SequentialPipelineResult extends PipelineResult<List<Record>> {
    private SequentialPipelineResult(List<Record> value, Long inputCount, Long outputCount, List<PipelineError> errors) {
        super(value, inputCount, outputCount, errors);
    }

    public static SequentialPipelineResult build(PipelineResult<List<Record>> result) {
        return new SequentialPipelineResult(result.getValue(), result.getInputCount(), result.getOutputCount(), result.getErrors());
    }
}

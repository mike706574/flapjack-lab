package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.record.alpha.Record;

public class ListResult extends PipelineResult<List<Record>> {
    public ListResult(PipelineResult<List<Record>> result) {
        super(result.getValue(), result.getInputContext(), result.getOutputContext(), result.getInputCount(), result.getOutputCount(), result.getFailures());
    }
}

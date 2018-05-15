package fun.mike.flapjack.pipeline.lab;

public class FlatFileResult extends PipelineResult<Nothing> {
    public FlatFileResult(PipelineResult<Nothing> result) {
        super(result.getValue(), result.getInputContext(), result.getOutputContext(), result.getInputCount(), result.getOutputCount(), result.getFailures());
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.Problem;
import fun.mike.flapjack.alpha.SerializationResult;
import fun.mike.record.alpha.Record;

public class SerializationPipelineError implements PipelineError {
    private final Long number;
    private final String line;
    private final Record record;
    private final List<Problem> problems;

    public SerializationPipelineError(Long number, String line, Record record, List<Problem> problems) {
        this.number = number;
        this.line = line;
        this.record = record;
        this.problems = problems;
    }

    public static SerializationPipelineError fromResult(Long number, String line, SerializationResult result) {
        return new SerializationPipelineError(number, line, result.getRecord(), result.getProblems());
    }

    @Override
    public Long getNumber() {
        return number;
    }

    @Override
    public String getLine() {
        return line;
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.Problem;
import fun.mike.flapjack.alpha.SerializationResult;
import fun.mike.record.alpha.Record;

public class SerializationPipelineError implements PipelineError {
    private final int number;
    private final String line;
    private final Record record;
    private final List<Problem> problems;

    public SerializationPipelineError(int number, String line, Record record, List<Problem> problems) {
        this.number = number;
        this.line = line;
        this.record = record;
        this.problems = problems;
    }

    public static SerializationPipelineError of(int number, String line, Record record, List<Problem> problems) {
        return new SerializationPipelineError(number, line, record, problems);
    }

    public static SerializationPipelineError fromResult(int number, String line, SerializationResult result) {
        return new SerializationPipelineError(number, line, result.getRecord(), result.getProblems());
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public String getLine() {
        return line;
    }

    @Override
    public void accept(PipelineErrorVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Record getRecord() {
        return record;
    }

    public List<Problem> getProblems() {
        return problems;
    }
}

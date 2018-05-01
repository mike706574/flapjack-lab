package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.flapjack.alpha.Problem;
import fun.mike.record.alpha.Record;

public class ParsePipelineError implements PipelineError {
    private final Long number;
    private final String line;
    private final Record record;
    private final List<Problem> problems;

    public ParsePipelineError(Long number, String line, Record record, List<Problem> problems) {
        this.number = number;
        this.line = line;
        this.record = record;
        this.problems = problems;
    }

    public static ParsePipelineError fromResult(Long number, String line, ParseResult error) {
        return new ParsePipelineError(number, line, error.getValue(), error.getProblems());
    }

    @Override
    public Long getNumber() {
        return number;
    }

    @Override
    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "ParsePipelineError{" +
                "number=" + number +
                ", line='" + line + '\'' +
                ", record=" + record +
                ", problems=" + problems +
                '}';
    }
}
package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.flapjack.alpha.Problem;
import fun.mike.record.alpha.Record;

public class ParseFailure implements Failure {
    private final int number;
    private final String line;
    private final Record record;
    private final List<Problem> problems;

    public ParseFailure(int number, String line, Record record, List<Problem> problems) {
        this.number = number;
        this.line = line;
        this.record = record;
        this.problems = problems;
    }

    public static ParseFailure of(int number, String line, Record record, List<Problem> problems) {
        return new ParseFailure(number, line, record, problems);
    }

    public static ParseFailure fromResult(int number, String line, ParseResult error) {
        return new ParseFailure(number, line, error.getValue(), error.getProblems());
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
    public void accept(FailureVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Record getRecord() {
        return record;
    }

    @Override
    public String explain() {
        DefaultFailureExplainer explainer = new DefaultFailureExplainer();
        explainer.visit(this);
        return explainer.explain();
    }

    public List<Problem> getProblems() {
        return problems;
    }

    @Override
    public String toString() {
        return "ParseFailure{" +
                "number=" + number +
                ", line='" + line + '\'' +
                ", record=" + record +
                ", problems=" + problems +
                '}';
    }
}

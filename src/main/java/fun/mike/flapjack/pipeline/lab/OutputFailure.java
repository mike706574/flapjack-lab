package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class OutputFailure implements Failure {
    private final int number;
    private final String line;
    private final Record record;
    private final Exception exception;

    private OutputFailure(int number, String line, Record record, Exception exception) {
        this.number = number;
        this.line = line;
        this.record = record;
        this.exception = exception;
    }

    public static OutputFailure build(int number, String line, Record record, Exception exception) {
        return new OutputFailure(number, line, record, exception);
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

    public Exception getException() {
        return exception;
    }
}

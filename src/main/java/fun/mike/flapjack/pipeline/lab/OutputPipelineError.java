package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class OutputPipelineError implements PipelineError {
    private final int number;
    private final String line;
    private final Record record;
    private final Exception exception;

    private OutputPipelineError(int number, String line, Record record, Exception exception) {
        this.number = number;
        this.line = line;
        this.record = record;
        this.exception = exception;
    }

    public static OutputPipelineError build(int number, String line, Record record, Exception exception) {
        return new OutputPipelineError(number, line, record, exception);
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

    public Exception getException() {
        return exception;
    }
}

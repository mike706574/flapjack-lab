package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class OutputPipelineError implements PipelineError{
    private final Long number;
    private final String line;
    private final Record record;
    private final Exception exception;

    public OutputPipelineError(Long number, String line, Record record, Exception exception) {
        this.number = number;
        this.line = line;
        this.record = record;
        this.exception = exception;
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

package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class TransformResult {
    private final boolean ok;
    private final boolean none;
    private final boolean error;
    private final Long number;
    private final String line;
    private final Record record;
    private final OperationInfo operationInfo;
    private final Exception exception;

    public TransformResult(boolean ok, boolean none, boolean error, Long number, String line, Record record, OperationInfo operationInfo, Exception exception) {
        this.ok = ok;
        this.none = none;
        this.error = error;
        this.number = number;
        this.line = line;
        this.record = record;
        this.operationInfo = operationInfo;
        this.exception = exception;
    }

    public static TransformResult ok(Long number, String line, Record record) {
        return new TransformResult(true, false, false, number, line, record, null, null);
    }

    public static TransformResult empty(Long number, String line, Record record, OperationInfo operationInfo) {
        return new TransformResult(false, true, false, number, line, record, operationInfo, null);
    }

    public static TransformResult error(Long number, String line, Record record, OperationInfo operationInfo, Exception exception) {
        return new TransformResult(false, false, true, number, line, record, operationInfo, exception);
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isNotOk() {
        return !ok;
    }

    public boolean isEmpty() {
        return none;
    }

    public boolean isPresent() {
        return !none;
    }

    public boolean hasError() {
        return error;
    }

    public Long getNumber() {
        return number;
    }

    public String getLine() {
        return line;
    }

    public Record getRecord() {
        return record;
    }

    public OperationInfo getOperationInfo() {
        return operationInfo;
    }

    public Exception getException() {
        return exception;
    }
}

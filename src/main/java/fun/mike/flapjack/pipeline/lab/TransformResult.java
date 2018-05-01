package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class TransformResult {
    private final boolean ok;
    private final boolean none;
    private final boolean error;
    private final Long number;
    private final String operationId;
    private final Long operationNumber;
    private final String line;
    private final Record record;
    private final Exception exception;

    public TransformResult(boolean ok, boolean none, boolean error, Long number, String operationId, Long operationNumber, String line, Record record, Exception exception) {
        this.ok = ok;
        this.none = none;
        this.error = error;
        this.number = number;
        this.operationId = operationId;
        this.operationNumber = operationNumber;
        this.line = line;
        this.record = record;
        this.exception = exception;
    }

    public static TransformResult ok(Long index, String line, Record record) {
        return new TransformResult(true, false, false, index, null, null, line, record, null);
    }

    public static TransformResult empty(Long index, String operationId, Long operationNumber, String line, Record record) {
        return new TransformResult(false, true, false, index, operationId, operationNumber, line, record, null);
    }

    public static TransformResult error(Long index, String operationId, Long operationNumber, String line, Record record, Exception exception) {
        return new TransformResult(false, false, true, index, operationId, operationNumber, line, record, exception);
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

    public String getLine() {
        return line;
    }

    public Record getRecord() {
        return record;
    }

    public Exception getException() {
        return exception;
    }

    public Long getNumber() {
        return number;
    }

    public String getOperationId() {
        return operationId;
    }

    public Long getOperationNumber() {
        return operationNumber;
    }
}

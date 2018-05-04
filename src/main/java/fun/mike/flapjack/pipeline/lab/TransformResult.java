package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class TransformResult {
    private final boolean ok;
    private final boolean none;
    private final boolean error;
    private final Record record;
    private final Record originalRecord;
    private final OperationInfo operationInfo;
    private final Exception exception;

    public TransformResult(boolean ok, boolean none, boolean error, Record record, Record originalRecord, OperationInfo operationInfo, Exception exception) {
        this.ok = ok;
        this.none = none;
        this.error = error;
        this.record = record;
        this.originalRecord = originalRecord;
        this.operationInfo = operationInfo;
        this.exception = exception;
    }

    public static TransformResult ok(Record record, Record originalRecord) {
        return new TransformResult(true, false, false, record, originalRecord, null, null);
    }

    public static TransformResult empty(Record record, Record originalRecord, OperationInfo operationInfo) {
        return new TransformResult(false, true, false, record, originalRecord, operationInfo, null);
    }

    public static TransformResult error(Record record, Record originalRecord, OperationInfo operationInfo, Exception exception) {
        return new TransformResult(false, false, true, record, originalRecord, operationInfo, exception);
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

    public Record getRecord() {
        return record;
    }

    public Record getOriginalRecord() {
        return originalRecord;
    }

    public OperationInfo getOperationInfo() {
        return operationInfo;
    }

    public Exception getException() {
        return exception;
    }
}

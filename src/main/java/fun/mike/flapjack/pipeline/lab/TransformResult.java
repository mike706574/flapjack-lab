package fun.mike.flapjack.pipeline.lab;

import java.util.Objects;

import fun.mike.record.alpha.Record;

public class TransformResult {
    private final boolean ok;
    private final boolean none;
    private final boolean failure;
    private final Record record;
    private final Record originalRecord;
    private final OperationInfo operationInfo;
    private final Exception exception;

    public TransformResult(boolean ok, boolean none, boolean failure, Record record, Record originalRecord, OperationInfo operationInfo, Exception exception) {
        this.ok = ok;
        this.none = none;
        this.failure = failure;
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

    public static TransformResult failure(Record record, Record originalRecord, OperationInfo operationInfo, Exception exception) {
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

    public boolean hasFailure() {
        return failure;
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

    @Override
    public String toString() {
        return "TransformResult{" +
                "ok=" + ok +
                ", none=" + none +
                ", failure=" + failure +
                ", record=" + record +
                ", originalRecord=" + originalRecord +
                ", operationInfo=" + operationInfo +
                ", exception=" + exception +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransformResult that = (TransformResult) o;
        return ok == that.ok &&
                none == that.none &&
                failure == that.failure &&
                Objects.equals(record, that.record) &&
                Objects.equals(originalRecord, that.originalRecord) &&
                Objects.equals(operationInfo, that.operationInfo) &&
                Objects.equals(exception, that.exception);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ok, none, failure, record, originalRecord, operationInfo, exception);
    }
}

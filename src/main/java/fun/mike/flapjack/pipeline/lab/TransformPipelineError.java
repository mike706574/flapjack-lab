package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class TransformPipelineError implements PipelineError {
    private final int number;
    private final String line;
    private final Record record;
    private final OperationInfo operationInfo;
    private final Exception exception;

    public TransformPipelineError(int number, String line, Record record, OperationInfo operationInfo, Exception exception) {
        this.number = number;
        this.line = line;
        this.record = record;
        this.operationInfo = operationInfo;
        this.exception = exception;
    }

    public static TransformPipelineError of(int number, String line, Record record, OperationInfo operationInfo, Exception exception) {
        return new TransformPipelineError(number, line, record, operationInfo, exception);
    }

    public static TransformPipelineError fromResult(int number, String line, TransformResult result) {
        return new TransformPipelineError(number, line, result.getRecord(), result.getOperationInfo(), result.getException());
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public String getLine() {
        return line;
    }

    public Record getRecord() {
        return record;
    }

    public OperationInfo getOperationInfo() {
        return operationInfo;
    }

    @Override
    public void accept(PipelineErrorVisitor visitor) {
        visitor.visit(this);
    }
}

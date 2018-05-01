package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class TransformPipelineError implements PipelineError {
    private final Long number;
    private final String line;
    private final Record record;
    private final String operationId;
    private final Long operationNumber;
    private final Exception exception;

    public TransformPipelineError(Long number, String line, Record record, String operationId, Long operationNumber, Exception exception) {
        this.number = number;
        this.line = line;
        this.record = record;
        this.operationId = operationId;
        this.operationNumber = operationNumber;
        this.exception = exception;
    }

    public static TransformPipelineError fromResult(Long number, String line, TransformResult result) {
        return new TransformPipelineError(number, line, result.getRecord(), result.getOperationId(), result.getOperationNumber(), result.getException());
    }

    public Exception getException() {
        return exception;
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

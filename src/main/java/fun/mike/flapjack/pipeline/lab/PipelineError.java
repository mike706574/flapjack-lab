package fun.mike.flapjack.pipeline.lab;

import java.util.Optional;

import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.flapjack.alpha.SerializationResult;
import fun.mike.record.alpha.Record;

public class PipelineError {
    private final boolean isParseError;
    private final boolean isTransformError;
    private final boolean isSerializationError;

    // All
    private final Long number;
    private final String line;

    // ParseResult
    private final ParseResult parseError;

    // Transform
    private final Record value;
    private final String getOperationId;
    private final Long operationNumber;

    // Serialization
    private final SerializationResult serializationError;

    private PipelineError(boolean isParseError, boolean isTransformError, boolean isSerializationError, Long number, String line, ParseResult parseError, Record value, String getOperationId, Long getOperationNumber, SerializationResult serializationError) {
        this.isParseError = isParseError;
        this.isTransformError = isTransformError;
        this.isSerializationError = isSerializationError;
        this.number = number;
        this.line = line;
        this.parseError = parseError;
        this.value = value;
        this.getOperationId = getOperationId;
        this.operationNumber = getOperationNumber;
        this.serializationError = serializationError;
    }

    private static PipelineError parse(Long number, String line, ParseResult parseError) {
        return new PipelineError(true, false, false, number, line, parseError, null, null, null, null);
    }

    private static PipelineError transform(Long number, String line, Record value, String operationId, Long operationNumber) {
        return new PipelineError(false, true, false, number, line, null, value, operationId, operationNumber, null);
    }

    private static PipelineError serializationError(Long number, String line, SerializationResult serializationError) {
        return new PipelineError(false, false, true, number, line, null, null, null, null, serializationError);
    }

    public boolean isParseError() {
        return isParseError;
    }

    public boolean isTransformError() {
        return isTransformError;
    }

    public boolean isSerializationError() {
        return isSerializationError;
    }

    public Long getNumber() {
        return number;
    }

    public String getLine() {
        return line;
    }

    public ParseResult getParseError() {
        return parseError;
    }

    public Record getValue() {
        return value;
    }

    public String getGetOperationId() {
        return getOperationId;
    }

    public Long getOperationNumber() {
        return operationNumber;
    }

    public SerializationResult getSerializationError() {
        return serializationError;
    }
}

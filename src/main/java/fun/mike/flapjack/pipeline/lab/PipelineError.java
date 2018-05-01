package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.flapjack.alpha.SerializationResult;

public class PipelineError {
    private final boolean isParseError;
    private final boolean isTransformError;
    private final boolean isSerializationError;

    private final Long number;
    private final String line;

    private final ParseResult parseError;
    private final TransformResult transformError;
    private final SerializationResult serializationError;

    private PipelineError(boolean isParseError, boolean isTransformError, boolean isSerializationError, Long number, String line, ParseResult parseError, TransformResult transformError, SerializationResult serializationError) {
        this.isParseError = isParseError;
        this.isTransformError = isTransformError;
        this.isSerializationError = isSerializationError;
        this.number = number;
        this.line = line;
        this.parseError = parseError;
        this.transformError = transformError;
        this.serializationError = serializationError;
    }

    public static PipelineError parse(Long number, String line, ParseResult parseError) {
        return new PipelineError(true, false, false, number, line, parseError, null, null);
    }

    public static PipelineError transform(Long number, String line, TransformResult transformError) {
        return new PipelineError(false, true, false, number, line, null, transformError, null);
    }

    public static PipelineError serialization(Long number, String line, SerializationResult serializationError) {
        return new PipelineError(false, false, true, number, line, null, null, serializationError);
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

    public TransformResult getTransformError() {
        return transformError;
    }

    public SerializationResult getSerializationError() {
        return serializationError;
    }

    @Override
    public String toString() {
        return "PipelineError{" +
                "isParseError=" + isParseError +
                ", isTransformError=" + isTransformError +
                ", isSerializationError=" + isSerializationError +
                ", number=" + number +
                ", line='" + line + '\'' +
                ", parseError=" + parseError +
                ", transformError=" + transformError +
                ", serializationError=" + serializationError +
                '}';
    }
}

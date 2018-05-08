package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class InputResult {
    private final Record value;
    private final String line;
    private final PipelineError error;

    public InputResult(Record value, String line, PipelineError error) {
        this.value = value;
        this.line = line;
        this.error = error;
    }

    public static InputResult ok(Record value, String line) {
        return new InputResult(value, line, null);
    }

    public static InputResult error(String line, PipelineError error) {
        return new InputResult(null, line, error);
    }

    public Record getValue() {
        return value;
    }

    public String getLine() {
        return line;
    }

    public boolean isOk() {
        return error == null;
    }

    public boolean hasError() {
        return error != null;
    }

    public PipelineError getError() {
        return error;
    }
}

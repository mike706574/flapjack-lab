package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class InputResult {
    private final Record value;
    private final String line;
    private final Failure failure;

    public InputResult(Record value, String line, Failure failure) {
        this.value = value;
        this.line = line;
        this.failure = failure;
    }

    public static InputResult ok(Record value, String line) {
        return new InputResult(value, line, null);
    }

    public static InputResult failure(String line, Failure failure) {
        return new InputResult(null, line, failure);
    }

    public Record getValue() {
        return value;
    }

    public String getLine() {
        return line;
    }

    public boolean isOk() {
        return failure == null;
    }

    public boolean hasFailure() {
        return failure != null;
    }

    public Failure getFailure() {
        return failure;
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import fun.mike.flapjack.alpha.Result;

public class PipelineResult<T> implements Result<T> {
    private final T value;
    private final Long inputCount;
    private final Long outputCount;
    private final List<PipelineError> errors;

    protected PipelineResult(T value, Long inputCount, Long outputCount, List<PipelineError> errors) {
        this.value = value;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.errors = errors;
    }

    public static <T> PipelineResult<T> of(T value, Long inputCount, Long outputCount, List<PipelineError> errors) {
        return new PipelineResult<>(value, inputCount, outputCount, errors);
    }

    public <U> PipelineResult<U> withValue(U value) {
        return new PipelineResult<>(value, inputCount, outputCount, errors);
    }

    public PipelineResult<T> withErrors(List<PipelineError> errors) {
        List<PipelineError> allErrors = new LinkedList<>();
        allErrors.addAll(this.errors);
        allErrors.addAll(errors);
        return new PipelineResult<>(value, inputCount, outputCount, allErrors);
    }

    public boolean isOk() {
        return errors.isEmpty();
    }

    public T getValue() {
        return value;
    }

    public T orElse(T other) {
        if (isOk()) {
            return value;
        }
        return other;
    }

    public T orElseThrow() {
        if (isOk()) {
            return value;
        }
        throw new RuntimeException("TODO");
    }

    public Long getInputCount() {
        return inputCount;
    }

    public Long getOutputCount() {
        return outputCount;
    }

    public Long getErrorCount() {
        return (long) errors.size();
    }

    public List<PipelineError> getErrors() {
        return errors;
    }

    public <U> PipelineResult<U> map(Function<T, U> mapper) {
        U newValue = mapper.apply(this.value);
        return withValue(newValue);
    }

    @Override
    public String toString() {
        return "PipelineResult{" +
                "value=" + value +
                ", inputCount=" + inputCount +
                ", outputCount=" + outputCount +
                ", errors=" + errors +
                '}';
    }
}

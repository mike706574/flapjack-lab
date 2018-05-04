package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import fun.mike.flapjack.alpha.Result;

public class PipelineResult<T> implements Result<T> {
    private final T value;
    private final FlatInputFile inputFile;
    private final OutputContext<?> outputContext;
    private final int inputCount;
    private final int outputCount;
    private final List<PipelineError> errors;

    protected PipelineResult(T value, FlatInputFile inputFile, OutputContext<?> outputContext, int inputCount, int outputCount, List<PipelineError> errors) {
        this.value = value;
        this.inputFile = inputFile;
        this.outputContext = outputContext;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.errors = errors;
    }

    public static <T> PipelineResult<T> of(T value, FlatInputFile inputFile, OutputContext<T> outputContext, int inputCount, int outputCount, List<PipelineError> errors) {
        return new PipelineResult<>(value, inputFile, outputContext, inputCount, outputCount, errors);
    }

    public <U> PipelineResult<U> withValue(U value) {
        return new PipelineResult<U>(value, inputFile, outputContext, inputCount, outputCount, errors);
    }

    public PipelineResult<T> withMoreErrors(List<PipelineError> errors) {
        List<PipelineError> allErrors = new LinkedList<>();
        allErrors.addAll(this.errors);
        allErrors.addAll(errors);
        return new PipelineResult<>(value, inputFile, outputContext, inputCount, outputCount, allErrors);
    }

    public PipelineResult<T> withErrors(List<PipelineError> errors) {
        return new PipelineResult<>(value, inputFile, outputContext, inputCount, outputCount, errors);
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

    public int getInputCount() {
        return inputCount;
    }

    public int getOutputCount() {
        return outputCount;
    }

    public int getErrorCount() {
        return errors.size();
    }

    public List<PipelineError> getErrors() {
        return errors;
    }

    public <E extends Exception> PipelineResult<T> withoutException(Class<E> exceptionType) {
        List<PipelineError> filteredErrors = errors.stream()
                .filter(error -> !hasException(error, exceptionType))
                .collect(Collectors.toList());
        return withErrors(filteredErrors);
    }

    public <E extends Exception> List<PipelineError> getErrorsByException(Class<E> exceptionType) {
        return getErrorsByType(TransformPipelineError.class)
                .stream()
                .filter(error -> exceptionType.isInstance(error.getException()))
                .collect(Collectors.toList());
    }

    private <E extends PipelineError> List<E> getErrorsByType(Class<E> errorType) {
        return errors.stream()
                .filter(errorType::isInstance)
                .map(errorType::cast)
                .collect(Collectors.toList());
    }

    private <E> boolean hasException(PipelineError error, Class<E> exceptionType) {
        if (error instanceof TransformPipelineError) {
            Exception exception = ((TransformPipelineError) error).getException();
            return exceptionType.isInstance(exception);
        }
        return false;
    }

    public <U> PipelineResult<U> map(Function<T, U> mapper) {
        U newValue = mapper.apply(this.value);
        return withValue(newValue);
    }

    public FlatInputFile getInputFile() {
        return inputFile;
    }

    public OutputContext<?> getOutputContext() {
        return outputContext;
    }

    @Override
    public String toString() {
        return "PipelineResult{" +
                "value=" + value +
                ", inputFile=" + inputFile +
                ", outputContext=" + outputContext +
                ", inputCount=" + inputCount +
                ", outputCount=" + outputCount +
                ", errors=" + errors +
                '}';
    }
}

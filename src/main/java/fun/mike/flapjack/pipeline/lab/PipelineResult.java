package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import fun.mike.flapjack.alpha.Result;

public class PipelineResult<T> implements Result<T> {
    private final T value;
    private final InputContext inputContext;
    private final OutputContext<T> outputContext;
    private final int inputCount;
    private final int outputCount;
    private final List<Failure> failures;

    protected PipelineResult(T value, InputContext inputContext, OutputContext<T> outputContext, int inputCount, int outputCount, List<Failure> failures) {
        this.value = value;
        this.inputContext = inputContext;
        this.outputContext = outputContext;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.failures = failures;
    }

    public static <T> PipelineResult<T> of(T value, InputContext inputContext, OutputContext<T> outputContext, int inputCount, int outputCount, List<Failure> failures) {
        return new PipelineResult<>(value, inputContext, outputContext, inputCount, outputCount, failures);
    }

    public <U> PipelineResult<U> withValue(U value) {
        OutputContext<U> outputContext = new ConstantOutputContext<>(value);
        return new PipelineResult<>(value, inputContext, outputContext, inputCount, outputCount, failures);
    }

    public PipelineResult<T> withMoreFailures(List<Failure> failures) {
        List<Failure> allFailures = new LinkedList<>();
        allFailures.addAll(this.failures);
        allFailures.addAll(failures);
        return new PipelineResult<>(value, inputContext, outputContext, inputCount, outputCount, allFailures);
    }

    public PipelineResult<T> withFailures(List<Failure> failures) {
        return new PipelineResult<>(value, inputContext, outputContext, inputCount, outputCount, failures);
    }

    public boolean isOk() {
        return failures.isEmpty();
    }

    public boolean isNotOk() {
        return !failures.isEmpty();
    }

    public boolean hasFailures() { return !failures.isEmpty(); }

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

    public int getFailureCount() {
        return failures.size();
    }

    public List<Failure> getFailures() {
        return failures;
    }

    public <U> PipelineResult<U> merge(PipelineResult<U> result) {
        List<Failure> mergedFailures = new LinkedList<>();
        mergedFailures.addAll(failures);
        mergedFailures.addAll(result.getFailures());
        U value = result.getValue();
        OutputContext<U> outputContext = result.getOutputContext();
        return PipelineResult.of(value, inputContext, outputContext, inputCount, result.getOutputCount(), mergedFailures);
    }

    public <E extends Exception> PipelineResult<T> withoutException(Class<E> exceptionType) {
        List<Failure> filteredFailures = failures.stream()
                .filter(error -> !hasException(error, exceptionType))
                .collect(Collectors.toList());
        return withFailures(filteredFailures);
    }

    public <E extends Exception> List<Failure> getFailuresByException(Class<E> exceptionType) {
        return getFailuresByType(TransformFailure.class)
                .stream()
                .filter(error -> exceptionType.isInstance(error.getException()))
                .collect(Collectors.toList());
    }

    private <E extends Failure> List<E> getFailuresByType(Class<E> failureType) {
        return failures.stream()
                .filter(failureType::isInstance)
                .map(failureType::cast)
                .collect(Collectors.toList());
    }

    private <E> boolean hasException(Failure failure, Class<E> exceptionType) {
        if (failure instanceof TransformFailure) {
            Exception exception = ((TransformFailure) failure).getException();
            return exceptionType.isInstance(exception);
        }
        return false;
    }

    public <U> PipelineResult<U> map(Function<T, U> mapper) {
        U newValue = mapper.apply(this.value);
        return withValue(newValue);
    }

    public InputContext getInputContext() {
        return inputContext;
    }

    public OutputContext<T> getOutputContext() {
        return outputContext;
    }

    @Override
    public String toString() {
        return "PipelineResult{" +
                "value=" + value +
                ", inputContext=" + inputContext +
                ", outputContext=" + outputContext +
                ", inputCount=" + inputCount +
                ", outputCount=" + outputCount +
                ", failures=" + failures +
                '}';
    }
}

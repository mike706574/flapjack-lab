package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import fun.mike.record.alpha.Record;

public class ReduceOutputChannel<T> implements OutputChannel<T> {
    private final T identityValue;
    private final BiFunction<T, Record, T> reducer;
    private T reducedValue;
    private List<PipelineError> errors;

    public ReduceOutputChannel(T identityValue, BiFunction<T, Record, T> reducer) {
        this.identityValue = identityValue;
        this.reducer = reducer;
        this.reducedValue = identityValue;
        this.errors = new LinkedList<>();
    }

    @Override
    public boolean receive(int number, String line, Record value) {
        try {
            reducedValue = reducer.apply(reducedValue, value);
            return true;
        } catch (Exception ex) {
            errors.add(OutputPipelineError.build(number, line, value, ex));
            return false;
        }
    }

    @Override
    public List<PipelineError> getErrors() {
        return errors;
    }

    public T getValue() {
        return reducedValue;
    }

    @Override
    public void close() {}
}

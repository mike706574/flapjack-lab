package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import fun.mike.record.alpha.Record;

public class ReducingOutputChannel<T> implements OutputChannel {
    private final BiFunction<T, Record, T> reducer;
    private final T identityValue;
    private T reducedValue;
    private List<PipelineError> errors;

    public ReducingOutputChannel(T identityValue, BiFunction<T, Record, T> reducer) {
        this.identityValue = identityValue;
        this.reducedValue = identityValue;
        this.reducer = reducer;
        this.errors = new LinkedList<>();
    }

    @Override
    public boolean receive(Long number, String line, Record value) {
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
    public void close() {
    }
}

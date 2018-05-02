package fun.mike.flapjack.pipeline.lab;

import java.util.function.BiFunction;

import fun.mike.record.alpha.Record;

public class Reduction<T> implements OutputContext<T> {
    private final T identityValue;
    private final BiFunction<T, Record, T> reducer;

    public Reduction(T identityValue, BiFunction<T, Record, T> reducer) {
        this.identityValue = identityValue;
        this.reducer = reducer;
    }

    @Override
    public OutputChannel<T> buildChannel() {
        return new ReducingOutputChannel<T>(identityValue, reducer);
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }
}

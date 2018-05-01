package fun.mike.flapjack.pipeline.lab;

import java.util.function.BiFunction;

import fun.mike.record.alpha.Record;

public class ReducingOutputChannel<T> implements OutputChannel {
    private final BiFunction<T, Record, T> reducer;

    public ReducingOutputChannel(BiFunction<T, Record, T> reducer) {
        this.reducer = reducer;
    }

    @Override
    public boolean receive(Long number, String line, Record value) {
        return false;
    }

    @Override
    public void close() {

    }
}

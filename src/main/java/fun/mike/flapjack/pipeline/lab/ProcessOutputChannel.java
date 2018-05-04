package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import fun.mike.record.alpha.Record;

public class ProcessOutputChannel<T> implements OutputChannel<List<T>> {
    private final Function<Record, T> processor;
    private final List<T> values;

    public ProcessOutputChannel(Function<Record, T> processor) {
        this.processor = processor;
        values = new LinkedList<>();
    }

    @Override
    public boolean receive(int number, String line, Record value) {
        values.add(processor.apply(value));
        return true;
    }

    @Override
    public List<T> getValue() {
        return values;
    }

    @Override
    public void close() {}
}

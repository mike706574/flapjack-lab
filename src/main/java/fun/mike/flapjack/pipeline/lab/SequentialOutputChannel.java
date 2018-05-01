package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;

import fun.mike.record.alpha.Record;

public class SequentialOutputChannel implements OutputChannel {
    private final List<Record> values;

    public SequentialOutputChannel() {
        this.values = new LinkedList<>();
    }

    @Override
    public boolean receive(Record value) {
        values.add(value);
        return true;
    }

    public List<Record> getValues() {
        return new LinkedList<>(values);
    }

    @Override
    public void close() {}
}

package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;

import fun.mike.record.alpha.Record;

public class SequentialOutputChannel implements OutputChannel<List<Record>> {
    private final List<Record> records;

    public SequentialOutputChannel() {
        records = new LinkedList<>();
    }

    public static SequentialOutputChannel build() {
        return new SequentialOutputChannel();
    }

    @Override
    public boolean receive(Long number, String line, Record value) {
        records.add(value);
        return true;
    }

    @Override
    public List<Record> getValue() {
        return records;
    }

    @Override
    public void close() {}
}

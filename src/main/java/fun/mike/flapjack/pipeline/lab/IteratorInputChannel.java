package fun.mike.flapjack.pipeline.lab;

import java.util.Iterator;

import fun.mike.record.alpha.Record;

public class IteratorInputChannel implements InputChannel {
    private final Iterator<Record> iterator;

    public IteratorInputChannel(Iterator<Record> iterator) {
        this.iterator = iterator;
    }

    @Override
    public InputResult take() {
        return InputResult.ok(iterator.next(), null);
    }

    @Override
    public boolean hasMore() {
        return iterator.hasNext();
    }

    @Override
    public void close() {
    }
}
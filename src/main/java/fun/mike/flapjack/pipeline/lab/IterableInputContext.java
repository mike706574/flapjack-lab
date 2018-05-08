package fun.mike.flapjack.pipeline.lab;

import java.util.Iterator;

import fun.mike.record.alpha.Record;

public class IterableInputContext implements InputContext {
    private final Iterable<Record> records;

    public IterableInputContext(Iterable<Record> records) {
        this.records = records;
    }

    public Iterable<Record> getRecords() {
        return records;
    }

    @Override
    public InputChannel buildChannel() {
        return new IteratorInputChannel(records.iterator());
    }

    @Override
    public void accept(InputContextVisitor visitor) {
        visitor.accept(this);
    }

    private class IteratorInputChannel implements InputChannel {
        private final Iterator<Record> iterator;

        private IteratorInputChannel(Iterator<Record> iterator) {
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
        public void close() {}
    }
}

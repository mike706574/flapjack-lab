package fun.mike.flapjack.pipeline.lab;

import java.util.Collection;
import java.util.Iterator;

import fun.mike.record.alpha.Record;

public class CollectionInputContext implements InputContext {
    private final Collection<Record> records;

    public CollectionInputContext(Collection<Record> records) {
        this.records = records;
    }

    public Collection<Record> getRecords() {
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
}

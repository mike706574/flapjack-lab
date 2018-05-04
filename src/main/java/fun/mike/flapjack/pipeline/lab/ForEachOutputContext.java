package fun.mike.flapjack.pipeline.lab;

import java.util.function.Consumer;

import fun.mike.record.alpha.Record;

public class ForEachOutputContext implements OutputContext<Nothing> {
    private final Consumer<Record> consumer;

    public ForEachOutputContext(Consumer<Record> consumer) {
        this.consumer = consumer;
    }

    @Override
    public OutputChannel<Nothing> buildChannel() {
        return new ForEachOutputChannel(consumer);
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }
}

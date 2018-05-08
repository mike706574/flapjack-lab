package fun.mike.flapjack.pipeline.lab;

import java.util.Optional;
import java.util.function.Consumer;

import fun.mike.record.alpha.Record;

public class ForEachOutputChannel implements OutputChannel<Nothing> {
    private final Consumer<Record> consumer;

    public ForEachOutputChannel(Consumer<Record> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Optional<PipelineError> put(int number, String line, Record value) {
        consumer.accept(value);
        return Optional.empty();
    }

    @Override
    public Nothing getValue() {
        return Nothing.value();
    }

    @Override
    public void close() {}
}

package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.function.Function;

import fun.mike.record.alpha.Record;

public class ProcessOutputContext<T> implements OutputContext<List<T>> {
    private final Function<Record, T> processor;

    public ProcessOutputContext(Function<Record, T> processor) {
        this.processor = processor;
    }

    @Override
    public OutputChannel<List<T>> buildChannel() {
        return new ProcessOutputChannel<>(processor);
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }
}

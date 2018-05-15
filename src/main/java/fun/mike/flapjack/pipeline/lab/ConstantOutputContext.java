package fun.mike.flapjack.pipeline.lab;

import java.util.Optional;

import fun.mike.record.alpha.Record;

public class ConstantOutputContext<T> implements OutputContext<T> {
    private final T value;

    public ConstantOutputContext(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public OutputChannel<T> buildChannel() {
        return new ConstantOutputChannel();
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }

    private final class ConstantOutputChannel implements OutputChannel<T> {
        @Override
        public Optional<Failure> put(int number, String line, Record value) {
            return Optional.empty();
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public void close() {
        }
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import fun.mike.record.alpha.Record;

public class ListOutputContext implements OutputContext<List<Record>> {
    @Override
    public OutputChannel<List<Record>> buildChannel() {
        return new ListOutputChannel();
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }

    private final class ListOutputChannel implements OutputChannel<List<Record>> {
        private final List<Record> records;

        public ListOutputChannel() {
            records = new LinkedList<>();
        }

        @Override
        public Optional<Failure> put(int number, String line, Record value) {
            records.add(value);
            return Optional.empty();
        }

        @Override
        public List<Record> getValue() {
            return records;
        }

        @Override
        public void close() {
        }
    }
}

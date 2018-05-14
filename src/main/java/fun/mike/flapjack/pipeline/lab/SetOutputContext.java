package fun.mike.flapjack.pipeline.lab;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import fun.mike.record.alpha.Record;

public class SetOutputContext implements OutputContext<Set<Record>> {
    @Override
    public OutputChannel<Set<Record>> buildChannel() {
        return new SetOutputChannel();
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }

    private final class SetOutputChannel implements OutputChannel<Set<Record>> {
        private final Set<Record> records;

        public SetOutputChannel() {
            records = new HashSet<>();
        }

        @Override
        public Optional<PipelineError> put(int number, String line, Record value) {
            records.add(value);
            return Optional.empty();
        }

        @Override
        public Set<Record> getValue() {
            return records;
        }

        @Override
        public void close() {
        }
    }
}

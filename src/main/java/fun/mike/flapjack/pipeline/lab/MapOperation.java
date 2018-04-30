package fun.mike.flapjack.pipeline.lab;

import java.util.Optional;
import java.util.function.Function;

import fun.mike.record.alpha.Record;

public class MapOperation implements Operation {
    private final String id;
    private final Function<Record, Record> function;

    public MapOperation(String id, Function<Record, Record> function) {
        this.id = id;
        this.function = function;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Optional<Record> run(Record value) {
        Record mappedValue = function.apply(value);
        return Optional.of(mappedValue);
    }
}

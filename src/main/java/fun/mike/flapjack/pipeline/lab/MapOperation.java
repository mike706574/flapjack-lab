package fun.mike.flapjack.pipeline.lab;

import java.util.Optional;
import java.util.function.Function;

import fun.mike.record.alpha.Record;

public class MapOperation implements Operation {
    private final String id;
    private final String description;
    private final Function<Record, Record> function;

    public MapOperation(String id, String description, Function<Record, Record> function) {
        this.id = id;
        this.description = description;
        this.function = function;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Optional<Record> run(Record value) {
        Record mappedValue = function.apply(value);
        return Optional.of(mappedValue);
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.Optional;
import java.util.function.Predicate;

import fun.mike.record.alpha.Record;

public class FilterOperation implements Operation {
    private final String id;
    private final String description;
    private final Predicate<Record> predicate;

    public FilterOperation(String id, String description, Predicate<Record> predicate) {
        this.id = id;
        this.description = description;
        this.predicate = predicate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return id;
    }

    @Override
    public Optional<Record> run(Record value) {
        boolean present = predicate.test(value);
        return present ? Optional.of(value) : Optional.empty();
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.record.alpha.Record;

public interface Transform {
    TransformResult run(Record record);

    // Map
    static TransformBuilder map(Function<Record, Record> mapper) {
        return map(null, null, mapper);
    }

    static TransformBuilder map(String id, Function<Record, Record> mapper) {
        return map(id, null, mapper);
    }

    static TransformBuilder map(String id, String description, Function<Record, Record> mapper) {
        return new TransformBuilder().map(id, description, mapper);
    }

    // Filter
    static TransformBuilder filter(Predicate<Record> predicate) {
        return filter(null, null, predicate);
    }

    static TransformBuilder filter(String id, Predicate<Record> predicate) {
        return filter(id, null, predicate);
    }

    static TransformBuilder filter(String id, String description, Predicate<Record> predicate) {
        return new TransformBuilder().filter(id, description, predicate);
    }
}

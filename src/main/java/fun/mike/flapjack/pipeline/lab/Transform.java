package fun.mike.flapjack.pipeline.lab;

import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.record.alpha.Record;

public interface Transform {
    public TransformResult run(Record record);

    // Map
    public static TransformBuilder map(Function<Record, Record> mapper) {
        return map(null, null, mapper);
    }

    public static TransformBuilder map(String id, Function<Record, Record> mapper) {
        return map(id, null, mapper);
    }

    public static TransformBuilder map(String id, String description, Function<Record, Record> mapper) {
        return new TransformBuilder().map(id, description, mapper);
    }

    // Filter
    public static TransformBuilder filter(Predicate<Record> predicate) {
        return filter(null, null, predicate);
    }

    public static TransformBuilder filter(String id, Predicate<Record> predicate) {
        return filter(id, null, predicate);
    }

    public static TransformBuilder filter(String id, String description, Predicate<Record> predicate) {
        return new TransformBuilder().filter(id, description, predicate);
    }
}

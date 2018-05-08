package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.record.alpha.Record;

public class TransformBuilder {
    private final List<Operation> operations;

    public TransformBuilder() {
        operations = new LinkedList<>();
    }

    // Map
    public TransformBuilder map(Function<Record, Record> mapper) {
        return map(null, null, mapper);
    }

    public TransformBuilder map(String id, Function<Record, Record> mapper) {
        return map(id, null, mapper);
    }

    public TransformBuilder map(String id, String description, Function<Record, Record> mapper) {
        MapOperation operation = new MapOperation(id, description, mapper);
        operations.add(operation);
        return this;
    }

    // Filter
    public TransformBuilder filter(Predicate<Record> predicate) {
        return filter(null, null, predicate);
    }

    public TransformBuilder filter(String id, Predicate<Record> predicate) {
        return filter(id, null, predicate);
    }

    public TransformBuilder filter(String id, String description, Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(id, description, predicate);
        operations.add(operation);
        return this;
    }

    public Transform build() {
        return new GenericTransform(operations);
    }

    public TransformResult run(Record record) {
        return build().run(record);
    }
}

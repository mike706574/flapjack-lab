package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class TransformBuilder {
    private final FlatInputFile flatInputFile;
    private final List<Operation> steps;

    public TransformBuilder(FlatInputFile flatInputFile, List<Operation> steps) {
        this.flatInputFile = flatInputFile;
        this.steps = steps;
    }

    // Factory methods
    public static TransformBuilder mapFirst(String id, String description, FlatInputFile flatInputFile, Function<Record, Record> mapper) {
        MapOperation operation = new MapOperation(id, description, mapper);
        List<Operation> steps = new LinkedList<>();
        steps.add(operation);
        return new TransformBuilder(flatInputFile, steps);
    }

    public static TransformBuilder filterFirst(String id, String description, FlatInputFile flatInputFile, Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(id, description, predicate);
        List<Operation> steps = new LinkedList<>();
        steps.add(operation);
        return new TransformBuilder(flatInputFile, steps);
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
        steps.add(operation);
        return new TransformBuilder(flatInputFile, steps);
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
        steps.add(operation);
        return new TransformBuilder(flatInputFile, steps);
    }

    // Next
    public FlatOutputFileBuilder toFile(String path, Format format) {
        return new FlatOutputFileBuilder(flatInputFile, steps, path, format, false);
    }

    public SequentialPipeline toSequence() {
        return new SequentialPipeline(flatInputFile, steps);
    }

    public <G> GroupingPipeline<G> toGrouping(Function<Record, G> groupBy) {
        return new GroupingPipeline<>(flatInputFile, steps, groupBy);
    }

    public <T> ReducingPipeline<T> toReduction(T identityValue, BiFunction<T, Record, T> reducer) {
        Reduction<T> reduction = new Reduction<>(identityValue, reducer);
        return new ReducingPipeline<>(flatInputFile, steps, reduction);
    }
}

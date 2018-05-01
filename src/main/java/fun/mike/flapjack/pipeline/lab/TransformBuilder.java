package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class TransformBuilder {
    private final InputFile inputFile;
    private final List<Operation> steps;

    public TransformBuilder(InputFile inputFile, List<Operation> steps) {
        this.inputFile = inputFile;
        this.steps = steps;
    }

    public static TransformBuilder first(InputFile inputFile, Function<Record, Record> mapper) {
        return first(null, inputFile, mapper);
    }

    public static TransformBuilder first(String id, InputFile inputFile, Function<Record, Record> mapper) {
        MapOperation operation = new MapOperation(id, mapper);
        List<Operation> steps = new LinkedList<>();
        steps.add(operation);
        return new TransformBuilder(inputFile, steps);
    }

    public static TransformBuilder first(InputFile inputFile, Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(null, predicate);
        List<Operation> steps = new LinkedList<>();
        steps.add(operation);
        return new TransformBuilder(inputFile, steps);
    }

    public static TransformBuilder first(String id, InputFile inputFile, Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(id, predicate);
        List<Operation> steps = new LinkedList<>();
        steps.add(operation);
        return new TransformBuilder(inputFile, steps);
    }

    public TransformBuilder map(Function<Record, Record> mapper) {
        return map(null, mapper);
    }

    public TransformBuilder map(String id, Function<Record, Record> mapper) {
        MapOperation operation = new MapOperation(null, mapper);
        steps.add(operation);
        return new TransformBuilder(inputFile, steps);
    }

    public TransformBuilder filter(Predicate<Record> predicate) {
        return filter(null, predicate);
    }

    public TransformBuilder filter(String id, Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(null, predicate);
        steps.add(operation);
        return new TransformBuilder(inputFile, steps);
    }

    public FileToFilePipeline toFile(String path, Format format) {
        OutputFile outputFile = new OutputFile(path, format);
        return new FileToFilePipeline(inputFile, steps, outputFile);
    }

    public SequentialPipeline toSequence() {
        return new SequentialPipeline(inputFile, steps);
    }

    public <G> GroupingPipeline<G> toGrouping(Function<Record, G> groupBy) {
        return new GroupingPipeline<>(inputFile, steps, groupBy);
    }

    public <T> ReducingPipeline<T> toReduction(T identityValue, BiFunction<T, Record, T> reducer) {
        return new ReducingPipeline<>(inputFile, steps, identityValue, reducer);
    }
}

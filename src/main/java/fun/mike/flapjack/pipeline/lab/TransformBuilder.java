package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class TransformBuilder {
    private InputFile inputFile;
    private List<Operation> steps;

    public TransformBuilder(InputFile inputFile, List<Operation> steps) {
        this.inputFile = inputFile;
        this.steps = steps;
    }

    public static TransformBuilder first(InputFile inputFile, Function<Record, Record> mapper) {
        return first("unlabeled", inputFile, mapper);
    }

    public static TransformBuilder first(String label, InputFile inputFile, Function<Record, Record> mapper) {
        MapOperation operation = new MapOperation(label, mapper);
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

    public static TransformBuilder first(String label, InputFile inputFile, Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(label, predicate);
        List<Operation> steps = new LinkedList<>();
        steps.add(operation);
        return new TransformBuilder(inputFile, steps);
    }

    public TransformBuilder map(Function<Record, Record> mapper) {
        return map(null, mapper);
    }

    public TransformBuilder map(String label, Function<Record, Record> mapper) {
        MapOperation operation = new MapOperation(label, mapper);
        steps.add(operation);
        return new TransformBuilder(inputFile, steps);
    }

    public TransformBuilder filter(Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(null, predicate);
        steps.add(operation);
        return new TransformBuilder(inputFile, steps);
    }

    public TransformBuilder filter(String label, Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(label, predicate);
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
}

package fun.mike.flapjack.pipeline.lab;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class InputFileBuilder {
    private final String inputPath;
    private final Format inputFormat;
    private int skipFirst;
    private int skipLast;

    public InputFileBuilder(String inputPath, Format format) {
        this.inputPath = inputPath;
        this.inputFormat = format;
        this.skipFirst = 0;
        this.skipLast = 0;
    }

    // Options
    public InputFileBuilder skipFirst(int count) {
        this.skipFirst = count;
        return this;
    }

    public InputFileBuilder skipLast(int count) {
        this.skipLast = count;
        return this;
    }

    // Map
    public TransformBuilder map(Function<Record, Record> mapper) {
        return TransformBuilder.mapFirst(null, null, buildInputFile(), mapper);
    }

    public TransformBuilder map(String id, Function<Record, Record> mapper) {
        return TransformBuilder.mapFirst(id, null, buildInputFile(), mapper);
    }

    public TransformBuilder map(String id, String description, Function<Record, Record> mapper) {
        return TransformBuilder.mapFirst(id, description, buildInputFile(), mapper);
    }

    // Filter
    public TransformBuilder filter(Predicate<Record> predicate) {
        return TransformBuilder.filterFirst(null, null, buildInputFile(), predicate);
    }

    public TransformBuilder filter(String id, Predicate<Record> predicate) {
        return TransformBuilder.filterFirst(id, null, buildInputFile(), predicate);
    }

    public TransformBuilder filter(String id, String description, Predicate<Record> predicate) {
        return TransformBuilder.filterFirst(id, description, buildInputFile(), predicate);
    }

    // Next
    public FlatOutputFileBuilder toFile(String path, Format format) {
        return new FlatOutputFileBuilder(buildInputFile(), emptyList(), path, format, false);
    }

    public ListPipeline toList() {
        FlatInputFile flatInputFile = new FlatInputFile(inputPath, inputFormat, skipFirst, skipLast);
        OutputContext<List<Record>> outputContext = new ListOutputContext();
        return new ListPipeline(buildInputFile(), emptyList(), outputContext);
    }

    public <G> GroupPipeline<G> groupBy(Function<Record, G> groupBy) {
        GroupOutputContext<G> outputContext = new GroupOutputContext<>(groupBy);
        return new GroupPipeline<>(buildInputFile(), new LinkedList<>(), outputContext);
    }

    public <T> ReducePipeline<T> reduce(T identityValue, BiFunction<T, Record, T> reducer) {
        ReduceOutputContext<T> reduction = new ReduceOutputContext<>(identityValue, reducer);
        return new ReducePipeline<>(buildInputFile(), emptyList(), reduction);
    }

    // Private
    private <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    private FlatInputFile buildInputFile() {
        return new FlatInputFile(inputPath, inputFormat, skipFirst, skipLast);
    }
}

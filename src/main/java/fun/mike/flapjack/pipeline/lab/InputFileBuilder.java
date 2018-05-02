package fun.mike.flapjack.pipeline.lab;

import java.util.Collections;
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

    public SequentialPipeline toSequence() {
        FlatInputFile flatInputFile = new FlatInputFile(inputPath, inputFormat, skipFirst, skipLast);
        return new SequentialPipeline(buildInputFile(), emptyList());
    }

    public <G> GroupingPipeline<G> toGrouping(Function<Record, G> groupBy) {
        return new GroupingPipeline<>(buildInputFile(), emptyList(), groupBy);
    }

    public <T> ReducingPipeline<T> toReduction(T identityValue, BiFunction<T, Record, T> reducer) {
        Reduction<T> reduction = new Reduction<>(identityValue, reducer);
        return new ReducingPipeline<>(buildInputFile(), emptyList(), reduction);
    }

    // Private
    private <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    private FlatInputFile buildInputFile() {
        return new FlatInputFile(inputPath, inputFormat, skipFirst, skipLast);
    }
}

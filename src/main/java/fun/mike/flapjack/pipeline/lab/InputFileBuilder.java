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

    public InputFileBuilder skipFirst(int count) {
        this.skipFirst = count;
        return this;
    }

    public InputFileBuilder skipLast(int count) {
        this.skipLast = count;
        return this;
    }

    public TransformBuilder map(Function<Record, Record> mapper) {
        return TransformBuilder.first(buildInputFile(), mapper);
    }

    public TransformBuilder map(String label, Function<Record, Record> mapper) {
        return TransformBuilder.first(label, buildInputFile(), mapper);
    }

    public TransformBuilder filter(Predicate<Record> predicate) {
        return TransformBuilder.first(buildInputFile(), predicate);
    }

    public TransformBuilder filter(String label, Predicate<Record> predicate) {
        return TransformBuilder.first(label, buildInputFile(), predicate);
    }

    private InputFile buildInputFile() {
        return new InputFile(inputPath, inputFormat, skipFirst, skipLast);
    }

    public FileToFilePipeline toFile(String path, Format format) {
        OutputFile outputFile = new OutputFile(path, format);
        return new FileToFilePipeline(buildInputFile(), emptyList(), outputFile);
    }

    public SequentialPipeline toSequence() {
        InputFile inputFile = new InputFile(inputPath, inputFormat, skipFirst, skipLast);
        return new SequentialPipeline(buildInputFile(), emptyList());
    }

    public <G> GroupingPipeline<G> toGrouping(Function<Record, G> groupBy) {
        return new GroupingPipeline<>(buildInputFile(), emptyList(), groupBy);
    }

    public <T> ReducingPipeline<T> toReduction(T identityValue, BiFunction<T, Record, T> reducer) {
        return new ReducingPipeline<>(buildInputFile(), emptyList(), identityValue, reducer);
    }

    private <T> List<T> emptyList() {
        return Collections.emptyList();
    }
}

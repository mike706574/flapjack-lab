package fun.mike.flapjack.pipeline.lab;

import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class InputFileBuilder {
    private String inputPath;
    private Format inputFormat;
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
        InputFile inputFile = new InputFile(inputPath, inputFormat, skipFirst, skipLast);
        return TransformBuilder.first(inputFile, mapper);
    }

    public TransformBuilder map(String label, Function<Record, Record> mapper) {
        InputFile inputFile = new InputFile(inputPath, inputFormat, skipFirst, skipLast);
        return TransformBuilder.first(label, inputFile, mapper);
    }

    public TransformBuilder filter(Predicate<Record> predicate) {
        InputFile inputFile = new InputFile(inputPath, inputFormat, skipFirst, skipLast);
        return TransformBuilder.first(inputFile, predicate);
    }

    public TransformBuilder filter(String label, Predicate<Record> predicate) {
        InputFile inputFile = new InputFile(inputPath, inputFormat, skipFirst, skipLast);
        return TransformBuilder.first(label, inputFile, predicate);
    }
}

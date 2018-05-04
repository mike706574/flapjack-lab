package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class InputFilePipelineBuilder {
    private final String inputPath;
    private final Format inputFormat;
    private int skipFirst;
    private int skipLast;

    public InputFilePipelineBuilder(String inputPath, Format format) {
        this.inputPath = inputPath;
        this.inputFormat = format;
        this.skipFirst = 0;
        this.skipLast = 0;
    }

    // Options
    public InputFilePipelineBuilder skipFirst(int count) {
        this.skipFirst = count;
        return this;
    }

    public InputFilePipelineBuilder skipLast(int count) {
        this.skipLast = count;
        return this;
    }

    // Map
    public TransformPipelineBuilder map(Function<Record, Record> mapper) {
        return TransformPipelineBuilder.mapFirst(null, null, buildInputFile(), mapper);
    }

    public TransformPipelineBuilder map(String id, Function<Record, Record> mapper) {
        return TransformPipelineBuilder.mapFirst(id, null, buildInputFile(), mapper);
    }

    public TransformPipelineBuilder map(String id, String description, Function<Record, Record> mapper) {
        return TransformPipelineBuilder.mapFirst(id, description, buildInputFile(), mapper);
    }

    // Filter
    public TransformPipelineBuilder filter(Predicate<Record> predicate) {
        return TransformPipelineBuilder.filterFirst(null, null, buildInputFile(), predicate);
    }

    public TransformPipelineBuilder filter(String id, Predicate<Record> predicate) {
        return TransformPipelineBuilder.filterFirst(id, null, buildInputFile(), predicate);
    }

    public TransformPipelineBuilder filter(String id, String description, Predicate<Record> predicate) {
        return TransformPipelineBuilder.filterFirst(id, description, buildInputFile(), predicate);
    }

    // Transform
    public FlatOutputFilePipelineBuilder toFile(String path, Format format) {
        return new FlatOutputFilePipelineBuilder(buildInputFile(), emptyTransform(), path, format, false);
    }

    // Next
    public ListPipeline toList() {
        FlatInputFile flatInputFile = new FlatInputFile(inputPath, inputFormat, skipFirst, skipLast);
        OutputContext<List<Record>> outputContext = new ListOutputContext();
        return new ListPipeline(buildInputFile(), emptyTransform(), outputContext);
    }

    public <G> GroupPipeline<G> groupBy(Function<Record, G> groupBy) {
        GroupOutputContext<G> outputContext = new GroupOutputContext<>(groupBy);
        return new GroupPipeline<>(buildInputFile(), emptyTransform(), outputContext);
    }

    public <T> ReducePipeline<T> reduce(T identityValue, BiFunction<T, Record, T> reducer) {
        ReduceOutputContext<T> reduction = new ReduceOutputContext<>(identityValue, reducer);
        return new ReducePipeline<>(buildInputFile(), emptyTransform(), reduction);
    }

    public <T> ProcessPipeline<T> process(Function<Record, T> processor) {
        OutputContext<List<T>> outputContext = new ProcessOutputContext<>(processor);
        return new ProcessPipeline<>(buildInputFile(), emptyTransform(), outputContext);
    }

    // Private
    private Transform emptyTransform() {
        return new GenericTransform(new LinkedList<>());
    }

    private FlatInputFile buildInputFile() {
        return new FlatInputFile(inputPath, inputFormat, skipFirst, skipLast);
    }
}

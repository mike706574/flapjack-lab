package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class FlatInputFilePipelineBuilder {
    private final String inputPath;
    private final Format inputFormat;
    private int skipFirst;
    private int skipLast;
    private boolean logLines;

    public FlatInputFilePipelineBuilder(String inputPath, Format format) {
        this.inputPath = inputPath;
        this.inputFormat = format;
        this.skipFirst = 0;
        this.skipLast = 0;
        this.logLines = true;
    }

    // Options
    public FlatInputFilePipelineBuilder skipFirst(int count) {
        this.skipFirst = count;
        return this;
    }

    public FlatInputFilePipelineBuilder skipLast(int count) {
        this.skipLast = count;
        return this;
    }

    public FlatInputFilePipelineBuilder disableLineLogging() {
        this.logLines = false;
        return this;
    }

    // Map
    public OperationPipelineBuilder map(Function<Record, Record> mapper) {
        return OperationPipelineBuilder.mapFirst(null, null, buildInputContext(), mapper);
    }

    public OperationPipelineBuilder map(String id, Function<Record, Record> mapper) {
        return OperationPipelineBuilder.mapFirst(id, null, buildInputContext(), mapper);
    }

    public OperationPipelineBuilder map(String id, String description, Function<Record, Record> mapper) {
        return OperationPipelineBuilder.mapFirst(id, description, buildInputContext(), mapper);
    }

    // Filter
    public OperationPipelineBuilder filter(Predicate<Record> predicate) {
        return OperationPipelineBuilder.filterFirst(null, null, buildInputContext(), predicate);
    }

    public OperationPipelineBuilder filter(String id, Predicate<Record> predicate) {
        return OperationPipelineBuilder.filterFirst(id, null, buildInputContext(), predicate);
    }

    public OperationPipelineBuilder filter(String id, String description, Predicate<Record> predicate) {
        return OperationPipelineBuilder.filterFirst(id, description, buildInputContext(), predicate);
    }

    public TransformPipelineBuilder transform(Transform transform) {
        return new TransformPipelineBuilder(buildInputContext(), transform);
    }

    // Next
    public FlatOutputFilePipelineBuilder toFile(String path, Format format) {
        return new FlatOutputFilePipelineBuilder(buildInputContext(), emptyTransform(), path, format, false);
    }

    public ListPipeline toList() {
        OutputContext<List<Record>> outputContext = new ListOutputContext();
        return new ListPipeline(buildInputContext(), emptyTransform(), outputContext);
    }

    public <G> GroupPipeline<G> groupBy(Function<Record, G> groupBy) {
        GroupOutputContext<G> outputContext = new GroupOutputContext<>(groupBy);
        return new GroupPipeline<>(buildInputContext(), emptyTransform(), outputContext);
    }

    public <T> ReducePipeline<T> reduce(T identityValue, BiFunction<T, Record, T> reducer) {
        ReduceOutputContext<T> reduction = new ReduceOutputContext<>(identityValue, reducer);
        return new ReducePipeline<>(buildInputContext(), emptyTransform(), reduction);
    }

    public <T> ProcessPipeline<T> process(Function<Record, T> processor) {
        OutputContext<List<T>> outputContext = new ProcessOutputContext<>(processor);
        return new ProcessPipeline<>(buildInputContext(), emptyTransform(), outputContext);
    }

    // Private
    private Transform emptyTransform() {
        return new GenericTransform(new LinkedList<>());
    }

    private InputContext buildInputContext() {
        return new FlatFileInputContext(inputPath, inputFormat, skipFirst, skipLast, logLines);
    }
}

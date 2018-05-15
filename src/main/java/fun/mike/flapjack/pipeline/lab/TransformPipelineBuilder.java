package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class TransformPipelineBuilder {
    private final InputContext inputContext;
    private final Transform transform;

    public TransformPipelineBuilder(InputContext inputContext, Transform transform) {
        this.inputContext = inputContext;
        this.transform = transform;
    }

    public FlatOutputFilePipelineBuilder toFile(String path, Format format) {
        return new FlatOutputFilePipelineBuilder(inputContext, transform, path, format);
    }

    public ListPipeline toList() {
        OutputContext<List<Record>> outputContext = new ListOutputContext();
        return new ListPipeline(inputContext, transform, outputContext);
    }

    public SetPipeline toSet() {
        OutputContext<Set<Record>> outputContext = new SetOutputContext();
        return new SetPipeline(inputContext, transform, outputContext);
    }

    public <T> GenericPipeline<T> toContext(OutputContext<T> outputContext) {
        return new GenericPipeline<T>(inputContext, transform, outputContext);
    }

    public <G> GroupPipeline<G> groupBy(Function<Record, G> groupBy) {
        OutputContext<Map<G, List<Record>>> outputContext = new GroupOutputContext<>(groupBy);
        return new GroupPipeline<>(inputContext, transform, outputContext);
    }

    public <T> ReducePipeline<T> reduce(T identityValue, BiFunction<T, Record, T> reducer) {
        ReduceOutputContext<T> outputContext = new ReduceOutputContext<>(identityValue, reducer);
        return new ReducePipeline<>(inputContext, transform, outputContext);
    }

    public ForEachPipeline forEach(Consumer<Record> consumer) {
        OutputContext<Nothing> outputContext = new ForEachOutputContext(consumer);
        return new ForEachPipeline(inputContext, transform, outputContext);
    }

    public <T> ProcessPipeline<T> process(Function<Record, T> processor) {
        OutputContext<List<T>> outputContext = new ProcessOutputContext<>(processor);
        return new ProcessPipeline<>(inputContext, transform, outputContext);
    }

}

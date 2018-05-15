package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class OperationPipelineBuilder {
    private final InputContext inputContext;
    private final List<Operation> steps;

    public OperationPipelineBuilder(InputContext inputContext, List<Operation> steps) {
        this.inputContext = inputContext;
        this.steps = steps;
    }

    // Factory methods
    public static OperationPipelineBuilder mapFirst(String id, String description, InputContext inputContext, Function<Record, Record> mapper) {
        MapOperation operation = new MapOperation(id, description, mapper);
        List<Operation> steps = new LinkedList<>();
        steps.add(operation);
        return new OperationPipelineBuilder(inputContext, steps);
    }

    public static OperationPipelineBuilder filterFirst(String id, String description, InputContext inputContext, Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(id, description, predicate);
        List<Operation> steps = new LinkedList<>();
        steps.add(operation);
        return new OperationPipelineBuilder(inputContext, steps);
    }

    // Map
    public OperationPipelineBuilder map(Function<Record, Record> mapper) {
        return map(null, null, mapper);
    }

    public OperationPipelineBuilder map(String id, Function<Record, Record> mapper) {
        return map(id, null, mapper);
    }

    public OperationPipelineBuilder map(String id, String description, Function<Record, Record> mapper) {
        MapOperation operation = new MapOperation(id, description, mapper);
        steps.add(operation);
        return new OperationPipelineBuilder(inputContext, steps);
    }

    // Filter
    public OperationPipelineBuilder filter(Predicate<Record> predicate) {
        return filter(null, null, predicate);
    }

    public OperationPipelineBuilder filter(String id, Predicate<Record> predicate) {
        return filter(id, null, predicate);
    }

    public OperationPipelineBuilder filter(String id, String description, Predicate<Record> predicate) {
        FilterOperation operation = new FilterOperation(id, description, predicate);
        steps.add(operation);
        return new OperationPipelineBuilder(inputContext, steps);
    }

    // Next
    public FlatOutputFilePipelineBuilder toFile(String path, Format format) {
        return new FlatOutputFilePipelineBuilder(inputContext, buildTransform(), path, format);
    }

    public ListPipeline toList() {
        OutputContext<List<Record>> outputContext = new ListOutputContext();
        return new ListPipeline(inputContext, buildTransform(), outputContext);
    }

    public <G> GroupPipeline<G> groupBy(Function<Record, G> groupBy) {
        OutputContext<Map<G, List<Record>>> outputContext = new GroupOutputContext<>(groupBy);
        return new GroupPipeline<>(inputContext, buildTransform(), outputContext);
    }

    public <T> ReducePipeline<T> reduce(T identityValue, BiFunction<T, Record, T> reducer) {
        ReduceOutputContext<T> outputContext = new ReduceOutputContext<>(identityValue, reducer);
        return new ReducePipeline<>(inputContext, buildTransform(), outputContext);
    }

    public ForEachPipeline forEach(Consumer<Record> consumer) {
        OutputContext<Nothing> outputContext = new ForEachOutputContext(consumer);
        return new ForEachPipeline(inputContext, buildTransform(), outputContext);
    }

    public <T> ProcessPipeline<T> process(Function<Record, T> processor) {
        OutputContext<List<T>> outputContext = new ProcessOutputContext<>(processor);
        return new ProcessPipeline<>(inputContext, buildTransform(), outputContext);
    }

    private GenericTransform buildTransform() {
        return new GenericTransform(steps);
    }
}

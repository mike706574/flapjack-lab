package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;

public class TransformPipelineBuilder {
    private final FlatInputFile flatInputFile;
    private final Transform transform;

    public TransformPipelineBuilder(FlatInputFile flatInputFile, Transform transform) {
        this.flatInputFile = flatInputFile;
        this.transform = transform;
    }

    public FlatOutputFilePipelineBuilder toFile(String path, Format format) {
        return new FlatOutputFilePipelineBuilder(flatInputFile, transform, path, format, false);
    }

    public ListPipeline toList() {
        OutputContext<List<Record>> outputContext = new ListOutputContext();
        return new ListPipeline(flatInputFile, transform, outputContext);
    }

    public <G> GroupPipeline<G> groupBy(Function<Record, G> groupBy) {
        OutputContext<Map<G, List<Record>>> outputContext = new GroupOutputContext<>(groupBy);
        return new GroupPipeline<>(flatInputFile, transform, outputContext);
    }

    public <T> ReducePipeline<T> reduce(T identityValue, BiFunction<T, Record, T> reducer) {
        ReduceOutputContext<T> outputContext = new ReduceOutputContext<>(identityValue, reducer);
        return new ReducePipeline<>(flatInputFile, transform, outputContext);
    }

    public ForEachPipeline forEach(Consumer<Record> consumer) {
        OutputContext<Nothing> outputContext = new ForEachOutputContext(consumer);
        return new ForEachPipeline(flatInputFile, transform, outputContext);
    }

    public <T> ProcessPipeline<T> process(Function<Record, T> processor) {
        OutputContext<List<T>> outputContext = new ProcessOutputContext<>(processor);
        return new ProcessPipeline<>(flatInputFile, transform, outputContext);
    }

}

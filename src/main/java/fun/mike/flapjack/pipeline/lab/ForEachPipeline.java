package fun.mike.flapjack.pipeline.lab;

public class ForEachPipeline extends GenericPipeline<Nothing> {
    public ForEachPipeline(FlatInputFile flatInputFile, GenericTransform transform, OutputContext<Nothing> outputContext) {
        super(flatInputFile, transform, outputContext);
    }
}

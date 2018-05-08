package fun.mike.flapjack.pipeline.lab;

public class ForEachPipeline extends GenericPipeline<Nothing> {
    public ForEachPipeline(InputContext inputContext, Transform transform, OutputContext<Nothing> outputContext) {
        super(inputContext, transform, outputContext);
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.Set;

import fun.mike.record.alpha.Record;

public class SetPipeline extends GenericPipeline<Set<Record>> {
    public SetPipeline(InputContext inputContext, Transform transform, OutputContext<Set<Record>> outputContext) {
        super(inputContext, transform, outputContext);
    }

    public SetPipelineResult run() {
        return new SetPipelineResult(execute());
    }
}

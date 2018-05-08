package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;

import fun.mike.record.alpha.Record;

public class GroupPipeline<G> extends GenericPipeline<Map<G, List<Record>>> {
    public GroupPipeline(InputContext inputContext, Transform transform, OutputContext<Map<G, List<Record>>> outputContext) {
        super(inputContext, transform, outputContext);
    }

    public GroupPipelineResult<G> run() {
        return new GroupPipelineResult<>(execute());
    }
}

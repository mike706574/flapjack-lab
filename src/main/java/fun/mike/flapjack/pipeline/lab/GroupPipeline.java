package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;

import fun.mike.record.alpha.Record;

public class GroupPipeline<G> extends GenericPipeline<Map<G, List<Record>>> {
    public GroupPipeline(FlatInputFile flatInputFile, GenericTransform transform, OutputContext<Map<G, List<Record>>> outputContext) {
        super(flatInputFile, transform, outputContext);
    }

    public GroupPipelineResult<G> run() {
        return new GroupPipelineResult<>(execute());
    }
}

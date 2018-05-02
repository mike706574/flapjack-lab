package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupPipeline<G> extends GenericPipeline<Map<G, List<Record>>> {
    public GroupPipeline(FlatInputFile flatInputFile, List<Operation> operations, OutputContext<Map<G, List<Record>>> outputContext) {
        super(flatInputFile, operations, outputContext);
    }

    public GroupPipelineResult<G> run() {
        return new GroupPipelineResult<>(execute());
    }
}

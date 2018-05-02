package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupingPipeline<G> implements Pipeline<Map<G, List<Record>>> {
    private static final Logger log = LoggerFactory.getLogger(SequentialPipeline.class);
    private final FlatInputFile flatInputFile;
    private final List<Operation> operations;
    private final Function<Record, G> groupBy;

    public GroupingPipeline(FlatInputFile flatInputFile,
                            List<Operation> operations,
                            Function<Record, G> groupBy) {
        this.flatInputFile = flatInputFile;
        this.operations = operations;
        this.groupBy = groupBy;
    }

    @Override
    public PipelineResult<Map<G, List<Record>>> execute() {
        return run();
    }

    public GroupingPipelineResult<G> run() {
        OutputContext<Map<G, List<Record>>> outputContext = new GroupingOutputContext<>(groupBy);
        PipelineResult<Map<G, List<Record>>> result = PipelineInternals.runWithOutputChannel(flatInputFile,
                                                                                             operations,
                                                                                             outputContext);
        return GroupingPipelineResult.build(result);
    }
}

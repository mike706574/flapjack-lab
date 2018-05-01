package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupingPipeline<G> implements Pipeline<Map<G, List<Record>>> {
    private static final Logger log = LoggerFactory.getLogger(SequentialPipeline.class);
    private final InputFile inputFile;
    private final List<Operation> operations;
    private final Function<Record, G> groupBy;

    public GroupingPipeline(InputFile inputFile,
                            List<Operation> operations,
                            Function<Record, G> groupBy) {
        this.inputFile = inputFile;
        this.operations = operations;
        this.groupBy = groupBy;
    }

    @Override
    public PipelineResult<Map<G, List<Record>>> execute() {
        return run();
    }

    public GroupingPipelineResult<G> run() {
        GroupingOutputChannel<G> outputChannel = new GroupingOutputChannel<>(groupBy);
        PipelineResult<?> result = PipelineInternals.runWithOutputChannel(inputFile,
                                                                          operations,
                                                                          outputChannel,
                                                                          false);
        return GroupingPipelineResult.build(result.withValue(outputChannel.getValues()));
    }
}

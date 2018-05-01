package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.function.Function;

import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupingPipeline<G> {
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

    private static final class Result {
        public int inputCount;
        public int outputCount;

        public Result() {
            this.inputCount = 0;
            this.outputCount = 0;
        }
    }

    public GroupingPipelineResult<G> run() {
        log.debug("Running flow.");

        GroupingOutputChannel<G> outputChannel = new GroupingOutputChannel<>(groupBy);
        CommonPipelineResult commonResult = PipelineInternals.runWithOutputChannel(inputFile,
                                                                                   operations,
                                                                                   outputChannel);

        GroupingPipelineResult<G> result = new GroupingPipelineResult<>(outputChannel.getValues(),
                                                                        commonResult);

        if (result.isOk()) {
            log.debug("Pipeline completed with no errors.");
        } else {
            log.debug(String.format("Pipeline completed with %d errors.", result.getErrorCount()));
        }

        return result;
    }
}

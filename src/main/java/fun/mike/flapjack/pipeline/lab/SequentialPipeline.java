package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequentialPipeline {
    private static final Logger log = LoggerFactory.getLogger(SequentialPipeline.class);
    private final InputFile inputFile;
    private final List<Operation> operations;

    public SequentialPipeline(InputFile inputFile, List<Operation> operations) {
        this.inputFile = inputFile;
        this.operations = operations;
    }

    private static final class Result {
        public int inputCount;
        public int outputCount;

        public Result() {
            this.inputCount = 0;
            this.outputCount = 0;
        }
    }

    public SequentialPipelineResult run() {
        log.debug("Running flow.");

        SequentialOutputChannel outputChannel = new SequentialOutputChannel();
        CommonPipelineResult commonResult = PipelineInternals.runWithOutputChannel(inputFile,
                                                                                   operations,
                                                                                   outputChannel);

        SequentialPipelineResult result = new SequentialPipelineResult(outputChannel.getValues(),
                                                                       commonResult);

        if (result.isOk()) {
            log.debug("Pipeline completed with no errors.");
        } else {
            log.debug(String.format("Pipeline completed with %d errors.", result.getErrorCount()));
        }

        return result;
    }
}

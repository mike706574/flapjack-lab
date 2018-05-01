package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Optional;

import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequentialPipeline implements Pipeline<List<Record>> {
    private static final Logger log = LoggerFactory.getLogger(SequentialPipeline.class);
    private final InputFile inputFile;
    private final List<Operation> operations;

    public SequentialPipeline(InputFile inputFile, List<Operation> operations) {
        this.inputFile = inputFile;
        this.operations = operations;
    }

    public SequentialPipelineResult run() {
        OutputChannel outputChannel = NoOpOutputChannel.build();
        PipelineResult<List<Record>> result = PipelineInternals.runWithOutputChannel(inputFile,
                                                                                     operations,
                                                                                     outputChannel,
                                                                                     true)
                .map(Optional::get);
        return SequentialPipelineResult.build(result);
    }

    @Override
    public PipelineResult<List<Record>> execute() {
        return run();
    }
}

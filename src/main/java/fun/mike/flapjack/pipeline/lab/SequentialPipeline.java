package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequentialPipeline implements Pipeline<List<Record>> {
    private static final Logger log = LoggerFactory.getLogger(SequentialPipeline.class);
    private final FlatInputFile flatInputFile;
    private final List<Operation> operations;

    public SequentialPipeline(FlatInputFile flatInputFile, List<Operation> operations) {
        this.flatInputFile = flatInputFile;
        this.operations = operations;
    }

    public SequentialPipelineResult run() {
        SequentialOutputContext outputContext = new SequentialOutputContext();
        PipelineResult<List<Record>> result = PipelineInternals.runWithOutputChannel(flatInputFile,
                                                                                     operations,
                                                                                     outputContext);
        return SequentialPipelineResult.build(result);
    }

    @Override
    public PipelineResult<List<Record>> execute() {
        return run();
    }
}

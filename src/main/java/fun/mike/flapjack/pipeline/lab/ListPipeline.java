package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.record.alpha.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListPipeline extends GenericPipeline<List<Record>> {
    public ListPipeline(FlatInputFile flatInputFile, List<Operation> operations, OutputContext<List<Record>> outputContext) {
        super(flatInputFile, operations, outputContext);
    }

    public ListPipelineResult run() {
        return new ListPipelineResult(execute());
    }
}

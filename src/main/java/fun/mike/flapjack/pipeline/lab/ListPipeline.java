package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.record.alpha.Record;

public class ListPipeline extends GenericPipeline<List<Record>> {
    public ListPipeline(FlatInputFile flatInputFile, GenericTransform transform, OutputContext<List<Record>> outputContext) {
        super(flatInputFile, transform, outputContext);
    }

    public ListPipelineResult run() {
        return new ListPipelineResult(execute());
    }
}

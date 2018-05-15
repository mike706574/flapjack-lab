package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Optional;

import fun.mike.record.alpha.Record;

public class GenericTransform implements Transform {
    private final List<Operation> operations;
    private final List<CompiledOperation> compiledOperations;

    public GenericTransform(List<Operation> operations) {
        this.operations = operations;
        this.compiledOperations = Operations.compile(operations);
    }

    public TransformResult run(Record record) {
        Record outputRecord = record;
        String operationId = null;
        Long operationNumber = 1L;
        for (CompiledOperation compiledOperation : compiledOperations) {
            try {
                Optional<Record> result = compiledOperation.getOperation().run(outputRecord);

                if (!result.isPresent()) {
                    return TransformResult.empty(outputRecord, record, compiledOperation.getInfo());
                }

                outputRecord = result.get();
                operationNumber++;
            } catch (Exception ex) {
                return TransformResult.failure(outputRecord, record, compiledOperation.getInfo(), ex);
            }
        }
        return TransformResult.ok(outputRecord, record);
    }
}

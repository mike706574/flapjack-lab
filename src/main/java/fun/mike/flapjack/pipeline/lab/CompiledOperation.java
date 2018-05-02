package fun.mike.flapjack.pipeline.lab;

public class CompiledOperation {
    private final Operation operation;
    private final OperationInfo info;

    public CompiledOperation(Operation operation, OperationInfo info) {
        this.operation = operation;
        this.info = info;
    }

    public Operation getOperation() {
        return operation;
    }

    public OperationInfo getInfo() {
        return info;
    }
}

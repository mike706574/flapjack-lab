package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.function.BiFunction;

import fun.mike.record.alpha.Record;

public class ReducingPipeline<T> implements Pipeline<T> {
    private final InputFile inputFile;
    private final List<Operation> operations;
    private final T identityValue;
    private final BiFunction<T, Record, T> reducer;

    public ReducingPipeline(InputFile inputFile,
                            List<Operation> operations,
                            T identityValue,
                            BiFunction<T, Record, T> reducer) {
        this.inputFile = inputFile;
        this.operations = operations;
        this.identityValue = identityValue;
        this.reducer = reducer;
    }

    @Override
    public PipelineResult<T> execute() {
        return run();
    }

    public PipelineResult<T> run() {
        ReducingOutputChannel<T> outputChannel = new ReducingOutputChannel<>(identityValue, reducer);
        PipelineResult<?> result = PipelineInternals
                .runWithOutputChannel(inputFile,
                                      operations,
                                      outputChannel,
                                      false);
        return result.withValue(outputChannel.getValue());
    }
}

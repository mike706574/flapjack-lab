package fun.mike.flapjack.pipeline.lab;

import java.util.Optional;
import java.util.function.BiFunction;

import fun.mike.record.alpha.Record;

public class ReduceOutputContext<T> implements OutputContext<T> {
    private final T identityValue;
    private final BiFunction<T, Record, T> reducer;

    public ReduceOutputContext(T identityValue, BiFunction<T, Record, T> reducer) {
        this.identityValue = identityValue;
        this.reducer = reducer;
    }

    @Override
    public OutputChannel<T> buildChannel() {
        return new ReduceOutputChannel<>(identityValue, reducer);
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }

    private final class ReduceOutputChannel<T> implements OutputChannel<T> {
        private final T identityValue;
        private final BiFunction<T, Record, T> reducer;
        private T reducedValue;

        public ReduceOutputChannel(T identityValue, BiFunction<T, Record, T> reducer) {
            this.identityValue = identityValue;
            this.reducer = reducer;
            this.reducedValue = identityValue;
        }

        @Override
        public Optional<PipelineError> put(int number, String line, Record value) {
            try {
                reducedValue = reducer.apply(reducedValue, value);
                return Optional.empty();
            } catch (Exception ex) {
                return Optional.of(OutputPipelineError.build(number, line, value, ex));
            }
        }

        public T getValue() {
            return reducedValue;
        }

        @Override
        public void close() {
        }
    }
}

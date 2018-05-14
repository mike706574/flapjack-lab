package fun.mike.flapjack.pipeline.lab;

public interface OutputContext<T> {
    OutputChannel<T> buildChannel();

    void accept(OutputContextVisitor visitor);
}

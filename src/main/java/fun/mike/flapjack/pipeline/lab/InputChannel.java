package fun.mike.flapjack.pipeline.lab;

public interface InputChannel extends AutoCloseable {
    InputResult take();

    boolean hasMore();

    void close();
}


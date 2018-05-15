package fun.mike.flapjack.pipeline.lab;

import java.util.Optional;

import fun.mike.record.alpha.Record;

public interface OutputChannel<T> extends AutoCloseable {
    Optional<Failure> put(int number, String line, Record value);

    T getValue();

    void close();
}

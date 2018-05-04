package fun.mike.flapjack.pipeline.lab;

import java.util.Collections;
import java.util.List;

import fun.mike.record.alpha.Record;

public interface OutputChannel<T> extends AutoCloseable {
    boolean receive(int number, String line, Record value);

    default List<PipelineError> getErrors() {
        return Collections.emptyList();
    }

    T getValue();

    void close();
}

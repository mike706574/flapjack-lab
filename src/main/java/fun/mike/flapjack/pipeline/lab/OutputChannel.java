package fun.mike.flapjack.pipeline.lab;

import java.util.Collections;
import java.util.List;

import fun.mike.record.alpha.Record;

public interface OutputChannel extends AutoCloseable {
    boolean receive(Long number, String line, Record value);
    default List<PipelineError> getErrors() {
        return Collections.emptyList();
    }

    void close();
}

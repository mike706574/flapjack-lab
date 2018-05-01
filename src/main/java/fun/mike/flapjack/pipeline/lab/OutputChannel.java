package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public interface OutputChannel extends AutoCloseable {
    boolean receive(Record value);
    void close();
}

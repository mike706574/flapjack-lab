package fun.mike.flapjack.pipeline.lab;

import java.util.Optional;

import fun.mike.record.alpha.Record;

public interface Operation {
    String getId();

    Optional<Record> run(Record value);
}

package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public interface PipelineError {
    int getNumber();

    String getLine();

    Record getRecord();

    void accept(PipelineErrorVisitor visitor);
}

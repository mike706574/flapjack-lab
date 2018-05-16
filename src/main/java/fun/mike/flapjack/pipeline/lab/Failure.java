package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public interface Failure {
    int getNumber();

    String getLine();

    Record getRecord();

    String explain();

    void accept(FailureVisitor visitor);
}

package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.record.alpha.Record;

public class SequentialOutputContext implements OutputContext<List<Record>> {
    @Override
    public OutputChannel<List<Record>> buildChannel() {
        return new SequentialOutputChannel();
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }
}

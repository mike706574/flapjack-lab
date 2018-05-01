package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;

public class NoOpOutputChannel implements OutputChannel {
    public static NoOpOutputChannel build() {
        return new NoOpOutputChannel();
    }

    @Override
    public boolean receive(Long number, String line, Record value) {
        return true;
    }

    @Override
    public void close() {
    }
}

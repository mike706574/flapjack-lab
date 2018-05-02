package fun.mike.flapjack.pipeline.lab;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import fun.mike.record.alpha.Record;

public class GroupOutputContext<G> implements OutputContext<Map<G, List<Record>>> {
    private final Function<Record, G> groupBy;

    public GroupOutputContext(Function<Record, G> groupBy) {
        this.groupBy = groupBy;
    }

    @Override
    public OutputChannel<Map<G, List<Record>>> buildChannel() {
        return new GroupOutputChannel<>(groupBy);
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }
}

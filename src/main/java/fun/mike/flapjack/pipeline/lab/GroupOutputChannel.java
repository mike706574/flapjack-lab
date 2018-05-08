package fun.mike.flapjack.pipeline.lab;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import fun.mike.record.alpha.Record;

public class GroupOutputChannel<G> implements OutputChannel<Map<G, List<Record>>> {
    private final Function<Record, G> groupBy;
    private final Map<G, List<Record>> values;

    public GroupOutputChannel(Function<Record, G> groupBy) {
        this.values = new HashMap<>();
        this.groupBy = groupBy;
    }

    @Override
    public Optional<PipelineError> put(int number, String line, Record value) {
        try {
            G group = groupBy.apply(value);
            if (values.containsKey(group)) {
                values.get(group).add(value);
            } else {
                List<Record> groupValues = new LinkedList<>();
                groupValues.add(value);
                values.put(group, groupValues);
            }
            return Optional.empty();
        } catch (Exception ex) {
            return Optional.of(OutputPipelineError.build(number, line, value, ex));
        }
    }

    @Override
    public Map<G, List<Record>> getValue() {
        return values;
    }

    @Override
    public void close() {
    }
}

package fun.mike.flapjack.pipeline.lab;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import fun.mike.record.alpha.Record;

public class GroupingOutputChannel<G> implements OutputChannel {
    private final Function<Record, G> groupBy;
    private final Map<G, List<Record>> values;
    private final List<PipelineError> errors;

    public GroupingOutputChannel(Function<Record, G> groupBy) {
        this.values = new HashMap<>();
        this.groupBy = groupBy;
        this.errors = new LinkedList<>();
    }

    @Override
    public boolean receive(Long number, String line, Record value) {
        try {
            G group = groupBy.apply(value);
            if (values.containsKey(group)) {
                values.get(group).add(value);
            } else {
                List<Record> groupValues = new LinkedList<>();
                groupValues.add(value);
                values.put(group, groupValues);
            }
            return true;
        } catch (Exception ex) {
            errors.add(OutputPipelineError.build(number, line, value, ex));
            return false;
        }
    }

    public List<PipelineError> getErrors() {
        return errors;
    }

    public Map<G, List<Record>> getValues() {
        return new Group<>(values);
    }

    @Override
    public void close() {
    }
}

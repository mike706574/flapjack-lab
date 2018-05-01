package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fun.mike.record.alpha.Record;

public class Group<G> extends LinkedHashMap<G, List<Record>> {
    public Group(Map<? extends G, ? extends List<Record>> m) {
        super(m);
    }
}

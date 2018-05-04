package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupPipelineTest {
    private static final String base = "src/test/resources/pipeline/";

    private static final Format inputFormat =
            DelimitedFormat.unframed("delimited-animals",
                                     "Delimited animals format.",
                                     ',',
                                     Arrays.asList(Column.string("name"),
                                                   Column.integer("legs"),
                                                   Column.string("size")));

    @Test
    public void success() {
        String inputPath = base + "animals.csv";
        String outputPath = base + "animals.dat";

        GroupPipeline<String> pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .groupBy(x -> x.getString("size"));

        PipelineResult<Map<String, List<Record>>> result = pipeline.run();

        assertTrue(result.isOk());
        assertEquals(6, result.getInputCount());
        assertEquals(6, result.getOutputCount());
        assertEquals(0, result.getErrorCount());

        Map<String, List<Record>> values = result.getValue();

        assertEquals(4, values.size());

        List<Record> smallAnimals = values.get("SMALL");
        assertEquals(1, smallAnimals.size());
        assertEquals(Record.of("name", "snake",
                               "legs", 0,
                               "size", "SMALL"),
                     smallAnimals.get(0));

        List<Record> mediumAnimals = values.get("MEDIUM");
        assertEquals(3, mediumAnimals.size());
        assertEquals(Record.of("name", "dog",
                               "legs", 4,
                               "size", "MEDIUM"),
                     mediumAnimals.get(0));
        assertEquals(Record.of("name", "fox",
                               "legs", 4,
                               "size", "MEDIUM"),
                     mediumAnimals.get(1));
        assertEquals(Record.of("name", "ostrich",
                               "legs", 2,
                               "size", "MEDIUM"),
                     mediumAnimals.get(2));

        List<Record> hugeAnimals = values.get("HUGE");
        assertEquals(1, hugeAnimals.size());
        assertEquals(Record.of("name", "elephant",
                               "legs", 4,
                               "size", "HUGE"),
                     hugeAnimals.get(0));

        List<Record> giganticAnimals = values.get("GIGANTIC");
        assertEquals(1, giganticAnimals.size());
        assertEquals(Record.of("name", "whale",
                               "legs", 0,
                               "size", "GIGANTIC"),
                     giganticAnimals.get(0));
    }

    @Test
    public void failure() {
        String inputPath = base + "bad-animals.csv";
        String outputPath = base + "animals.dat";

        GroupPipeline<String> pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .groupBy(x -> x.getString("size"));

        PipelineResult<Map<String, List<Record>>> result = pipeline.run();

        assertFalse(result.isOk());

        assertEquals(6, result.getInputCount());
        assertEquals(4, result.getOutputCount());
        assertEquals(2, result.getErrorCount());
        assertEquals(2, result.getErrorCount());

        Map<String, List<Record>> values = result.getValue();

        assertEquals(3, values.size());

        List<Record> smallAnimals = values.get("SMALL");
        assertEquals(1, smallAnimals.size());
        assertEquals(Record.of("name", "snake",
                               "legs", 0,
                               "size", "SMALL"),
                     smallAnimals.get(0));

        List<Record> mediumAnimals = values.get("MEDIUM");
        assertEquals(2, mediumAnimals.size());
        assertEquals(Record.of("name", "dog",
                               "legs", 4,
                               "size", "MEDIUM"),
                     mediumAnimals.get(0));
        assertEquals(Record.of("name", "fox",
                               "legs", 4,
                               "size", "MEDIUM"),
                     mediumAnimals.get(1));

        List<Record> giganticAnimals = values.get("GIGANTIC");
        assertEquals(1, giganticAnimals.size());
        assertEquals(Record.of("name", "whale",
                               "legs", 0,
                               "size", "GIGANTIC"),
                     giganticAnimals.get(0));
    }
}

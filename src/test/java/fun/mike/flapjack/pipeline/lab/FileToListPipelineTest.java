package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;
import java.util.List;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileToListPipelineTest {
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

        ListPipeline pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .includeLineAs("message")
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .toList();

        PipelineResult<List<Record>> result = pipeline.run();

        assertTrue(result.isOk());
        assertEquals(6, result.getInputCount());
        assertEquals(3, result.getOutputCount());

        List<Record> values = result.orElseThrow();

        assertEquals(3, values.size());

        assertEquals(Record.of("name", "dog",
                               "legs", 4,
                               "size", "MEDIUM",
                               "message", "dog,4,medium"),
                     values.get(0));

        assertEquals(Record.of("name", "fox",
                               "legs", 4,
                               "size", "MEDIUM",
                               "message", "fox,4,medium"),
                     values.get(1));

        assertEquals(Record.of("name", "ostrich",
                               "legs", 2,
                               "size", "MEDIUM",
                               "message", "ostrich,2,medium"),
                     values.get(2));
    }

    @Test
    public void failure() {
        String inputPath = base + "bad-animals.csv";
        String outputPath = base + "animals.dat";

        ListPipeline pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .toList();

        PipelineResult<List<Record>> result = pipeline.run();

        assertFalse(result.isOk());

        assertEquals(6, result.getInputCount());
        assertEquals(2, result.getOutputCount());
        assertEquals(2, result.getFailureCount());
        assertEquals(2, result.getFailureCount());

        List<Record> values = result.getValue();
        assertEquals(2, values.size());

        assertEquals(Record.of("name", "dog",
                               "legs", 4,
                               "size", "MEDIUM"),
                     values.get(0));

        assertEquals(Record.of("name", "fox",
                               "legs", 4,
                               "size", "MEDIUM"),
                     values.get(1));
    }
}

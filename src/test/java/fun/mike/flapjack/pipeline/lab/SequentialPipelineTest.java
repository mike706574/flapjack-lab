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

public class SequentialPipelineTest {
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

        SequentialPipeline pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .toSequence();

        SequentialPipelineResult result = pipeline.run();


        assertTrue(result.isOk());
        assertEquals(new Long(6), result.getInputCount());
        assertEquals(new Long(3), result.getOutputCount());
        assertTrue(result.getParseErrors().isEmpty());
        assertTrue(result.getTransformErrors().isEmpty());

        List<Record> values = result.getValues();

        assertEquals(3, values.size());

        assertEquals(Record.of("name", "dog",
                               "legs", 4,
                               "size", "MEDIUM"),
                     values.get(0));

        assertEquals(Record.of("name", "fox",
                               "legs", 4,
                               "size", "MEDIUM"),
                     values.get(1));

        assertEquals(Record.of("name", "ostrich",
                               "legs", 2,
                               "size", "MEDIUM"),
                     values.get(2));
    }

    @Test
    public void failure() {
        String inputPath = base + "bad-animals.csv";
        String outputPath = base + "animals.dat";

        SequentialPipeline pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .toSequence();

        SequentialPipelineResult result = pipeline.run();

        assertFalse(result.isOk());

        assertEquals(new Long(6), result.getInputCount());
        assertEquals(new Long(2), result.getOutputCount());
        assertEquals(new Long(2), result.getErrorCount());
        assertEquals(2, result.getParseErrors().size());

        List<Record> values = result.getValues();
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

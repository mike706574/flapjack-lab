package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Format;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReducingPipelineTest {
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

        ReducingPipeline<Map<String, Integer>> pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .map(record -> record.updateString("size", String::toUpperCase))
                .toReduction(new HashMap<>(),
                             (tally, record) -> {
                                 String size = record.getString("size");
                                 Integer count = tally.getOrDefault(size, 0);
                                 tally.put(size, count + 1);
                                 return tally;
                             });

        PipelineResult<Map<String, Integer>> result = pipeline.run();

        assertTrue(result.isOk());
        assertEquals(new Long(6), result.getInputCount());
        assertEquals(new Long(6), result.getOutputCount());
        assertEquals(new Long(0), result.getErrorCount());

        Map<String, Integer> tally = result.getValue();

        assertEquals(4, tally.size());

        assertEquals(new Integer(3), tally.get("MEDIUM"));
        assertEquals(new Integer(1), tally.get("HUGE"));
        assertEquals(new Integer(1), tally.get("GIGANTIC"));
        assertEquals(new Integer(1), tally.get("SMALL"));
    }

    @Test
    public void failure() {
        String inputPath = base + "bad-animals.csv";
        String outputPath = base + "animals.dat";

        ReducingPipeline<Map<String, Integer>> pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .toReduction(new HashMap<>(),
                             (tally, record) -> {
                                 String size = record.getString("size");
                                 Integer count = tally.getOrDefault(size, 0);
                                 tally.put(size, count + 1);
                                 return tally;
                             });

        PipelineResult<Map<String, Integer>> result = pipeline.run();

        assertFalse(result.isOk());

        assertEquals(new Long(6), result.getInputCount());
        assertEquals(new Long(4), result.getOutputCount());
        assertEquals(new Long(2), result.getErrorCount());
        assertEquals(new Long(2), result.getErrorCount());

        Map<String, Integer> tally = result.getValue();

        assertEquals(3, tally.size());
        assertEquals(new Integer(2), tally.get("MEDIUM"));
        assertEquals(new Integer(1), tally.get("GIGANTIC"));
        assertEquals(new Integer(1), tally.get("SMALL"));
    }
}

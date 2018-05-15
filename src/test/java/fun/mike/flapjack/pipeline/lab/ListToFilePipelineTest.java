package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;
import java.util.List;

import fun.mike.flapjack.alpha.Field;
import fun.mike.flapjack.alpha.FixedWidthFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.io.alpha.IO;
import fun.mike.record.alpha.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListToFilePipelineTest {
    private static final String base = "src/test/resources/pipeline/";

    private static final Format outputFormat =
            new FixedWidthFormat("delimited-animals",
                                 "Delimited animals format.",
                                 Arrays.asList(Field.string("name", 10),
                                               Field.string("size", 10)));

    @Before
    public void setUp() {
        IO.nuke(base + "animals.dat");
    }

    @After
    public void tearDown() {
        IO.nuke(base + "animals.dat");
    }

    @Test
    public void success() {
        String inputPath = base + "animals.csv";
        String outputPath = base + "animals.dat";

        List<Record> records = Arrays.asList(Record.of("name", "dog",
                                                       "size", "medium"),
                                             Record.of("name", "elephant",
                                                       "size", "huge"),
                                             Record.of("name", "fox",
                                                       "size", "medium"),
                                             Record.of("name", "ostrich",
                                                       "size", "medium"),
                                             Record.of("name", "whale",
                                                       "size", "gigantic"),
                                             Record.of("name", "snake",
                                                       "size", "small"));

        FlatFilePipeline pipeline = Pipeline.fromList(records)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .toFile(outputPath, outputFormat)
                .build();

        PipelineResult<Nothing> result = pipeline.run();

        assertTrue(result.isOk());
        assertEquals(6, result.getInputCount());
        assertEquals(3, result.getOutputCount());
        assertEquals(0, result.getFailureCount());
        assertEquals(0, result.getFailures().size());
        assertTrue(result.getFailures().isEmpty());

        assertEquals(IO.slurp(base + "expected-animals.dat"),
                     IO.slurp(base + "animals.dat"));
    }
}

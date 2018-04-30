package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Field;
import fun.mike.flapjack.alpha.FixedWidthFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.io.alpha.IO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PipelineTest {
    private static final String base = "src/test/resources/pipeline/";

    private static final Format inputFormat =
            DelimitedFormat.unframed("delimited-animals",
                                     "Delimited animals format.",
                                     ',',
                                     Arrays.asList(Column.string("name"),
                                                   Column.integer("legs"),
                                                   Column.string("size")));

    private static final Format outputFormat =
            new FixedWidthFormat("delimited-animals",
                                 "Delimited animals format.",
                                 Arrays.asList(Field.string("name", 10),
                                               Field.string("size", 10)));

    @Test
    public void success() {
        String inputPath = base + "animals.csv";
        String outputPath = base + "animals.dat";

        Pipeline pipeline = Pipeline.from(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .to(outputPath, outputFormat)
                .build();

        PipelineResult result = pipeline.run();

        System.out.println(result.summarize());

        assertTrue(result.isOk());
        assertEquals(new Long(6), result.getInputCount());
        assertEquals(new Long(3), result.getOutputCount());
        assertTrue(result.getParseErrors().isEmpty());
        assertTrue(result.getTransformErrors().isEmpty());
        assertTrue(result.getSerializationErrors().isEmpty());

        assertEquals(IO.slurp(base + "expected-animals.dat"),
                     IO.slurp(base + "animals.dat"));
    }

    @Test
    public void headerAndFooter() {

    }

    @Test
    public void failure() {
        String inputPath = base + "bad-animals.csv";
        String outputPath = base + "animals.dat";

        Pipeline pipeline = Pipeline.from(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .to(outputPath, outputFormat)
                .build();

        PipelineResult result = pipeline.run();

        System.out.println(result.summarize());

        assertFalse(result.isOk());
    }

    private void withFile(String path, String content, Runnable f) {
        try {
            IO.spit(path, content);
            f.run();
        }
        finally {
            IO.nuke(path);
        }
    }
}

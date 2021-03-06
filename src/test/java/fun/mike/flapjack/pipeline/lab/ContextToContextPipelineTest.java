package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;
import java.util.List;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Field;
import fun.mike.flapjack.alpha.FixedWidthFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.io.alpha.IO;
import fun.mike.record.alpha.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContextToContextPipelineTest {
    private static final String base = "src/test/resources/pipeline/";

    private static final Format inputFormat =
            DelimitedFormat.unframed("delimited-animals",
                                     "Delimited animals format.",
                                     ',',
                                     Arrays.asList(Column.string("name"),
                                                   Column.integer("legs"),
                                                   Column.string("size")));

    private static final Format anotherFormat =
            DelimitedFormat.unframed("delimited-animals-2",
                                     "Another delimited animals format.",
                                     ',',
                                     Arrays.asList(Column.string("name"),
                                                   Column.string("size"),
                                                   Column.integer("legs")));
    private static final Format outputFormat =
            new FixedWidthFormat("delimited-animals",
                                 "Delimited animals format.",
                                 Arrays.asList(Field.string("name", 10),
                                               Field.string("size", 10)));

    @Before
    public void setUp() {
        IO.nuke(base + "animals.dat");
        IO.nuke(base + "animals-with-header.csv");
        IO.nuke(base + "bad-animals.dat");
    }

    @After
    public void tearDown() {
        IO.nuke(base + "animals.dat");
        IO.nuke(base + "animals-with-header.csv");
        IO.nuke(base + "bad-animals.dat");
    }

    @Test
    public void success() {
        String inputPath = base + "animals.csv";
        String outputPath = base + "animals.dat";

        InputContext inputContext = new FlatFileInputContext(inputPath, inputFormat, null, false, false);

        Transform transform = Transform
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .build();

        OutputContext<Nothing> outputContext = new FlatFileOutputContext(outputPath, outputFormat, false);

        Pipeline<Nothing> pipeline = Pipeline.fromContext(inputContext)
                .transform(transform)
                .toContext(outputContext);


        PipelineResult<Nothing> result = pipeline.execute();

        assertTrue(result.isOk());
        assertEquals(6, result.getInputCount());
        assertEquals(3, result.getOutputCount());
        assertEquals(0, result.getFailureCount());
        assertEquals(0, result.getFailures().size());
        assertTrue(result.getFailures().isEmpty());

        assertEquals(IO.slurp(base + "expected-animals.dat"),
                     IO.slurp(base + "animals.dat"));
    }


    @Test
    public void wat() {
        String inputPath = base + "bad-animals.csv";
        String outputPath = base + "bad-animals.dat";

        ListResult inputResult = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .map(x -> x.dissoc("size"))
                .toList()
                .run();

        List<Record> records = inputResult.getValue();

        System.out.println(PipelineExplainer.explainResult(inputResult));
        System.out.println("Line");
        System.out.println(records.get(0).getMetadata());
        System.out.println(records.get(0).getMetadataProperty("line"));

        PipelineResult<Nothing> outputResult = Pipeline.fromList(records)
                .toFile(outputPath, outputFormat)
                .run();

        System.out.println(PipelineExplainer.explainResult(outputResult));
    }

    @Test
    public void successWithHeader() {
        String inputPath = base + "animals.csv";
        String outputPath = base + "animals-with-header.csv";

        PipelineResult<Nothing> result = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .toFile(outputPath, anotherFormat)
                .run();

        assertTrue(result.isOk());
        assertEquals(6, result.getInputCount());
        assertEquals(3, result.getOutputCount());
        assertEquals(0, result.getFailureCount());
        assertEquals(0, result.getFailures().size());
        assertTrue(result.getFailures().isEmpty());

        assertEquals(IO.slurp(base + "expected-animals-with-header.csv"),
                     IO.slurp(base + "animals-with-header.csv"));
    }

    @Test
    public void failure() {
        String inputPath = base + "bad-animals.csv";
        String outputPath = base + "bad-animals.dat";

        FlatFilePipeline pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .toFile(outputPath, outputFormat)
                .build();

        PipelineResult<Nothing> result = pipeline.run();

        assertFalse(result.isOk());
        assertEquals(6, result.getInputCount());
        assertEquals(2, result.getOutputCount());
        assertEquals(2, result.getFailureCount());

        List<Failure> failures = result.getFailures();

        assertEquals(2, failures.size());

        Failure firstFailure = failures.get(0);
        assertEquals(2, firstFailure.getNumber());

        Failure secondFailure = failures.get(1);
        assertEquals(4, secondFailure.getNumber());

        assertEquals(IO.slurp(base + "expected-bad-animals.dat"),
                     IO.slurp(base + "bad-animals.dat"));
    }
}

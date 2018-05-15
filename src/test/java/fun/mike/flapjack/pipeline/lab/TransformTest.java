package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;
import java.util.List;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransformTest {
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
        Record inputRecord = Record.of("name", "dog",
                                       "legs", "4",
                                       "size", "medium");


        Transform transform = Transform.map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .build();

        TransformResult result = transform.run(inputRecord);

        assertTrue(result.isOk());

        assertEquals(Record.of("name", "dog",
                               "legs", "4",
                               "size", "MEDIUM"),
                     result.getRecord());

        assertEquals(inputRecord,
                     result.getOriginalRecord());
    }

    @Test
    public void empty() {
        Record inputRecord = Record.of("name", "dog",
                                       "legs", "4",
                                       "size", "medium");

        Transform transform = Transform.map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("LARGE"))
                .build();

        TransformResult result = transform.run(inputRecord);

        assertTrue(result.isEmpty());

        assertEquals(inputRecord,
                     result.getOriginalRecord());
    }

    @Test
    public void exception() {
        Record inputRecord = Record.of("name", "dog",
                                       "legs", "4",
                                       "size", "medium");

        Transform transform = Transform.map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> {
                    throw new RuntimeException("Failure!");
                })
                .build();

        TransformResult result = transform.run(inputRecord);

        assertTrue(result.hasFailure());

        assertEquals(inputRecord, result.getOriginalRecord());

        Exception exception = result.getException();

        assertTrue(exception instanceof RuntimeException);
        assertEquals("Failure!", exception.getMessage());

        OperationInfo operation = result.getOperationInfo();

        assertEquals("filter-2", operation.getId());
        assertEquals(2, operation.getNumber());
    }

    @Test
    public void pipeline() {
        String inputPath = base + "animals.csv";
        String outputPath = base + "animals.dat";

        Transform transform = Transform.map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .build();

        ListPipeline pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .transform(transform)
                .toList();

        PipelineResult<List<Record>> result = pipeline.run();

        assertTrue(result.isOk());
        assertEquals(6, result.getInputCount());
        assertEquals(3, result.getOutputCount());

        List<Record> values = result.orElseThrow();

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
}

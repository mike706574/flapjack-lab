package fun.mike.flapjack.pipeline.lab;

import fun.mike.record.alpha.Record;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransformTest {
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
                        throw new RuntimeException("Error!");
                    })
                .build();

        TransformResult result = transform.run(inputRecord);

        assertTrue(result.hasError());

        assertEquals(inputRecord, result.getOriginalRecord());

        Exception exception = result.getException();

        assertTrue(exception instanceof RuntimeException);
        assertEquals("Error!", exception.getMessage());

        OperationInfo operation = result.getOperationInfo();

        assertEquals("filter-2", operation.getId());
        assertEquals(new Long(2), operation.getNumber());
    }
}

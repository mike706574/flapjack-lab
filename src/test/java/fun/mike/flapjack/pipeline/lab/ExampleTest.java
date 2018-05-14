package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;
import java.util.List;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.record.alpha.Record;
import org.junit.Test;

public class ExampleTest {
    @Test
    public void example() {
        // Define an input format
        Format inputFormat =
                DelimitedFormat.unframed("animals",
                                         "A bunch of animals.",
                                         ',',
                                         Arrays.asList(Column.string("name"),
                                                       Column.integer("legs"),
                                                       Column.string("size")));

        // Build a pipeline
        ListResult result =
                Pipeline.fromFile("src/test/resources/pipeline/animals.csv", inputFormat)
                        .map(x -> x.updateString("size", String::toUpperCase))
                        .filter(x -> x.getString("size").equals("MEDIUM"))
                        .toList()
                        .run();

        System.out.println(result);

        System.out.println(PipelineExplainer.explainResult(result));
        // Check for errors
        result.isOk();
        // => true

        result.getErrorCount();
        // => 0

        // See how many animals went in
        result.getInputCount();
        // => 6

        // See how many medium-sized animals came out
        result.getOutputCount();
        // => 3

        // Get the animals
        List<Record> animals = result.getValue();

        System.out.println(animals);

        // See the first animal
        System.out.println(animals.get(0));
    }
}

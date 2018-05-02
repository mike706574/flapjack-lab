package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Field;
import fun.mike.flapjack.alpha.FixedWidthFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.MissingValueProblem;
import org.junit.Test;

public class PipelineExplainerTest {
    @Test
    public void foo() {
        PipelineError parseError =
                ParsePipelineError.of(1L,
                                      "ABCDEFG",
                                      null,
                                      Arrays.asList(new MissingValueProblem("a", "string")));

        PipelineError serializationError =
                SerializationPipelineError.of(2L,
                                              "ABCDEFG",
                                              null,
                                              Arrays.asList(new MissingValueProblem("b", "integer")));

        PipelineError transformError =
                TransformPipelineError.of(3L,
                                          "BEWFWF",
                                          null,
                                          new OperationInfo(1L, "map-vals", "Mapping values", "map"),
                                          new RuntimeException("coo coo"));


        Format inputFormat = DelimitedFormat.unframed("delimited-animals",
                                         "Delimited animals format.",
                                         ',',
                                         Arrays.asList(Column.string("name"),
                                                       Column.integer("legs"),
                                                       Column.string("size")));

        Format outputFormat =
                new FixedWidthFormat("delimited-animals",
                                     "Delimited animals format.",
                                     Arrays.asList(Field.string("name", 10),
                                                   Field.string("size", 10)));

        FlatInputFile inputFile = new FlatInputFile("in.csv", inputFormat, 0, 0);
        OutputContext<Nothing> outputContext = new FlatFileOutputContext("out.dat", outputFormat, false);

        PipelineResult<Nothing> x =
                PipelineResult.of(Nothing.value(),
                                  inputFile,
                                  outputContext,
                                  5L,
                                  2L,
                                  Arrays.asList(parseError, serializationError, transformError));

        System.out.println(PipelineExplainer.explainResult(x));

    }
}

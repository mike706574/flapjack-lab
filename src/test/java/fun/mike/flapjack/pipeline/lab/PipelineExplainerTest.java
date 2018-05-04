package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;
import java.util.Collections;

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
                ParsePipelineError.of(1,
                                      "ABCDEFG",
                                      null,
                                      Collections.singletonList(new MissingValueProblem("a", "string")));

        PipelineError serializationError =
                SerializationPipelineError.of(2,
                                              "ABCDEFG",
                                              null,
                                              Collections.singletonList(new MissingValueProblem("b", "integer")));

        PipelineError transformError =
                TransformPipelineError.of(3,
                                          "BEWFWF",
                                          null,
                                          new OperationInfo(1, "map-vals", "Mapping values", "map"),
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

        FlatInputFile inputFile = new FlatInputFile("in.csv", inputFormat, 0, 0, true);
        OutputContext<Nothing> outputContext = new FlatFileOutputContext("out.dat", outputFormat, false);

        PipelineResult<Nothing> x =
                PipelineResult.of(Nothing.value(),
                                  inputFile,
                                  outputContext,
                                  5,
                                  2,
                                  Arrays.asList(parseError, serializationError, transformError));

        System.out.println(PipelineExplainer.explainResult(x));

    }
}

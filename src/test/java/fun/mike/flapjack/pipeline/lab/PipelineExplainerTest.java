package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;

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

        PipelineResult<Object> x =
                PipelineResult.of(Nothing.value(),
                                  5L,
                                  2L,
                                  Arrays.asList(parseError, serializationError, transformError));

        System.out.println(PipelineExplainer.explainResult(x));

    }
}

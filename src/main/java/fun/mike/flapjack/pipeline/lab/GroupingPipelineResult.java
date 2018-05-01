package fun.mike.flapjack.pipeline.lab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.record.alpha.Record;

public class GroupingPipelineResult<G> {
    private final Map<G, List<Record>> values;
    private final Long inputCount;
    private final Long outputCount;
    private final List<ParseResult> parseErrors;
    private final List<TransformResult> transformErrors;

    public GroupingPipelineResult(Map<G, List<Record>> values, Long inputCount, Long outputCount, List<ParseResult> parseErrors, List<TransformResult> transformErrors) {
        this.values = values;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.parseErrors = parseErrors;
        this.transformErrors = transformErrors;
    }

    public GroupingPipelineResult(Map<G, List<Record>> values, CommonPipelineResult result) {
        this.values = values;
        this.inputCount = result.getInputCount();
        this.outputCount = result.getOutputCount();
        this.parseErrors = result.getParseErrors();
        this.transformErrors = result.getTransformErrors();
    }

    public boolean isOk() {
        return parseErrors.isEmpty() && transformErrors.isEmpty();
    }

    public Long getInputCount() {
        return inputCount;
    }

    public Long getOutputCount() {
        return outputCount;
    }

    public Map<G, List<Record>> getValues() {
        return new HashMap<>(values);
    }

    public Long getErrorCount() {
        return (long) (parseErrors.size() + transformErrors.size());
    }

    public List<ParseResult> getParseErrors() {
        return parseErrors;
    }

    public List<TransformResult> getTransformErrors() {
        return transformErrors;
    }

    @Override
    public String toString() {
        return "SequentialPipelineResult{" +
                "values=" + values +
                ", inputCount=" + inputCount +
                ", outputCount=" + outputCount +
                ", parseErrors=" + parseErrors +
                ", transformErrors=" + transformErrors +
                '}';
    }
}

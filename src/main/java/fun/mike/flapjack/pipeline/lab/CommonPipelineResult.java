package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.ParseResult;

public class CommonPipelineResult {
    private final Long inputCount;
    private final Long outputCount;
    private final List<ParseResult> parseErrors;
    private final List<TransformResult> transformErrors;

    public CommonPipelineResult(Long inputCount, Long outputCount, List<ParseResult> parseErrors, List<TransformResult> transformErrors) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.parseErrors = parseErrors;
        this.transformErrors = transformErrors;
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
        return "FilePipelineResult{" +
                "inputCount=" + inputCount +
                ", outputCount=" + outputCount +
                ", parseErrors=" + parseErrors +
                ", transformErrors=" + transformErrors +
                '}';
    }
}

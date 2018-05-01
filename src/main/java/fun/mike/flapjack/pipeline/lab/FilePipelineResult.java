package fun.mike.flapjack.pipeline.lab;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.flapjack.alpha.Problem;
import fun.mike.flapjack.alpha.SerializationResult;

public class FilePipelineResult {
    private final Long inputCount;
    private final Long outputCount;
    private final List<ParseResult> parseErrors;
    private final List<TransformResult> transformErrors;
    private final List<SerializationResult> serializationErrors;

    public FilePipelineResult(Long inputCount, Long outputCount, List<ParseResult> parseErrors, List<TransformResult> transformErrors, List<SerializationResult> serializationErrors) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.parseErrors = parseErrors;
        this.transformErrors = transformErrors;
        this.serializationErrors = serializationErrors;
    }

    public FilePipelineResult(CommonPipelineResult result, List<SerializationResult> serializationErrors) {
        this.inputCount = result.getInputCount();
        this.outputCount = result.getOutputCount();
        this.parseErrors = result.getParseErrors();
        this.transformErrors = result.getTransformErrors();
        this.serializationErrors = serializationErrors;
    }

    public FilePipelineResult withSerializationErrors(List<SerializationResult> serializationErrors) {
        return new FilePipelineResult(inputCount, outputCount, parseErrors, transformErrors, serializationErrors);
    }

    public boolean isOk() {
        return parseErrors.isEmpty() && transformErrors.isEmpty() && serializationErrors.isEmpty();
    }

    public Long getInputCount() {
        return inputCount;
    }

    public Long getOutputCount() {
        return outputCount;
    }

    public Long getErrorCount() {
        return (long) (parseErrors.size() + transformErrors.size() + serializationErrors.size());
    }

    public List<ParseResult> getParseErrors() {
        return parseErrors;
    }

    public List<TransformResult> getTransformErrors() {
        return transformErrors;
    }

    public List<SerializationResult> getSerializationErrors() {
        return serializationErrors;
    }

    @Override
    public String toString() {
        return "FilePipelineResult{" +
                "inputCount=" + inputCount +
                ", outputCount=" + outputCount +
                ", parseErrors=" + parseErrors +
                ", transformErrors=" + transformErrors +
                ", serializationErrors=" + serializationErrors +
                '}';
    }


    public String summarize() {
        if (isOk()) {
            return String.join("\n",
                               "All records processed successfully with no errors.",
                               "Input count: " + inputCount,
                               "Output count: " + outputCount);

        }

        String parseErrorListing = parseErrors.stream()
                .map(error -> String.format("Line: |%s|\n%d problems found when parsing:\n%s",
                                            error.getLine(),
                                            error.getProblems().size(),
                                            error.getProblems().stream()
                                                    .map(Problem::explain)
                                                    .collect(Collectors.joining("\n"))))
                .collect(Collectors.joining("\n\n"));
        long errorCount = getErrorCount();

        String transformErrorListing = transformErrors.stream()
                .map(error -> String.format("%s\n%s\n%s",
                                            error.getLine(),
                                            error.getRecord(),
                                            stackTrace(error.getException())))
                .collect(Collectors.joining("\n\n"));

        String serializationErrorListing = serializationErrors.stream()
                .map(error -> String.format("Line: |%s|\n%d problems found during serialization:\n%s",
                                            error.getRecord(),
                                            error.getProblems().size(),
                                            error.getProblems().stream()
                                                    .map(Problem::explain)
                                                    .collect(Collectors.joining("\n"))))
                .collect(Collectors.joining("\n\n"));

        String errorListing = Arrays.asList(parseErrorListing,
                                            transformErrorListing,
                                            serializationErrorListing)
                .stream()
                .filter(listing -> !listing.trim().equals(""))
                .collect(Collectors.joining("\n\n"));

        return String.join("\n",
                           String.format("Failed to process %d of %d records.",
                                         errorCount,
                                         inputCount),
                           "Input count: " + inputCount,
                           "Output count: " + outputCount,
                           "",
                           errorListing);
    }

    public String stackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}

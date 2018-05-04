package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import fun.mike.flapjack.alpha.Problem;

public class DefaultPipelineErrorExplainer implements PipelineErrorVisitor {
    private final List<String> explanations;

    public DefaultPipelineErrorExplainer() {
        this.explanations = new LinkedList<>();
    }

    private void add(String explanation) {
        explanations.add(explanation);
    }

    public String explain() {
        return String.join("\n\n", explanations);
    }

    @Override
    public void visit(SerializationPipelineError error) {
        add(explainSerialization(error));
    }

    @Override
    public void visit(ParsePipelineError error) {
        add(explainParse(error));
    }

    @Override
    public void visit(TransformPipelineError error) {
        add(explainTransform(error));
    }

    @Override
    public void visit(OutputPipelineError error) {
        add(explainOutput(error));
    }

    public static String explainParse(ParsePipelineError error) {
        return "Record #" +
                error.getNumber() +
                ": Failed to parse record\nLine: |" +
                error.getLine() +
                "|\nRecord: " +
                error.getRecord() +
                "\n" +
                problemList(error.getProblems());
    }

    public static String explainSerialization(SerializationPipelineError error) {
        return "Record #" +
                error.getNumber() +
                ": Failed to serialize record\nLine: |" +
                error.getLine() +
                "|\nRecord: " +
                error.getRecord() +
                "\n" +
                problemList(error.getProblems());
    }

    public static String explainTransform(TransformPipelineError error) {
        return "Record #" +
                error.getNumber() +
                ": Exception thrown during transform\nLine: |" +
                error.getLine() +
                "|\nRecord: " +
                error.getRecord() +
                "\nOperation: " +
                error.getOperationInfo() +
                "\n" +
                Exceptions.stackTrace(error.getException());
    }

    public static String explainOutput(OutputPipelineError error) {
        return "Record #" +
                error.getNumber() +
                ": Exception thrown during output process\nLine: |" +
                error.getLine() +
                "|\nRecord: " +
                error.getRecord() +
                "\n" +
                Exceptions.stackTrace(error.getException());
    }

    private static String problemList(List<Problem> problems) {
        String problemListing = problems.stream()
                .map(problem -> "  - " + problem.explain())
                .collect(Collectors.joining("\n"));
        return "Problems:\n" + problemListing;
    }
}

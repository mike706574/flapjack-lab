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
        StringBuilder buffer = new StringBuilder();
        buffer.append("Record #");
        buffer.append(error.getNumber());
        buffer.append(": Failed to parse record\nLine: |");
        buffer.append(error.getLine());
        buffer.append("|\nRecord: ");
        buffer.append(error.getRecord());
        buffer.append("\n");
        buffer.append(problemList(error.getProblems()));
        return buffer.toString();
    }

    public static String explainSerialization(SerializationPipelineError error) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Record #");
        buffer.append(error.getNumber());
        buffer.append(": Failed to serialize record\nLine: |");
        buffer.append(error.getLine());
        buffer.append("|\nRecord: ");
        buffer.append(error.getRecord());
        buffer.append("\n");
        buffer.append(problemList(error.getProblems()));
        return buffer.toString();
    }

    public static String explainTransform(TransformPipelineError error) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Record #");
        buffer.append(error.getNumber());
        buffer.append(": Exception thrown during transform\nLine: |");
        buffer.append(error.getLine());
        buffer.append("|\nRecord: ");
        buffer.append(error.getRecord());
        buffer.append("\nOperation: ");
        buffer.append(error.getOperationInfo());
        buffer.append("\n");
        buffer.append(Exceptions.stackTrace(error.getException()));
        return buffer.toString();
    }

    public static String explainOutput(OutputPipelineError error) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Record #");
        buffer.append(error.getNumber());
        buffer.append(": Exception thrown during output process\nLine: |");
        buffer.append(error.getLine());
        buffer.append("|\nRecord: ");
        buffer.append(error.getRecord());
        buffer.append("\n");
        buffer.append(Exceptions.stackTrace(error.getException()));
        return buffer.toString();
    }

    private static String problemList(List<Problem> problems) {
        String problemListing = problems.stream()
                .map(problem -> "  - " + problem.explain())
                .collect(Collectors.joining("\n"));
        return "Problems:\n" + problemListing;
    }
}

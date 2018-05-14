package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import fun.mike.flapjack.alpha.Problem;
import fun.mike.record.alpha.Record;

public class DefaultPipelineErrorExplainer implements PipelineErrorVisitor {
    private final List<String> explanations;

    public DefaultPipelineErrorExplainer() {
        this.explanations = new LinkedList<>();
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
        Details details = getDetails(error);

        return "Record #" +
                details.number +
                ": Failed to serialize record\nLine: |" +
                details.line +
                "|\nRecord: " +
                error.getRecord() +
                "\n" +
                problemList(error.getProblems());
    }

    public static String explainTransform(TransformPipelineError error) {
        Details details = getDetails(error);

        return "Record #" +
                details.number +
                ": Exception thrown during transform\nLine: |" +
                details.line +
                "|\nRecord: " +
                error.getRecord() +
                "\nOperation: " +
                error.getOperationInfo() +
                "\n" +
                Exceptions.stackTrace(error.getException());
    }

    public static String explainOutput(OutputPipelineError error) {
        Details details = getDetails(error);
        return "Record #" +
                details.number +
                ": Exception thrown during output process\nLine: |" +
                details.line +
                "|\nRecord: " +
                error.getRecord() +
                "\n" +
                Exceptions.stackTrace(error.getException());
    }

    private static Details getDetails(PipelineError error) {
        Record record = error.getRecord();

        if (record == null) {
            return new Details(error.getNumber(), error.getLine());
        }

        Record metadata = error.getRecord().getMetadata();
        int number = metadata.optionalInteger("number").orElse(error.getNumber());
        String line = metadata.optionalString("line").orElse(error.getLine());
        return new Details(number, line);
    }

    private static String problemList(List<Problem> problems) {
        String problemListing = problems.stream()
                .map(problem -> "  - " + problem.explain())
                .collect(Collectors.joining("\n"));
        return "Problems:\n" + problemListing;
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

    private static final class Details {
        public final int number;
        public final String line;

        private Details(int number, String line) {
            this.number = number;
            this.line = line;
        }
    }
}

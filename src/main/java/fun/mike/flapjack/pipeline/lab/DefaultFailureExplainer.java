package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import fun.mike.flapjack.alpha.Problem;
import fun.mike.record.alpha.Record;

public class DefaultFailureExplainer implements FailureVisitor {
    private final List<String> explanations;

    public DefaultFailureExplainer() {
        this.explanations = new LinkedList<>();
    }

    public static String explainParse(ParseFailure failure) {
        return "Record #" +
                failure.getNumber() +
                ": Failed to parse record\nLine: |" +
                failure.getLine() +
                "|\nRecord: " +
                failure.getRecord() +
                "\n" +
                problemList(failure.getProblems());
    }

    public static String explainSerialization(SerializationFailure failure) {
        Details details = getDetails(failure);

        return "Record #" +
                details.number +
                ": Failed to serialize record\nLine: |" +
                details.line +
                "|\nRecord: " +
                failure.getRecord() +
                "\n" +
                problemList(failure.getProblems());
    }

    public static String explainTransform(TransformFailure failure) {
        Details details = getDetails(failure);

        return "Record #" +
                details.number +
                ": Exception thrown during transform\nLine: |" +
                details.line +
                "|\nRecord: " +
                failure.getRecord() +
                "\nOperation: " +
                failure.getOperationInfo() +
                "\n" +
                Exceptions.stackTrace(failure.getException());
    }

    public static String explainOutput(OutputFailure failure) {
        Details details = getDetails(failure);
        return "Record #" +
                details.number +
                ": Exception thrown during output process\nLine: |" +
                details.line +
                "|\nRecord: " +
                failure.getRecord() +
                "\nFull stack trace:\n" +
                Exceptions.stackTrace(failure.getException());
    }

    private static Details getDetails(Failure failure) {
        Record record = failure.getRecord();

        if (record == null) {
            return new Details(failure.getNumber(), failure.getLine());
        }

        Record metadata = failure.getRecord().getMetadata();
        int number = metadata.optionalInteger("number").orElse(failure.getNumber());
        String line = metadata.optionalString("line").orElse(failure.getLine());
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
    public void visit(SerializationFailure failure) {
        add(explainSerialization(failure));
    }

    @Override
    public void visit(ParseFailure failure) {
        add(explainParse(failure));
    }

    @Override
    public void visit(TransformFailure failure) {
        add(explainTransform(failure));
    }

    @Override
    public void visit(OutputFailure failure) {
        add(explainOutput(failure));
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

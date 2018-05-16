package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import de.vandermeer.asciitable.AsciiTable;

public class PipelineExplainer {
    public static <T> String explainResult(PipelineResult<T> result) {
        int inputCount = result.getInputCount();
        int outputCount = result.getOutputCount();
        int failureCount = result.getFailureCount();
        String outputDescription = outputCount == 1
                ? "1 record" :
                outputCount + " records";

        if (result.getFailureCount() == 0) {
            String inputDescription = inputCount == 1 ? "1 of 1 records" : "all " + inputCount + " records";
            return String.format("Successfully processed %s. %s emitted.",
                                 inputDescription,
                                 outputDescription);

        }

        String inputDescription = inputCount == 1 ?
                failureCount + "of 1 record" :
                failureCount + " of " + inputCount + " records";

        return String.format("Failed to process %s. %s written to output.\n\nFailures:\n\n%s",
                             inputDescription,
                             outputDescription,
                             explainFailures(result.getFailures()));
    }

    public static <T> String explainFailures(List<Failure> failures) {
        DefaultFailureExplainer explainer = new DefaultFailureExplainer();
        for (Failure failure : failures) {
            failure.accept(explainer);
        }
        return explainer.explain();
    }

    public static String explainInput(InputContext inputContext) {
        InputContextExplainer inputExplainer = new InputContextExplainer();
        inputContext.accept(inputExplainer);
        return inputExplainer.explain();
    }

    public static <T> String explainOutput(OutputContext<T> outputContext) {
        OutputContextExplainer outputExplainer = new OutputContextExplainer();
        outputContext.accept(outputExplainer);
        return outputExplainer.explain();
    }

    private static String heading(String text) {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRule();
        table.addRow(text);
        table.addRule();
        table.addRule();
        return table.render(40);
    }

    private static String operationsListing(List<Operation> operations) {
        AsciiTable opsTable = new AsciiTable();
        opsTable.addRule();
        opsTable.addRow("Number", "Type", "Identifier", "Description");
        opsTable.addRule();
        for (OperationInfo info : Operations.info(operations)) {
            opsTable.addRow(info.getNumber(),
                            info.getType(),
                            whenNull(info.getId(), "None"),
                            whenNull(info.getDescription(), "None"));
            opsTable.addRule();
        }

        return opsTable.render();
    }

    private static String whenNull(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }
}

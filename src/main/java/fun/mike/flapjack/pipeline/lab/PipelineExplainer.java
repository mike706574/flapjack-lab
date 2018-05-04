package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import de.vandermeer.asciitable.AsciiTable;
import fun.mike.flapjack.lab.FormatExplainer;

public class PipelineExplainer {
    public static <T> String explainResult(PipelineResult<T> result) {
        result.getInputCount();
        if(result.getErrorCount() == 0) {
            return String.format("Successfully processed all %d records. %d records written to output file.",
                                 result.getInputCount(),
                                 result.getOutputCount());
        }

        return String.format("Failed to run %d of %d records. %d records written to output file.\n\nErrors:\n\n%s",
                             result.getErrorCount(),
                             result.getInputCount(),
                             result.getOutputCount(),
                             explainErrors(result.getErrors()));
    }

    public static <T> String explainErrors(List<PipelineError> errors) {
        DefaultPipelineErrorExplainer explainer = new DefaultPipelineErrorExplainer();
        for(PipelineError error : errors) {
            error.accept(explainer);
        }
        return explainer.explain();
    }

    public static String explainProps(FlatInputFile flatInputFile, List<Operation> operations, FlatFileOutputContext outputFile) {
        AsciiTable inputTable = new AsciiTable();
        inputTable.addRule();
        inputTable.addRow("Path", flatInputFile.getPath());
        inputTable.addRule();
        inputTable.addRow("Skip", flatInputFile.getSkip());
        inputTable.addRule();
        inputTable.addRow("Skip Last", flatInputFile.getSkipLast());
        inputTable.addRule();

        AsciiTable outputTable= new AsciiTable();
        outputTable.addRule();
        outputTable.addRow("Path", outputFile.getPath());
        outputTable.addRule();

        return String.join("\n",
                           heading("INPUT"),
                           "Properties:",
                           inputTable.render(),
                           "Format:",
                           FormatExplainer.explain(flatInputFile.getFormat()),
                           heading("TRANSFORM"),
                           "Operations:",
                           operationsListing(operations),
                           heading("OUTPUT"),
                           "Properties:",
                           outputTable.render(),
                           "Format:",
                           FormatExplainer.explain(outputFile.getFormat())
        );
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
        if(value == null) {
            return defaultValue;
        }
        return value.toString();
    }
}

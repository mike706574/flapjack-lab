package fun.mike.flapjack.lab;

import java.util.List;
import java.util.Map;

import de.vandermeer.asciitable.AsciiTable;
import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Framing;

public class DelimitedFormatExplainer {

    public static String explain(DelimitedFormat format) {
        String id = format.getId();
        String description = format.getDescription();
        Character delimiter = format.getDelimiter();
        Framing framing = format.getFraming();
        Character frameDelimiter = format.getFrameDelimiter()
                .orElse(null);
        boolean endingDelimiter = format.hasEndingDelimiter();
        int offset = format.getOffset();
        List<Column> columns = format.getColumns();

        AsciiTable summaryTable = new AsciiTable();
        summaryTable.addRule();
        summaryTable.addRow("Identifier", id);
        summaryTable.addRule();
        summaryTable.addRow("Type", "Delimited");
        summaryTable.addRule();
        summaryTable.addRow("Description", whenNull(description, "A delimited format."));
        summaryTable.addRule();
        summaryTable.addRule();
        summaryTable.addRow("Skip First", format.getSkipFirst());
        summaryTable.addRule();
        summaryTable.addRow("Skip Last", format.getSkipLast());
        summaryTable.addRule();
        summaryTable.addRow("Number of Columns", columns.size());
        summaryTable.addRule();
        summaryTable.addRow("Delimiter", explainDelimiter(delimiter));
        summaryTable.addRule();
        summaryTable.addRow("Framing", explainFraming(framing, frameDelimiter));
        summaryTable.addRule();
        summaryTable.addRow("Ending Delimiter", explainEndingDelimiter(endingDelimiter));
        summaryTable.addRule();
        summaryTable.addRow("Offset", explainOffset(offset));
        summaryTable.addRule();

        AsciiTable optionsTable = new AsciiTable();
        optionsTable.addRule();
        optionsTable.addRow("Name", "Description", "Value");
        optionsTable.addRule();
        optionsTable.addRow("delimiter", "The column delimiter", delimiter);
        optionsTable.addRule();
        optionsTable.addRow("endingDelimiter", "Whether or not an ending delimiter is required", endingDelimiter);
        optionsTable.addRule();
        optionsTable.addRow("framing", "Whether or not values are framed", whenNull(framing, "null"));
        optionsTable.addRule();
        optionsTable.addRow("frameDelimiter", "Frame delimiter", whenNull(frameDelimiter, "N/A"));
        optionsTable.addRule();
        optionsTable.addRow("offset", "Number of columns to skip", whenNull(offset, "null"));
        optionsTable.addRule();
        optionsTable.addRow("hasHeader", "Whether or not to include a header when serializing", format.hasHeader());
        optionsTable.addRule();
        optionsTable.addRow("skipFirst", "Number of records to skip when parsing", whenNull(frameDelimiter, "N/A"));
        optionsTable.addRule();
        optionsTable.addRow("skipLast", "Number of ending records to skip when parsing", whenNull(offset, "null"));
        optionsTable.addRule();

        AsciiTable columnTable = new AsciiTable();
        columnTable.addRule();
        columnTable.addRow("Name", "Type", "Type Desc", "Props");
        columnTable.addRule();

        for (Column column : columns) {
            String type = column.getType();
            Map<String, Object> props = column.getProps();
            columnTable.addRow(column.getId(),
                               column.getType(),
                               AttributeExplainer.explainType(type),
                               AttributeExplainer.explainProps(props));
            columnTable.addRule();
        }

        return String.join("\n",
                           "Summary:",
                           summaryTable.render(),
                           "Options:",
                           optionsTable.render(),
                           "Columns (" + columns.size() + " total):",
                           columnTable.render());
    }

    private static String whenNull(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    private static String explainOffset(int offset) {
        if (offset == 0) {
            return "No columns will be skipped";
        }
        return String.format("The first %d columns will be skipped",
                             offset);
    }

    private static String explainDelimiter(Character delimiter) {
        return String.format("Columns are delimited by %s",
                             explainCharacter(delimiter));
    }

    private static String explainEndingDelimiter(boolean endingDelimiter) {
        if (endingDelimiter) {
            return "The record must end with a delimiter";
        }
        return "The record must not end with a delimiter";
    }

    private static String explainFraming(Framing framing, Character frameDelimiter) {
        switch (framing) {
            case REQUIRED:
                return String.format("Values must be framed with %s",
                                     explainCharacter(frameDelimiter));
            case OPTIONAL:
                return String.format("Values may or may not be framed by %s",
                                     explainCharacter(frameDelimiter));
            case NONE:
                return "Values must not be framed";
        }

        throw new IllegalStateException("Unexpected framing value: " + framing);
    }

    private static String explainCharacter(Character character) {
        if (character == ',') return "commas";
        if (character == '|') return "pipes";
        if (character == '"') return "double quotation marks";
        if (character == '\'') return "single quotation marks";
        return String.format("a %s character", character);
    }
}

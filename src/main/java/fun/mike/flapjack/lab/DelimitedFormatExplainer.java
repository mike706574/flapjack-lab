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

        AsciiTable summary = new AsciiTable();
        summary.addRule();
        summary.addRow(explainDelimiter(delimiter));
        summary.addRule();
        summary.addRow(explainFraming(framing, frameDelimiter));
        summary.addRule();
        summary.addRow(explainEndingDelimiter(endingDelimiter));
        summary.addRule();
        summary.addRow(explainOffset(offset));
        summary.addRule();

        AsciiTable options = new AsciiTable();
        options.addRule();
        options.addRow("Name", "Description", "Value");
        options.addRule();
        options.addRow("delimiter", "The column delimiter", delimiter);
        options.addRule();
        options.addRow("endingDelimiter", "Whether or not an ending delimiter is required", endingDelimiter);
        options.addRule();
        options.addRow("framing", "Whether or not values are framed", framing);
        options.addRule();
        options.addRow("frameDelimiter", "Frame delimiter", frameDelimiter);
        options.addRule();
        options.addRow("offset", "Number of columns to skip", offset);
        options.addRule();

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
                           "ID: " + id,
                           "Type: Delimited",
                           "Description: " + description,
                           "Number of Columns: " + columns.size(),
                           "Summary:",
                           summary.render(),
                           "Options:",
                           options.render(),
                           "Columns (" + columns.size() + " total):",
                           columnTable.render());
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

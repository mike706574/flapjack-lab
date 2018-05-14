package fun.mike.flapjack.lab;

import java.util.List;
import java.util.Map;

import de.vandermeer.asciitable.AsciiTable;
import fun.mike.flapjack.alpha.Field;
import fun.mike.flapjack.alpha.FixedWidthFormat;

public class FixedWidthFormatExplainer {

    public static String explain(FixedWidthFormat format) {
        String id = format.getId();
        String description = format.getDescription();
        List<Field> fields = format.getFields();

        AsciiTable summaryTable = new AsciiTable();
        summaryTable.addRule();
        summaryTable.addRow("Identifier", id);
        summaryTable.addRule();
        summaryTable.addRow("Type", "Fixed-width");
        summaryTable.addRule();
        summaryTable.addRow("Description", whenNull(description, "A fixed-width format."));
        summaryTable.addRule();
        summaryTable.addRow("Number of Fields", fields.size());
        summaryTable.addRule();
        summaryTable.addRow("Skip First", format.getSkipFirst());
        summaryTable.addRule();
        summaryTable.addRow("Skip Last", format.getSkipLast());
        summaryTable.addRule();

        AsciiTable fieldTable = new AsciiTable();
        fieldTable.addRule();
        fieldTable.addRow("Name", "Length", "Type", "Type Desc", "Props");
        fieldTable.addRule();
        for (Field field : fields) {
            String type = field.getType();
            Map<String, Object> props = field.getProps();
            fieldTable.addRow(field.getId(),
                              field.getLength(),
                              field.getType(),
                              AttributeExplainer.explainType(type),
                              AttributeExplainer.explainProps(props));
            fieldTable.addRule();
        }

        int recordSize = fields.stream()
                .mapToInt(Field::getLength)
                .sum();

        return String.join("\n",
                           "Summary:",
                           summaryTable.render(),
                           "Fields (" + fields.size() + " total):",
                           fieldTable.render());
    }

    private static String whenNull(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

}

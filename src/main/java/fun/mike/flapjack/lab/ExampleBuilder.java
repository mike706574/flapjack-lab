package fun.mike.flapjack.lab;

import java.util.function.Function;
import java.util.stream.Collectors;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;

public class ExampleBuilder {
    public String record(DelimitedFormat format) {
        Function<Column, String> val = col -> {
            switch (col.getType()) {
                case "string":
                    return "\"\"";
                case "trimmed-string":
                    return "\"\"";
                case "integer":
                    return "0";
                case "big-decimal":
                    return "new BigDecimal(0)";
                default:
                    throw new IllegalArgumentException("Unsupported type: " + col.getType());
            }
        };

        String values = format.getColumns()
                .stream()
                .map(col -> String.format("\"%s\", %s",
                                          col.getId(),
                                          val.apply(col)))
                .collect(Collectors.joining(",\n"));

        return String.format("Record.of(%s);",
                             values);
    }
}

package fun.mike.flapjack.lab;

import java.util.Map;
import java.util.stream.Collectors;

public class AttributeExplainer {
    public static String explainType(String type) {
        switch (type) {
            case "string":
                return "A string";
            case "trimmed-string":
                return "A trimmed string";
            case "integer":
                return "An integer";
            case "date":
                return "A Date";
            case "double":
                return "A Double";
            case "big-decimal":
                return "A BigDecimal";
            case "string-enum":
                return "An string enumeration";
        }
        return String.format("A %s property", type);
    }

    public static String explainProps(Map<String, Object> props) {
        if (props.isEmpty()) {
            return "None";
        }
        return props.entrySet()
                .stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }
}

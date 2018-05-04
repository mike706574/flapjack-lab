package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;

public class Operations {
    public static List<CompiledOperation> compile(List<Operation> operations) {
        List<CompiledOperation> data = new LinkedList<>();
        for (int i = 0; i < operations.size(); i++) {
            Operation operation = operations.get(i);
            OperationInfo info = buildInfo(operation, i);
            data.add(new CompiledOperation(operation, info));
        }
        return data;
    }

    public static List<OperationInfo> info(List<Operation> operations) {
        List<OperationInfo> allInfo = new LinkedList<>();
        for (int i = 0; i < operations.size(); i++) {
            Operation operation = operations.get(i);
            OperationInfo info = buildInfo(operation, i);
            allInfo.add(info);
        }
        return allInfo;
    }

    private static OperationInfo buildInfo(Operation operation, int index) {
        int number = index + 1;
        String type = getType(operation);

        String id = operation.getId();
        if(id == null) {
            id = type + "-" + number;
        }

        String description = operation.getDescription();
        if(description == null) {
            description = uppercaseFirstLetter(type) + " operation";
        }

        return new OperationInfo(number, id, description, type);
    }

    public static String getType(Operation operation) {
        String type = lowercaseFirstLetter(operation.getClass().getSimpleName());
        if(type.endsWith("Operation") && type.length() > 9) {
            return type.substring(0, type.length() - 9);
        }
        return type;
    }

    private static String uppercaseFirstLetter(String string) {
        char c[] = string.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        string = new String(c);
        return string;
    }

    private static String lowercaseFirstLetter(String string) {
        char c[] = string.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        string = new String(c);
        return string;
    }
}

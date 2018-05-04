package fun.mike.flapjack.pipeline.lab;

public class OperationInfo {
    private final int number;
    private final String id;
    private final String description;
    private final String type;

    public OperationInfo(int number, String id, String description, String type) {
        this.number = number;
        this.id = id;
        this.description = description;
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "OperationInfo{" +
                "number=" + number +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

package fun.mike.flapjack.pipeline.lab;

public interface PipelineError {
    Long getNumber();

    String getLine();

    void accept(PipelineErrorVisitor visitor);
}

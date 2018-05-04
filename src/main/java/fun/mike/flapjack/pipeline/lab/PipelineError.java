package fun.mike.flapjack.pipeline.lab;

public interface PipelineError {
    int getNumber();

    String getLine();

    void accept(PipelineErrorVisitor visitor);
}

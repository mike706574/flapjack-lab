package fun.mike.flapjack.pipeline.lab;

public interface PipelineErrorVisitor {
    void visit(SerializationPipelineError error);

    void visit(ParsePipelineError error);

    void visit(TransformPipelineError error);

    void visit(OutputPipelineError error);
}

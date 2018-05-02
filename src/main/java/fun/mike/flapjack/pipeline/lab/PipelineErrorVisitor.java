package fun.mike.flapjack.pipeline.lab;

public interface PipelineErrorVisitor {
    public void visit(SerializationPipelineError error);
    public void visit(ParsePipelineError error);
    public void visit(TransformPipelineError error);
    public void visit(OutputPipelineError error);
}

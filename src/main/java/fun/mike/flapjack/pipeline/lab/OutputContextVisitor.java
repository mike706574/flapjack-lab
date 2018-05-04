package fun.mike.flapjack.pipeline.lab;

public interface OutputContextVisitor {
    void accept(FlatFileOutputContext outputContext);
    void accept(ListOutputContext outputContext);
    void accept(ForEachOutputContext outputContext);
    void accept(ReduceOutputContext outputContext);
    void accept(ProcessOutputContext outputContext);
    void accept(GroupOutputContext outputContext);

}

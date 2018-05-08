package fun.mike.flapjack.pipeline.lab;

public interface InputContextVisitor {
    void accept(FlatFileInputContext inputContext);

    void accept(IterableInputContext inputContext);

    void accept(CollectionInputContext collectionInputContext);
}

package fun.mike.flapjack.pipeline.lab;

public interface InputContext {
    InputChannel buildChannel();
    void accept(InputContextVisitor visitor);
}

package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.lab.FormatExplainer;

public class InputContextExplainer implements InputContextVisitor {
    private String explanation;

    public InputContextExplainer() {
        explanation = "Nothing.";
    }

    public String explain() {
        return explanation;
    }

    @Override
    public void accept(FlatFileInputContext inputContext) {
        String path = inputContext.getPath();
        Format format = inputContext.getFormat();
        explanation = String.join("\n",
                                  "Reading from a flat file.",
                                  "File path: " + path,
                                  FormatExplainer.explain(format));
    }

    @Override
    public void accept(IterableInputContext inputContext) {
        explanation = "From an iterable of class " + inputContext.getRecords().getClass().getSimpleName() + ".";
    }

    @Override
    public void accept(CollectionInputContext inputContext) {
        explanation = "From a collection of class " + inputContext.getRecords().getClass().getSimpleName() + " containing " + inputContext.getRecords().size() + " elements.";
    }
}
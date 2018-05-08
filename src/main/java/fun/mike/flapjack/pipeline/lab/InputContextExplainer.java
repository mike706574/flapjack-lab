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
        int skip = inputContext.getSkip();
        int skipLast = inputContext.getSkipLast();
        Format format = inputContext.getFormat();
        explanation = String.join("\n",
                                  "Reading from a flat file.",
                                  "File path: " + path,
                                  "Skip: " + skip,
                                  "Skip Last: " + skipLast,
                                  FormatExplainer.explain(format));
    }
}

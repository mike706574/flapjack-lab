package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.lab.FormatExplainer;

public class OutputContextExplainer implements OutputContextVisitor {
    private String explanation;

    public OutputContextExplainer() {
        explanation = "Nothing.";
    }

    public String explain() {
        return explanation;
    }

    @Override
    public void accept(FlatFileOutputContext outputContext) {
        String path = outputContext.getPath();
        boolean includeHeader = outputContext.includeHeader();
        Format format = outputContext.getFormat();
        String headerLabel = includeHeader ? "Yes" : "No";
        explanation = String.join("\n",
                                  "Writing to a flat file.",
                                  "File path: " + path,
                                  "Include header? " + headerLabel,
                                  FormatExplainer.explain(format));
    }

    @Override
    public void accept(ListOutputContext outputContext) {
        explanation = "A list.";
    }

    @Override
    public void accept(ForEachOutputContext outputContext) {
        explanation = "A custom consumer.";
    }

    @Override
    public void accept(ReduceOutputContext outputContext) {
        explanation = "A reduction.";
    }

    @Override
    public void accept(ProcessOutputContext outputContext) {
        explanation = "A process.";
    }

    @Override
    public void accept(GroupOutputContext outputContext) {
        explanation = "A group.";
    }
}

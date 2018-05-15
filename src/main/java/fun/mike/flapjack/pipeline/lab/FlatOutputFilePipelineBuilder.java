package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class FlatOutputFilePipelineBuilder {
    private final InputContext inputContext;
    private final Transform transform;

    private final String outputPath;
    private final Format outputFormat;
    private boolean logFormat;

    public FlatOutputFilePipelineBuilder(InputContext inputContext, Transform transform, String outputPath, Format outputFormat) {
        this.inputContext = inputContext;
        this.transform = transform;
        this.outputPath = outputPath;
        this.outputFormat = outputFormat;
        this.logFormat = false;
    }

    public FlatFilePipeline build() {
        FlatFileOutputContext outputFile = new FlatFileOutputContext(outputPath, outputFormat, logFormat);
        return new FlatFilePipeline(inputContext, transform, outputFile);
    }

    public FlatOutputFilePipelineBuilder logFormat() {
        this.logFormat = true;
        return this;
    }

    public FlatFileResult run() {
        return build().run();
    }
}

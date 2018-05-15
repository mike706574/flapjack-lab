package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class FlatOutputFilePipelineBuilder {
    private final InputContext inputContext;
    private final Transform transform;

    private final String outputPath;
    private final Format outputFormat;

    public FlatOutputFilePipelineBuilder(InputContext inputContext, Transform transform, String outputPath, Format outputFormat) {
        this.inputContext = inputContext;
        this.transform = transform;
        this.outputPath = outputPath;
        this.outputFormat = outputFormat;
    }

    public FlatFilePipeline build() {
        FlatFileOutputContext outputFile = new FlatFileOutputContext(outputPath, outputFormat);
        return new FlatFilePipeline(inputContext, transform, outputFile);
    }

    public FlatFileResult run() {
        return build().run();
    }
}

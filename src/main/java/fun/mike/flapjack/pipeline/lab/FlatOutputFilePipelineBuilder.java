package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class FlatOutputFilePipelineBuilder {
    private final InputContext inputContext;
    private final Transform transform;

    private final String outputPath;
    private final Format outputFormat;
    private Boolean includeHeader;

    public FlatOutputFilePipelineBuilder(InputContext inputContext, Transform transform, String outputPath, Format outputFormat, Boolean includeHeader) {
        this.inputContext = inputContext;
        this.transform = transform;
        this.outputPath = outputPath;
        this.outputFormat = outputFormat;
        this.includeHeader = includeHeader;
    }

    public FlatOutputFilePipelineBuilder includeHeader() {
        includeHeader = true;
        return this;
    }

    public FlatFilePipeline build() {
        FlatFileOutputContext outputFile = new FlatFileOutputContext(outputPath, outputFormat, includeHeader);
        return new FlatFilePipeline(inputContext, transform, outputFile);
    }

    public PipelineResult<Nothing> run() {
        return build().run();
    }
}

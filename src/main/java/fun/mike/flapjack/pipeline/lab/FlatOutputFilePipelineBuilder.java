package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class FlatOutputFilePipelineBuilder {
    private final FlatInputFile inputFile;
    private final Transform transform;

    private final String outputPath;
    private final Format outputFormat;
    private Boolean includeHeader;

    public FlatOutputFilePipelineBuilder(FlatInputFile inputFile, Transform transform, String outputPath, Format outputFormat, Boolean includeHeader) {
        this.inputFile = inputFile;
        this.transform = transform;
        this.outputPath = outputPath;
        this.outputFormat = outputFormat;
        this.includeHeader = includeHeader;
    }

    public FlatOutputFilePipelineBuilder includeHeader() {
        includeHeader = true;
        return this;
    }

    public FileToFilePipeline build() {
        FlatFileOutputContext outputFile = new FlatFileOutputContext(outputPath, outputFormat, includeHeader);
        return new FileToFilePipeline(inputFile, transform, outputFile);
    }

    public PipelineResult<Nothing> run() {
        return build().run();
    }
}

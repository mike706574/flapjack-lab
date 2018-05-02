package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.Format;

public class FlatOutputFileBuilder {
    private final FlatInputFile inputFile;
    private final List<Operation> operations;

    private final String outputPath;
    private final Format outputFormat;
    private Boolean includeHeader;

    public FlatOutputFileBuilder(FlatInputFile inputFile, List<Operation> operations, String outputPath, Format outputFormat, Boolean includeHeader) {
        this.inputFile = inputFile;
        this.operations = operations;
        this.outputPath = outputPath;
        this.outputFormat = outputFormat;
        this.includeHeader = includeHeader;
    }

    public FlatOutputFileBuilder includeHeader() {
        includeHeader = true;
        return this;
    }

    public FileToFilePipeline build() {
        FlatFileOutputContext outputFile = new FlatFileOutputContext(outputPath, outputFormat, includeHeader);
        return FileToFilePipeline.of(inputFile, operations, outputFile);
    }

    public PipelineResult<Nothing> run() {
        return build().run();
    }
}

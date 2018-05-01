package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.Format;

public class OutputFileBuilder {
    private final InputFile inputFile;
    private final List<Operation> steps;
    private final String outputPath;
    private final Format outputFormat;

    public OutputFileBuilder(InputFile inputFile, List<Operation> steps, String outputPath, Format outputFormat) {
        this.inputFile = inputFile;
        this.steps = steps;
        this.outputPath = outputPath;
        this.outputFormat = outputFormat;
    }

    public FileToFilePipeline build() {
        OutputFile outputFile = new OutputFile(outputPath, outputFormat);
        return new FileToFilePipeline(inputFile, steps, outputFile);
    }
}

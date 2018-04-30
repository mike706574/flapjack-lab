package fun.mike.flapjack.pipeline.lab;

import java.util.List;

import fun.mike.flapjack.alpha.Format;

public class OutputFileBuilder {
    private InputFile inputFile;
    private List<Operation> steps;
    private String outputPath;
    private Format outputFormat;

    public OutputFileBuilder(InputFile inputFile, List<Operation> steps, String outputPath, Format outputFormat) {
        this.inputFile = inputFile;
        this.steps = steps;
        this.outputPath = outputPath;
        this.outputFormat = outputFormat;
    }

    public Pipeline build() {
        OutputFile outputFile = new OutputFile(outputPath, outputFormat);
        return new Pipeline(inputFile, steps, outputFile);
    }
}

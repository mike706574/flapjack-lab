package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.UpdateSkipFirstVisitor;
import fun.mike.flapjack.alpha.UpdateSkipLastVisitor;

public class FlatInputFilePipelineBuilder extends InputPipelineBuilder {
    private final String inputPath;
    private Format inputFormat;
    private boolean logFormat;
    private boolean logLines;

    public FlatInputFilePipelineBuilder(String inputPath, Format format) {
        super();
        this.inputPath = inputPath;
        this.inputFormat = format;
        this.logFormat = false;
        this.logLines = false;
    }

    // Options
    public FlatInputFilePipelineBuilder skipFirst(int count) {
        inputFormat = UpdateSkipFirstVisitor.visit(inputFormat, count);
        return this;
    }

    public FlatInputFilePipelineBuilder skipLast(int count) {
        inputFormat = UpdateSkipLastVisitor.visit(inputFormat, count);
        return this;
    }

    public FlatInputFilePipelineBuilder logLines() {
        this.logLines = true;
        return this;
    }

    public FlatInputFilePipelineBuilder logFormat() {
        this.logFormat = true;
        return this;
    }

    @Override
    InputContext buildInputContext() {
        return new FlatFileInputContext(inputPath, inputFormat, logFormat, logLines);
    }
}

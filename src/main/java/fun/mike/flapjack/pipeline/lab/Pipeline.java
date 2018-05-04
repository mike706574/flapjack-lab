package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public interface Pipeline<V> {
    static InputFilePipelineBuilder fromFile(String path, Format format) {
        return new InputFilePipelineBuilder(path, format);
    }

    PipelineResult<V> execute();
}

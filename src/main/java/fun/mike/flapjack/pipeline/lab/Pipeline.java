package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public interface Pipeline<V> {
    static InputFileBuilder fromFile(String path, Format format) {
        return new InputFileBuilder(path, format);
    }

    PipelineResult<V> execute();
}

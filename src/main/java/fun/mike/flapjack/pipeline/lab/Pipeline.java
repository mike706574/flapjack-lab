package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class Pipeline {
    public static InputFileBuilder fromFile(String path, Format format) {
        return new InputFileBuilder(path, format);
    }
}

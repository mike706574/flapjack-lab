package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class OutputFile {
    private final String path;
    private final Format format;

    public OutputFile(String path, Format format) {
        this.path = path;
        this.format = format;
    }

    @Override
    public String toString() {
        return "OutputFile{" +
                "path='" + getPath() + '\'' +
                ", format=" + getFormat() +
                '}';
    }

    public String getPath() {
        return path;
    }

    public Format getFormat() {
        return format;
    }
}

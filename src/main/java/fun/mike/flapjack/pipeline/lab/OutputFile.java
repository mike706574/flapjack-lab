package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class OutputFile {
    public final String path;
    public final Format format;

    public OutputFile(String path, Format format) {
        this.path = path;
        this.format = format;
    }

    @Override
    public String toString() {
        return "OutputFile{" +
                "path='" + path + '\'' +
                ", format=" + format +
                '}';
    }
}

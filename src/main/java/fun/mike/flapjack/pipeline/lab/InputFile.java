package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class InputFile {
    public final String path;
    public final Format format;
    public final int skip;
    public final int skipLast;

    public InputFile(String path, Format format, int skip, int skipLast) {
        this.path = path;
        this.format = format;
        this.skip = skip;
        this.skipLast = skipLast;
    }

    @Override
    public String toString() {
        return "InputFile{" +
                "path='" + path + '\'' +
                ", format=" + format +
                ", skip=" + skip +
                ", skipLast=" + skipLast +
                '}';
    }
}

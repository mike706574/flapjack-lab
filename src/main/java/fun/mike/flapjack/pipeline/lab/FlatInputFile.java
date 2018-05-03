package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class FlatInputFile {
    private final String path;
    private final Format format;
    private final int skip;
    private final int skipLast;

    public FlatInputFile(String path, Format format, int skip, int skipLast) {
        this.path = path;
        this.format = format;
        this.skip = skip;
        this.skipLast = skipLast;
    }

    @Override
    public String toString() {
        return "FlatInputFile{" +
                "path='" + getPath() + '\'' +
                ", format=" + getFormat() +
                ", skip=" + getSkip() +
                ", skipLast=" + getSkipLast() +
                '}';
    }

    public String getPath() {
        return path;
    }

    public Format getFormat() {
        return format;
    }

    public int getSkip() {
        return skip;
    }

    public int getSkipLast() {
        return skipLast;
    }
}
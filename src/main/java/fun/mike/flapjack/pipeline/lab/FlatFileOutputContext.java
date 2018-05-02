package fun.mike.flapjack.pipeline.lab;

import fun.mike.flapjack.alpha.Format;

public class FlatFileOutputContext implements OutputContext<Nothing> {
    private final String path;
    private final Format format;
    private final Boolean includeHeader;

    public FlatFileOutputContext(String path, Format format, Boolean includeHeader) {
        this.path = path;
        this.format = format;
        this.includeHeader = includeHeader;
    }

    public FlatFileOutputContext of(String path, Format format, Boolean includeHeader) {
        return new FlatFileOutputContext(path, format, includeHeader);
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

    @Override
    public OutputChannel<Nothing> buildChannel() {
        return new FlatFileOutputChannel(path, format, includeHeader);
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }
}

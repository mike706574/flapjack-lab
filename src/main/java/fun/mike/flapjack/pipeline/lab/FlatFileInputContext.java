package fun.mike.flapjack.pipeline.lab;

import java.util.Objects;

import fun.mike.flapjack.alpha.Format;

public class FlatFileInputContext implements InputContext {
    private final String path;
    private final Format format;
    private final boolean logFormat;
    private final boolean logLines;

    public FlatFileInputContext(String path, Format format, boolean logFormat, boolean logLines) {
        this.path = path;
        this.format = format;
        this.logFormat = logFormat;
        this.logLines = logLines;
    }

    @Override
    public String toString() {
        return "FlatFileInputContext{" +
                "path='" + path + '\'' +
                ", format=" + format +
                ", logFormat=" + logFormat +
                ", logLines=" + logLines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatFileInputContext that = (FlatFileInputContext) o;
        return logFormat == that.logFormat &&
                logLines == that.logLines &&
                Objects.equals(path, that.path) &&
                Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, format, logFormat, logLines);
    }

    public String getPath() {
        return path;
    }

    public Format getFormat() {
        return format;
    }

    public boolean logFormat() { return logFormat; }

    public boolean logLines() {
        return logLines;
    }

    @Override
    public InputChannel buildChannel() {
        return new FlatFileInputChannel(path, format, logLines);
    }

    @Override
    public void accept(InputContextVisitor visitor) {
        visitor.accept(this);
    }
}

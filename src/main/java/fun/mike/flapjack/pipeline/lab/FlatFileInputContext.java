package fun.mike.flapjack.pipeline.lab;

import java.util.Objects;

import fun.mike.flapjack.alpha.Format;

public class FlatFileInputContext implements InputContext {
    private final String path;
    private final Format format;
    private final boolean logLines;

    public FlatFileInputContext(String path, Format format, boolean logLines) {
        this.path = path;
        this.format = format;
        this.logLines = logLines;
    }

    @Override
    public String toString() {
        return "FlatFileInputContext{" +
                "path='" + path + '\'' +
                ", format=" + format +
                ", logLines=" + logLines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatFileInputContext that = (FlatFileInputContext) o;
        return logLines == that.logLines &&
                Objects.equals(path, that.path) &&
                Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {

        return Objects.hash(path, format, logLines);
    }

    public String getPath() {
        return path;
    }

    public Format getFormat() {
        return format;
    }


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
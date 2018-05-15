package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.SerializationResult;
import fun.mike.io.alpha.Spitter;
import fun.mike.record.alpha.Record;

public class FlatFileOutputContext implements OutputContext<Nothing> {
    private final String path;
    private final Format format;
    private final boolean logFormat;

    public FlatFileOutputContext(String path, Format format, boolean logFormat) {
        this.path = path;
        this.format = format;
        this.logFormat = logFormat;
    }

    public FlatFileOutputContext of(String path, Format format) {
        return new FlatFileOutputContext(path, format, logFormat);
    }

    @Override
    public String toString() {
        return "FlatFileOutputContext{" +
                "path='" + path + '\'' +
                ", format=" + format +
                ", logFormat=" + logFormat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatFileOutputContext that = (FlatFileOutputContext) o;
        return logFormat == that.logFormat &&
                Objects.equals(path, that.path) &&
                Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, format, logFormat);
    }

    public String getPath() {
        return path;
    }

    public Format getFormat() {
        return format;
    }

    public boolean logFormat() {
        return logFormat;
    }

    @Override
    public OutputChannel<Nothing> buildChannel() {
        return new FlatFileOutputChannel(path, format);
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }

    private final class FlatFileOutputChannel implements OutputChannel<Nothing> {
        private final Spitter spitter;
        private final Format format;

        private final List<Failure> failures;

        public FlatFileOutputChannel(String path, Format format) {
            this.format = format;

            this.spitter = new Spitter(path);

            if (format instanceof DelimitedFormat) {
                boolean includeHeader = ((DelimitedFormat)format).hasHeader();
                spitter.spit(HeaderBuilder.build((DelimitedFormat) format));
            }

            this.failures = new LinkedList<>();
        }

        @Override
        public Optional<Failure> put(int number, String line, Record value) {
            SerializationResult serializationResult = format.serialize(value);

            if (serializationResult.isOk()) {
                String outputLine = serializationResult.getValue();
                spitter.spit(outputLine);
                return Optional.empty();
            }

            return Optional.of(SerializationFailure.fromResult(number, line, serializationResult));
        }

        public List<Failure> getFailures() {
            return failures;
        }

        @Override
        public Nothing getValue() {
            return Nothing.value();
        }

        @Override
        public void close() {
            spitter.close();
        }
    }
}

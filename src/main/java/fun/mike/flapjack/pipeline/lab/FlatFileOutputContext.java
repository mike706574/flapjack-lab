package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.SerializationResult;
import fun.mike.io.alpha.Spitter;
import fun.mike.record.alpha.Record;

public class FlatFileOutputContext implements OutputContext<Nothing> {
    private final String path;
    private final Format format;

    public FlatFileOutputContext(String path, Format format) {
        this.path = path;
        this.format = format;
    }

    public FlatFileOutputContext of(String path, Format format) {
        return new FlatFileOutputContext(path, format);
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
        return new FlatFileOutputChannel(path, format);
    }

    @Override
    public void accept(OutputContextVisitor visitor) {
        visitor.accept(this);
    }

    private final class FlatFileOutputChannel implements OutputChannel<Nothing> {
        private final Spitter spitter;
        private final Format format;

        private final List<PipelineError> errors;

        public FlatFileOutputChannel(String path, Format format) {
            this.format = format;

            this.spitter = new Spitter(path);

            if (format instanceof DelimitedFormat) {
                boolean includeHeader = ((DelimitedFormat)format).hasHeader();
                spitter.spit(HeaderBuilder.build((DelimitedFormat) format));
            }

            this.errors = new LinkedList<>();
        }

        @Override
        public Optional<PipelineError> put(int number, String line, Record value) {
            SerializationResult serializationResult = format.serialize(value);

            if (serializationResult.isOk()) {
                String outputLine = serializationResult.getValue();
                spitter.spit(outputLine);
                return Optional.empty();
            }

            return Optional.of(SerializationPipelineError.fromResult(number, line, serializationResult));
        }

        public List<PipelineError> getErrors() {
            return errors;
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

package fun.mike.flapjack.pipeline.lab;

import java.util.LinkedList;
import java.util.List;

import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.SerializationResult;
import fun.mike.io.alpha.Spitter;
import fun.mike.record.alpha.Record;

public class FileOutputChannel implements OutputChannel {
    private final Spitter spitter;
    private final Format format;

    private final List<SerializationResult> serializationErrors;

    public FileOutputChannel(String path, Format format) {
        this.spitter = new Spitter(path);
        this.format = format;
        this.serializationErrors = new LinkedList<>();
    }

    @Override
    public boolean receive(Record value) {
        SerializationResult serializationResult = format.serialize(value);

        if(serializationResult.isOk()) {
            String outputLine = serializationResult.getValue();
            spitter.spit(outputLine);
            return true;
        }

        serializationErrors.add(serializationResult);
        return false;
    }

    public List<SerializationResult> getSerializationErrors() {
        return serializationErrors;
    }

    @Override
    public void close() {
        spitter.close();
    }
}

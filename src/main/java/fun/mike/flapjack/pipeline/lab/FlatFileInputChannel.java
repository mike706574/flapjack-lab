package fun.mike.flapjack.pipeline.lab;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.Stream;

import fun.mike.flapjack.alpha.Format;
import fun.mike.flapjack.alpha.GetSkipFirstVisitor;
import fun.mike.flapjack.alpha.GetSkipLastVisitor;
import fun.mike.flapjack.alpha.ParseResult;
import fun.mike.io.alpha.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlatFileInputChannel implements InputChannel {
    private final Logger log = LoggerFactory.getLogger(FlatFileInputChannel.class);

    private final String path;
    private final Format format;
    private final boolean logLines;
    private final int lineCount;
    private final int limit;
    private int lineIndex;
    private BufferedReader reader;

    public FlatFileInputChannel(String path, Format format, boolean logLines) {
        this.path = path;
        this.format = format;
        this.logLines = logLines;

        this.lineIndex = 0;

        try (Stream<String> stream = IO.streamLines(path)) {
            lineCount = (int) stream.count();
        }
        int skipLast = GetSkipLastVisitor.visit(format);
        limit = lineCount - GetSkipLastVisitor.visit(format);

        try {
            this.reader = new BufferedReader(new FileReader(this.path));
        } catch (FileNotFoundException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public InputResult take() {
        int skipFirst = GetSkipFirstVisitor.visit(format);

        while (lineIndex < skipFirst && lineIndex < lineCount) {
            lineIndex++;
            try {
                reader.readLine();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        int number = lineIndex + 1;
        lineIndex++;

        String line;
        try {
            line = reader.readLine();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        if (logLines) {
            log.debug("Processing record #" + number + ": " + line);
        }

        ParseResult parseResult = format.parse(line);

        if (parseResult.hasProblems()) {
            return InputResult.error(line, ParsePipelineError.fromResult(number, line, parseResult));
        }

        System.out.println("mdata" + parseResult.getValue().getMetadata());
        parseResult.getValue().setMetadataProperty("number", number);
        System.out.println("mdata" + parseResult.getValue().getMetadata());

        return InputResult.ok(parseResult.getValue(), line);
    }

    public boolean hasMore() {
        return lineIndex < limit;
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}

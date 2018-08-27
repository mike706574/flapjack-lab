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
    private final String lineKey;
    private final boolean logLines;
    private final int lineCount;
    private final int limit;
    private final int skipFirst;
    private int lineIndex;
    private BufferedReader reader;

    public FlatFileInputChannel(String path, Format format, String lineKey, boolean logLines) {
        this.path = path;
        this.format = format;
        this.lineKey = lineKey;
        this.logLines = logLines;

        this.lineIndex = 0;

        try (Stream<String> stream = IO.streamLines(path)) {
            lineCount = (int) stream.count();
        }

        skipFirst = GetSkipFirstVisitor.visit(format);
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
        while (lineIndex < skipFirst && lineIndex < lineCount) {
            lineIndex++;
            readLine(reader);
        }



        int number = lineIndex + 1;
        lineIndex++;

        String line = readLine(reader);

        if (logLines) {
            log.debug("Processing record #" + number + ": " + line);
        }

        ParseResult parseResult = format.parse(line);

        if (parseResult.hasProblems()) {
            return InputResult.failure(line, ParseFailure.fromResult(number, line, parseResult));
        }

        if(lineKey != null) {
            parseResult.getValue().set(lineKey, line);
        }

        parseResult.getValue().setMetadataProperty("number", number);

        return InputResult.ok(parseResult.getValue(), line);
    }

    private String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public boolean hasMore() {
        return lineCount > skipFirst && lineIndex < limit;
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

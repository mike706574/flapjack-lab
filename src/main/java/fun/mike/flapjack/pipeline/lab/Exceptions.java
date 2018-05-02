package fun.mike.flapjack.pipeline.lab;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Exceptions {
    public static String stackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}

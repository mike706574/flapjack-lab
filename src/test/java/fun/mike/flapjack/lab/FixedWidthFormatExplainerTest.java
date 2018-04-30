package fun.mike.flapjack.lab;

import java.util.Arrays;
import java.util.List;

import fun.mike.flapjack.alpha.Field;
import fun.mike.flapjack.alpha.FixedWidthFormat;
import fun.mike.flapjack.lab.FixedWidthFormatExplainer;
import org.junit.Test;


public class FixedWidthFormatExplainerTest {
    @Test
    public void explain() {
        List<Field> fields = Arrays.asList(Field.with("foo", 5, "string"),
                                           Field.with("bar", 5, "string"));

        FixedWidthFormat format = new FixedWidthFormat("baz", "Baz", fields);

        System.out.println(FixedWidthFormatExplainer.explain(format));
    }
}

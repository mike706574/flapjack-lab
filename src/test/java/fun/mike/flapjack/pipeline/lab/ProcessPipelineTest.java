package fun.mike.flapjack.pipeline.lab;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import fun.mike.flapjack.alpha.Column;
import fun.mike.flapjack.alpha.DelimitedFormat;
import fun.mike.flapjack.alpha.Format;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ProcessPipelineTest {
    private static final String base = "src/test/resources/pipeline/";

    private static final Format inputFormat =
            DelimitedFormat.unframed("delimited-animals",
                                     "Delimited animals format.",
                                     ',',
                                     Arrays.asList(Column.string("name"),
                                                   Column.integer("legs"),
                                                   Column.string("size")));

    @Test
    public void success() {
        String inputPath = base + "animals.csv";
        String outputPath = base + "animals.dat";

        List<Animal> database = new LinkedList<>();

        ProcessPipeline<Animal> pipeline = Pipeline.fromFile(inputPath, inputFormat)
                .map(x -> x.updateString("size", String::toUpperCase))
                .filter(x -> x.getString("size").equals("MEDIUM"))
                .process(record -> {
                    Animal animal = new Animal(record.getString("name"),
                                               record.getInteger("legs"),
                                               record.getString("size"));
                    database.add(animal);
                    return animal;
                });

        ProcessResult<Animal> result = pipeline.run();

        assertTrue(result.isOk());
        assertEquals(6, result.getInputCount());
        assertEquals(3, result.getOutputCount());

        List<Animal> values = result.orElseThrow();

        assertEquals(3, values.size());

        assertEquals(new Animal("dog", 4, "MEDIUM"),
                     values.get(0));

        assertEquals(new Animal("fox", 4, "MEDIUM"),
                     values.get(1));

        assertEquals(new Animal("ostrich", 2, "MEDIUM"),
                     values.get(2));

        assertEquals(3, database.size());
    }

    private static final class Animal {
        public final String name;
        public final int legs;
        public final String size;

        public Animal(String name, int legs, String size) {
            this.name = name;
            this.legs = legs;
            this.size = size;
        }

        @Override
        public String toString() {
            return "Animal{" +
                    "name='" + name + '\'' +
                    ", legs=" + legs +
                    ", size='" + size + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Animal animal = (Animal) o;
            return legs == animal.legs &&
                    Objects.equals(name, animal.name) &&
                    Objects.equals(size, animal.size);
        }

        @Override
        public int hashCode() {

            return Objects.hash(name, legs, size);
        }
    }
}

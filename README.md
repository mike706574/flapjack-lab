# flapjack-lab

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fun.mike/flapjack-lab/badge.svg)](https://maven-badges.herokuapp.com/maven-central/fun.mike/flapjack-lab)
[![Javadocs](https://www.javadoc.io/badge/fun.mike/flapjack-lab.svg)](https://www.javadoc.io/doc/fun.mike/flapjack-lab)

Experimental `flapjack` functionality.

## Usage

### Pipeline

`animals.csv`

```
dog,4,medium
elephant,4,huge
fox,4,medium
ostrich,2,medium
whale,0,gigantic
snake,0,small
```

`Example.java`

```java
Format inputFormat =
    DelimitedFormat.unframed("delimited-animals",
                             "Delimited animals format.",
                             ',',
                             Arrays.asList(Column.string("name"),
                                           Column.integer("legs"),
                                           Column.string("size")));

Format outputFormat =
    FixedWidthFormat("delimited-animals",
                     "Delimited animals format.",
                     Arrays.asList(Field.string("name", 10),
                                   Field.string("size", 10)));
String inputPath = base + "animals.csv";
String outputPath = base + "animals.dat";

Pipeline pipeline = Pipeline.from("animals.csv", inputFormat)
    .map(x -> x.updateString("size", String::toUpperCase))
    .filter(x -> x.getString("size").equals("MEDIUM"))
    .to("medium-sized-animals", outputFormat)
    .build();

PipelineResult result = pipeline.run();

result.isOk();
// => true

result.getInputCount();
// => 6

result.getErrorCount();
// => 0
```

`medium-sized-animals.csv`

```
DOG       MEDIUM    
FOX       MEDIUM    
OSTRICH   MEDIUM    
```

## Build

[![CircleCI](https://circleci.com/gh/mike706574/flapjack-lab.svg?style=svg)](https://circleci.com/gh/mike706574/flapjack-lab)

## Copyright and License

The use and distribution terms for this software are covered by the
[Eclipse Public License 1.0] which can be found in the file
epl-v10.html at the root of this distribution. By using this softwaer
in any fashion, you are agreeing to be bound by the terms of this
license. You must not remove this notice, or any other, from this
software.

[Eclipse Public License 1.0]: http://opensource.org/licenses/eclipse-1.0.php

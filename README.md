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
// Define an input format
Format inputFormat =
    DelimitedFormat.unframed("animals",
                             "A bunch of animals.",
                             ',',
                             Arrays.asList(Column.string("name"),
                                           Column.integer("legs")));

// Define an output format
Format outputFormat =
    FixedWidthFormat("medium-sized-animals",
                     "A bunch of medium-sized animals.",
                     Arrays.asList(Field.string("name", 10),
                                   Field.string("size", 10)));
                                   
// Build a pipeline
Pipeline pipeline = Pipeline.from("animals.csv", inputFormat)
    .filter(x -> x.getString("size").equals("MEDIUM"))
    .map(x -> x.updateString("name", String::toUpperCase))
    .to("medium-sized-animals.csv", outputFormat)
    .build();

// Run it
PipelineResult result = pipeline.run();

// Check for errors
result.isOk();
// => true

result.getErrorCount();
// => 0

// See how many animals went in
result.getInputCount();
// => 6

// See how many medium-sized animals came out
result.getOutputCount();
// => 3
```

`medium-sized-animals.csv`

```
DOG       4         
FOX       4         
OSTRICH   2         
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

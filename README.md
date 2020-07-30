# rmf-codegen

[![Build Status](https://travis-ci.com/vrapio/rmf-codegen.svg?branch=master)](https://travis-ci.com/vrapio/rmf-codegen)

Provides RAML code generators based on [RMF](https://github.com/vrapio/rest-modeling-framework).
The code generators are written in [kotlin](https://kotlinlang.org/).

# Supported languages & frameworks

* Java types for JSON serialization/deserialization of RAML types via [Jackson](https://github.com/FasterXML/jackson)
* Java client for accessing RAML resources via [Spring RestTemplate](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#webmvc-resttemplate)
* TypeScript types generated from RAML types
* TypeScript types for validating JSON payloads via [hapijs/joi](https://github.com/hapijs/joi)

# Install `rmf-codegen` CLI

To install the rmf-codegen cli, run the following command
```
export VRAP_VERSION=1.0.0-20200730063454 && curl -o- -s https://raw.githubusercontent.com/vrapio/rmf-codegen/master/scripts/install.sh | bash
```
You will find a new command available  `rmf-codegen`, you can check that all is good by executing `rmf-codegen -v`

# Build a native executable with GraalVM

You can also build a native executable with the following commands:
```
cd tools/cli-application/
../../gradlew nativeImage
```
The native executable can then be found at `build/graal/rmf-codegen`.
It's currently only tested with Mac OS X.

# Why did we choose kotlin for writing our code generators?

We choose kotlin because of the following features:
* extension methods, which allow us to add methods to our RMF model types
* multin-line template string with embedded expressions, which allows us to keep the templates and the code in the same file
* it integrates very well with our RMF Java types
* it has very good IDE support, which makes writing code generators very easy

These features make developing code generators for different programming languges/frameworks very easy. 
An example of a Java Spring code generator can be found here: [codegen-renderers/src/main/kotlin/io/vrap/codegen/languages/java/client/SpringClientRenderer.kt](https://github.com/vrapio/rmf-codegen/blob/master/codegen-renderers/src/main/kotlin/io/vrap/codegen/languages/java/client/SpringClientRenderer.kt)

# Running tests against your own raml files

Our `TestCodeGenerator` test can be run against a user provided RAML file by setting 
the `TEST_RAML_FILE` environment variable to the file path.

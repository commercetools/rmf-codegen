# rmf-codegen

Provides RAML code generators based on [RMF](https://github.com/vrapio/rest-modeling-framework).
The code generators are written in [kotlin](https://kotlinlang.org/).

# Supported languages & frameworks

* Java types for JSON serialization/deserialization of RAML types via [Jackson](https://github.com/FasterXML/jackson)
* Java client for accessing RAML resources via [Spring RestTemplate](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#webmvc-resttemplate)
* TypeScript types generated from RAML types
* TypeScript types for validating JSON payloads via [hapijs/joi](https://github.com/hapijs/joi)

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
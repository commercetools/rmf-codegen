# RMF-Codegen

RAML API client code generators based on the [RMF (REST Modeling Framework)](https://github.com/vrapio/rest-modeling-framework).
The code generators are written in [kotlin](https://kotlinlang.org/).

## Supported output targets

- `JAVA_CLIENT`:
  - Java types for JSON serialization/deserialization of RAML types via [Jackson](https://github.com/FasterXML/jackson)
  - Java client for accessing RAML resources via [Spring RestTemplate](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#webmvc-resttemplate)
- `TYPESCRIPT_CLIENT`:
  - TypeScript types generated from RAML types
  - TypeScript types for validating JSON payloads via [hapijs/joi](https://github.com/hapijs/joi)
- `CSHARP_CLIENT`
- `PHP_CLIENT`
- `POSTMAN`: Collection file for the [Postman API Client](https://www.postman.com/product/api-client/)
- `RAML_DOC`: Emits RAML files targeted at generating robust and stable API documentation. Fully flattens and resolves types and resources. The output RAML has a fixed canonical filesystem structure and every type or resource file contains all information to fully document it. It's used by the [commercetools-docs-kit](https://github.com/commercetools/commercetools-docs-kit/tree/master/packages/gatsby-theme-api-docs)

## Install `rmf-codegen` CLI

To install the rmf-codegen cli, run the following command

```
curl -o- -s https://raw.githubusercontent.com/vrapio/rmf-codegen/main/scripts/install.sh | bash
```

You will find a new command available `rmf-codegen`, you can check that all is good by executing `rmf-codegen -v`

## NPM wrapper package

The [`commercetools-docs-kit`](https://github.com/commercetools/commercetools-docs-kit) provides an NPM package that automates the download of the JAR and provides the `rmf-codegen` command to JavaScript projects without global installation. (find at [NPM](https://www.npmjs.com/login?next=/package/@commercetools-docs/rmf-codegen))

## Docker

Docker images for the code generator are available in the Github Container Registry

```
docker pull docker.pkg.github.com/commercetools/rmf-codegen/codegen:latest
```
## Usage

General Usage:

```
Usage: rmf-codegen [-hv] [COMMAND]
Allows to validate RAML files and generate code from them
  -h, --help                display this help message
  -v, --version             print version information and exit
Commands:
  generate                  Generate source code from a RAML specification.
  verify                    Allows to verify if a raml spec is valid.
```

Generating Client SDKs or normalized RAML for documentation:

```
Usage: rmf-codegen generate [-hvw] [-b=<basePackageName>]
                            [-c=<clientPackageName>] [-m=<modelPackageName>]
                            -o=<outputFolder> [-s=<sharedPackage>] -t=<target>
                            <ramlFileLocation>
Generate source code from a RAML specification.
  <ramlFileLocation>        Api file location
  -b, --base-package=<basePackageName>
                            The base package, this package in case the model or
                            client models aren't provided
  -c, --client-package=<clientPackageName>
                            The client package, This will be used as the package
                            for the client stub.
  -h, --help                display this help message
  -m, --model-package=<modelPackageName>
                            The models package, this will be used as the model
                            package in the generated code.
  -o, --output-folder=<outputFolder>
                            Output folder for generated files.
  -s, --shared-package=<sharedPackage>
                            The shared package to be used for the generated code.
  -t, --target=<target>     Specifies the code generation target
                            Valid values: JAVA_CLIENT, TYPESCRIPT_CLIENT,
                            CSHARP_CLIENT, PHP_CLIENT, PHP_BASE, PHP_TEST,
                            POSTMAN, RAML_DOC
  -v, --verbose             If set, this would move the verbosity level to debug.
  -w, --watch               Watches the files for changes

```

Validating a RAML API:

```
Usage: rmf-codegen verify [-hw] <ramlFileLocation>
Allows to verify if a RAML spec is valid.
  <ramlFileLocation>        Api file location
  -h, --help                display this help message
  -w, --watch               Watches the files for changes
```

## Development

### Build the "fat jar"

A single "fat jar" is built with the following commands:

```sh
cd tools/cli-application/
../../gradlew build
```

The JAR can then be found at `./rmf-gen.jar`

### Build a native executable with GraalVM

A native executable is built with the following commands:

```sh
cd tools/cli-application/
../../gradlew nativeImage
```

The native executable can then be found at `./tools/cli-application/build/graal/rmf-codegen`.
It's currently only tested with Mac OS X. It emits error messages but the functionality works.

The graalvm native image configuration has to be re-generated regulary using the `tools/cli-application/generate-graal-config.sh` script.

### Why did we choose kotlin for writing our code generators?

We choose kotlin because of the following features:

- extension methods, which allow us to add methods to our RMF model types
- multi-line template string with embedded expressions, which allows us to keep the templates and the code in the same file
- it integrates very well with our RMF Java types
- it has very good IDE support, which makes writing code generators very easy

These features make developing code generators for different programming languges/frameworks very easy.
An example of a Java Spring code generator can be found here: [codegen-renderers/src/main/kotlin/io/vrap/codegen/languages/java/client/SpringClientRenderer.kt](https://github.com/vrapio/rmf-codegen/blob/main/codegen-renderers/src/main/kotlin/io/vrap/codegen/languages/java/client/SpringClientRenderer.kt)

# Running tests against your own RAML files

Our `TestCodeGenerator` test can be run against a user provided RAML file by setting
the `TEST_RAML_FILE` environment variable to the file path.

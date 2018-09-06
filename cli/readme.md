# Raml CLI

### Building project

This is the raml generator CLI addition

* in order to build it run `./gradlew distZip -p cli` at the root project to generate the application archive

* you will find a cli.zip generated which contain the generated program

* if you run ./cli generate help you will get the cli manual
```
NAME
        cli generate - generate raml stub

SYNOPSIS
        vrap generate [-out <outputFolder>] [-p <basePackageName>] [--]
                <apiFileLocation>

OPTIONS
        -out <outputFolder>
            Output folder for generated files

        -p <basePackageName>
            The base package to be used for the generated sdk

        --
            This option can be used to separate command-line options from the
            list of argument, (useful when arguments might be mistaken for
            command-line options

        <apiFileLocation>
            Api file location

```

### Use example

`./cli generate api.raml`

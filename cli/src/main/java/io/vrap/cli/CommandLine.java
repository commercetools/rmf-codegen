package io.vrap.cli;

import io.airlift.airline.*;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfigBuilder;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorComponent;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorModule;
import org.eclipse.emf.common.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class CommandLine {

    final static Logger LOGGER = LoggerFactory.getLogger(CommandLine.class);

    public static void main(String... args){
        Cli<Runnable> commandParser = creatCliParser();
        commandParser.parse(args).run();
    }

    public static Cli<Runnable> creatCliParser(){
        final Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("cli")
                .withDescription("code generator for raml")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, StubGenerator.class);


        final Cli<Runnable> commandParser = builder.build();
        return commandParser;
    }

    @Command(name = "generate", description = "generate raml stub")
    public static class StubGenerator implements Runnable
    {

        @Option(name = "-p",description = "The base package to be used for the generated sdk")
        public String basePackageName = "io.vrap";

        @Option(name = "-out", description = "Output folder for generated files")
        public File outputFolder;

        @Arguments(description = "Api file location",required = true)
        public File apiFileLocation;


        @Override
        public String toString() {
            return "StubGenerator{" +
                    ", basePackageName='" + basePackageName + '\'' +
                    ", outputFolder=" + outputFolder +
                    ", apiFileLocation=" + apiFileLocation +
                    '}';
        }

        @Override
        public void run() {

            final GeneratorConfigBuilder builder = new GeneratorConfigBuilder();
            try {
                builder.ramlFileLocation(URI.createFileURI(apiFileLocation.getCanonicalPath()));

            } catch (final IOException e) {
                throw new RuntimeException(e);
            }

            Optional.ofNullable(basePackageName).map(builder::packagePrefix).orElse(null);
            Optional.ofNullable(outputFolder).map(File::toPath).map(builder::outputFolder).orElse(null);


            final GeneratorComponent generatorComponent = new GeneratorComponent(GeneratorModule.of(builder.build()));
            generatorComponent.getStCodeGenerator()
                    .generateClasses()
                    .flattenAsFlowable(GenerationResult::getGeneratedFiles)
                    .subscribe(
                            path -> LOGGER.info("generated file " + path),
                            error -> LOGGER.error("Error encountered while generating the client stub", error)
                    );

        }
    }

}

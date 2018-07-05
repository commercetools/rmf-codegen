package io.vrap.rmf.codegen.plugin;

import io.vrap.rmf.codegen.common.generator.client.ClientCodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfigBuilder;
import io.vrap.rmf.codegen.common.generator.doc.DefaultJavaDocProcessor;
import io.vrap.rmf.codegen.common.generator.injection.DaggerGeneratorComponent;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorComponent;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorModule;
import io.vrap.rmf.codegen.common.generator.model.codegen.BeanGenerator;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class RamlCodeGeneratorTask extends DefaultTask {

    private final GeneratorConfigBuilder builder = new GeneratorConfigBuilder().javaDocProcessor(new DefaultJavaDocProcessor());
    private static final Logger LOGGER = Logging.getLogger(RamlCodeGeneratorTask.class);

    @Input
    @Nonnull
    public RamlCodeGeneratorTask setRamlFileLocation(final File ramlFilePath) {
        builder.ramlFileLocation(ramlFilePath.toPath());
        return RamlCodeGeneratorTask.this;
    }

    @Input
    public RamlCodeGeneratorTask setOutputFolder(final File outputFolder) {
        builder.outputFolder(outputFolder.toPath());
        return RamlCodeGeneratorTask.this;
    }

    @Input
    public RamlCodeGeneratorTask setPackagePrefix(final String packagePrefix) {
        builder.packagePrefix(packagePrefix);
        return RamlCodeGeneratorTask.this;
    }


    @Input
    public RamlCodeGeneratorTask setCustomTypeMapping(final Map<String, String> customTypeMapping) {
        builder.customTypeMapping(customTypeMapping);
        return RamlCodeGeneratorTask.this;
    }


    @TaskAction
    void generateRamlStub() {
        final GeneratorComponent generatorComponent = DaggerGeneratorComponent
                .builder()
                .generatorModule(GeneratorModule.of(builder.build(), BeanGenerator::new, ClientCodeGenerator::new))
                .build();
        generatorComponent.getMasterCodeGenerator()
                .generateStub()
                .flattenAsFlowable(GenerationResult::getGeneratedFiles)
                .subscribe(
                        path -> LOGGER.info("generated file " + path),
                        error -> LOGGER.error("Error encountered while generating the client stub", error)
                );
    }
}
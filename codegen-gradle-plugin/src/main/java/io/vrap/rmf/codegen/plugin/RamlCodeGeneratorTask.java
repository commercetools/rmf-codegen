package io.vrap.rmf.codegen.plugin;

import io.vrap.rmf.codegen.common.generator.client.spring.SpringClientCodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfigBuilder;
import io.vrap.rmf.codegen.common.generator.injection.DaggerGeneratorComponent;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorComponent;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorModule;
import io.vrap.rmf.codegen.common.generator.model.codegen.BeanGenerator;
import org.eclipse.emf.common.util.URI;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class RamlCodeGeneratorTask extends DefaultTask {


    private static final Logger LOGGER = Logging.getLogger(RamlCodeGeneratorTask.class);


    private File ramlFileLocation;
    private File outputFolder;
    private String packagePrefix;
    private Map<String, String> customTypeMapping;

    public RamlCodeGeneratorTask() {
    }

    public void setRamlFileLocation(File ramlFileLocation) {
        this.ramlFileLocation = ramlFileLocation;
    }

    public void setCustomTypeMapping(Map<String, String> customTypeMapping) {
        this.customTypeMapping = customTypeMapping;
    }

    public void setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    @InputFile
    public File getRamlFileLocation() {
        return ramlFileLocation;
    }

    @OutputDirectory
    @org.gradle.api.tasks.Optional
    public File getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(File outputFolder) {
        this.outputFolder = outputFolder;
    }

    @org.gradle.api.tasks.Optional
    @Input
    public String getPackagePrefix() {
        return packagePrefix;
    }




    @org.gradle.api.tasks.Optional
    @Input
    public Map<String, String> getCustomTypeMapping() {
        return customTypeMapping;
    }



    @Override
    public String toString() {
        return "RamlCodeGeneratorTask{" +
                "ramlFileLocation=" + ramlFileLocation +
                ", outputFolder=" + outputFolder +
                ", packagePrefix='" + packagePrefix + '\'' +
                ", customTypeMapping=" + customTypeMapping +
                '}';
    }

    @TaskAction
    void generateRamlStub() {

        final GeneratorConfigBuilder builder = new GeneratorConfigBuilder();
        try {
            builder.ramlFileLocation(URI.createFileURI(ramlFileLocation.getCanonicalPath()));

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        Optional.ofNullable(packagePrefix).map(builder::packagePrefix).orElse(null);
        Optional.ofNullable(outputFolder).map(File::toPath).map(builder::outputFolder).orElse(null);
        Optional.ofNullable(customTypeMapping).map(builder::customTypeMapping).orElse(null);


        final GeneratorComponent generatorComponent = DaggerGeneratorComponent
                .builder()
                .generatorModule(GeneratorModule.of(builder.build(), BeanGenerator::new, SpringClientCodeGenerator::new))
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
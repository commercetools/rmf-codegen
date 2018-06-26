package io.vrap.rmf.codegen.common.generator.injection;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Flowable;
import io.vrap.rmf.codegen.common.generator.core.CodeGeneratorFactory;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.raml.model.RamlDiagnostic;
import io.vrap.rmf.raml.model.RamlModelBuilder;
import io.vrap.rmf.raml.model.RamlModelResult;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.types.AnyType;
import org.eclipse.emf.common.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Module
public class GeneratorModule {

    private final Logger LOGGER = LoggerFactory.getLogger(GeneratorModule.class);
    private final GeneratorConfig generatorConfig;
    private final JavaDocProcessor javaDocProcessor;
    private final List<CodeGeneratorFactory> codeGenerators;


    GeneratorModule(final GeneratorConfig generatorConfig, final JavaDocProcessor javaDocProcessor, final List<CodeGeneratorFactory> codeGenerators) {
        Objects.requireNonNull(generatorConfig);
        Objects.requireNonNull(codeGenerators);
        this.generatorConfig = generatorConfig;
        this.codeGenerators = codeGenerators;
        this.javaDocProcessor = javaDocProcessor;
    }

    public static GeneratorModule of(final GeneratorConfig generatorConfig, JavaDocProcessor javaDocProcessor, final List<CodeGeneratorFactory> codeGenerators) {
        Objects.requireNonNull(generatorConfig);
        Objects.requireNonNull(codeGenerators);
        return new GeneratorModule(generatorConfig, javaDocProcessor, codeGenerators);
    }

    public static GeneratorModule of(final GeneratorConfig generatorConfig, final JavaDocProcessor javaDocProcessor, final CodeGeneratorFactory codeGenerators) {
        Objects.requireNonNull(generatorConfig);
        Objects.requireNonNull(codeGenerators);
        return new GeneratorModule(generatorConfig, javaDocProcessor, Arrays.asList(codeGenerators));
    }

    public GeneratorConfig getGeneratorConfig() {
        return generatorConfig;
    }

    @Provides
    @Named(GeneratorConfig.RAML_FILE_LOCATION)
    public Path getRamlFileLocation() {
        return getGeneratorConfig().getRamlFileLocation();
    }


    @Provides
    @Named(GeneratorConfig.OUTPUT_FOLDER)
    public Path getOutputFolder() {
        return generatorConfig.getOutputFolder();
    }

    @Provides
    @Named(GeneratorConfig.PACKAGE_PREFIX)
    public String getPackagePrefix() {
        return generatorConfig.getPackagePrefix();
    }

    @Provides
    public JavaDocProcessor getJavaDocProcessor() {
        return javaDocProcessor;
    }


    @Provides
    public List<CodeGeneratorFactory> getCodeGeneratorFactories() {
        return codeGenerators;
    }

    @Provides
    public Api provideRamlModel(@Named(GeneratorConfig.RAML_FILE_LOCATION) final Path ramlFilePath) {
        final URI fileURI = URI.createFileURI(ramlFilePath.toString());
        final RamlModelResult<Api> modelResult = new RamlModelBuilder().buildApi(fileURI);
        final List<RamlDiagnostic> validationResults = modelResult.getValidationResults();
        if (!validationResults.isEmpty()) {
            validationResults.stream().forEach(validationResult -> {
                LOGGER.warn("Error encountered while checking Raml API " + validationResult.toString());
            });
            throw new RuntimeException("Error while paring raml input");
        }
        return modelResult.getRootObject();
    }

    @Provides
    @Singleton
    public Flowable<AnyType> provideRamlEntitiesObjects(final Api ramlApi) {
        final Flowable<AnyType> resultFlow = Flowable.fromIterable(ramlApi.getUses())
                .flatMapIterable(libraryUse -> libraryUse.getLibrary().getTypes());
        return resultFlow;
    }

}

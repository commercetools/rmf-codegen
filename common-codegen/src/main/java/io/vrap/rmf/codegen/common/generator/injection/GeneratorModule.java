package io.vrap.rmf.codegen.common.generator.injection;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.reactivex.Flowable;
import io.vrap.rmf.codegen.common.generator.core.CodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.CodeGeneratorFactory;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.codegen.common.generator.extensions.types.AnyTypeExtension;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.raml.model.RamlDiagnostic;
import io.vrap.rmf.raml.model.RamlModelBuilder;
import io.vrap.rmf.raml.model.RamlModelResult;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.resources.Resource;
import io.vrap.rmf.raml.model.types.AnyType;
import org.eclipse.emf.common.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GeneratorModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorModule.class);
    private final GeneratorConfig generatorConfig;
    private final List<CodeGeneratorFactory> codeGeneratorFactories;

    private GeneratorModule(final GeneratorConfig generatorConfig, final List<CodeGeneratorFactory> codeGeneratorFactories) {
        Objects.requireNonNull(generatorConfig);
        this.generatorConfig = generatorConfig;
        this.codeGeneratorFactories = codeGeneratorFactories;

    }

    public static GeneratorModule of(final GeneratorConfig generatorConfig, final List<CodeGeneratorFactory> codeGeneratorFactories) {
        Objects.requireNonNull(generatorConfig);
        Objects.requireNonNull(codeGeneratorFactories);
        return new GeneratorModule(generatorConfig, codeGeneratorFactories);
    }
    public static GeneratorModule of(final GeneratorConfig generatorConfig, final CodeGeneratorFactory... codeGeneratorFactories) {
        Objects.requireNonNull(generatorConfig);
        Objects.requireNonNull(codeGeneratorFactories);
        return new GeneratorModule(generatorConfig, Arrays.asList(codeGeneratorFactories));
    }

    @Provides
    public GeneratorConfig getGeneratorConfig() {
        return generatorConfig;
    }

    @Provides
    @Named(GeneratorConfig.RAML_FILE_LOCATION)
    public URI getRamlFileLocation() {
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
        return getGeneratorConfig().getJavaDocProcessor();
    }


    @Provides
    public List<CodeGeneratorFactory> getCodeGeneratorFactories() {
        return codeGeneratorFactories;
    }

    @Provides
    public Map<String,String> getCustomTypeMapping() {
        return getGeneratorConfig().getCustomTypeMapping();
    }

    @Provides
    @Singleton
    public Api provideRamlModel(@Named(GeneratorConfig.RAML_FILE_LOCATION) final URI fileURI) {
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
    public List<AnyType> provideRamlEntitiesObjects(final Api ramlApi) {
        final List<AnyType> resultFlow = Flowable.fromIterable(ramlApi.getUses())
                .flatMapIterable(libraryUse -> libraryUse.getLibrary().getTypes())
                .toList()
                .blockingGet();
        return resultFlow;
    }

    @Provides
    @Singleton
    public List<Resource> getAllReources(final Api ramlApi) {
        return ramlApi.getAllContainedResources();
    }

    @Provides
    @Singleton
    public List<CodeGenerator> getCodeGenerators(final GeneratorConfig generatorConfig, final Api api ) {
        return getCodeGeneratorFactories().stream().map(codeGeneratorFactory -> codeGeneratorFactory.createCodeGenerator(generatorConfig, api)).collect(Collectors.toList());
    }

    @Provides
    @Singleton
    public TypeNameSwitch getTypeNameSwitch(GeneratorConfig generatorConfig){
        return TypeNameSwitch.of(generatorConfig);
    }

}

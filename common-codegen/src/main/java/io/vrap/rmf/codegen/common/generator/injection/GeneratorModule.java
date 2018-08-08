package io.vrap.rmf.codegen.common.generator.injection;


import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.reactivex.Flowable;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapper;
import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapperFactory;
import io.vrap.rmf.raml.model.RamlDiagnostic;
import io.vrap.rmf.raml.model.RamlModelBuilder;
import io.vrap.rmf.raml.model.RamlModelResult;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.resources.Resource;
import io.vrap.rmf.raml.model.types.AnyType;
import org.eclipse.emf.common.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

public class GeneratorModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorModule.class);
    private final GeneratorConfig generatorConfig;

    private GeneratorModule(final GeneratorConfig generatorConfig) {
        Objects.requireNonNull(generatorConfig);
        this.generatorConfig = generatorConfig;

    }

    public static GeneratorModule of(final GeneratorConfig generatorConfig) {
        Objects.requireNonNull(generatorConfig);
        return new GeneratorModule(generatorConfig);
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
    public TypeNameSwitch getTypeNameSwitch(GeneratorConfig generatorConfig){
        return TypeNameSwitch.of(generatorConfig);
    }

    @Provides
    @Singleton
    public List<ExtensionMapper> getAllExtensionMappers(Injector injector){
        List<ExtensionMapper> result =  Flowable.fromIterable(ServiceLoader.load(ExtensionMapperFactory.class))
                .map(ExtensionMapperFactory::create)
                .doOnNext(extensionMapper -> injector.injectMembers(extensionMapper.getExtension()))
                .toList()
                .blockingGet();

        if(result.isEmpty()){
            LOGGER.warn("no Extension mapper detected");
        }
        return result;
    }

}

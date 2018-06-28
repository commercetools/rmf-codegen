package io.vrap.rmf.codegen.common.generator.core;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.types.AnyType;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public abstract class CodeGenerator {

    private final GeneratorConfig generatorConfig;

    private final Flowable<AnyType> ramlObjects;

    private final Api api;

    private final TypeNameSwitch typeNameSwitch;


    public CodeGenerator(GeneratorConfig generatorConfig, Api api) {
        Objects.requireNonNull(generatorConfig);
        this.generatorConfig = generatorConfig;
        this.api = api;
        this.ramlObjects = Flowable.fromIterable(api.getUses()).flatMapIterable(libraryUse -> libraryUse.getLibrary().getTypes());
        this.typeNameSwitch = TypeNameSwitch.of(getPackagePrefix(), generatorConfig.getCustomTypeMapping());
    }

    public final Api getApi() {
        return api;
    }

    public final String getPackagePrefix() {
        return getGeneratorConfig().getPackagePrefix();
    }

    public final Path getOutputFolder() {
        return getGeneratorConfig().getOutputFolder();
    }

    public final JavaDocProcessor getJavaDocProcessor() {
        return getGeneratorConfig().getJavaDocProcessor();
    }

    public final Flowable<AnyType> getRamlObjects() {
        return ramlObjects;
    }

    public final Map<String, String> getCustomTypeMapping() {
        return getGeneratorConfig().getCustomTypeMapping();
    }

    public final TypeNameSwitch getTypeNameSwitch() {
        return typeNameSwitch;
    }

    public abstract  Single<GenerationResult> generateStub();

    private final GeneratorConfig getGeneratorConfig() {
        return generatorConfig;
    }
}

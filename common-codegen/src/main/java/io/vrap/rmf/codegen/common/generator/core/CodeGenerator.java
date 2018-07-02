package io.vrap.rmf.codegen.common.generator.core;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.types.AnyType;

import java.util.Objects;

public abstract class CodeGenerator extends ConfigDecoratorBase {


    private final Flowable<AnyType> ramlObjects;

    private final Api api;

    public CodeGenerator(GeneratorConfig generatorConfig, Api api) {
        super(generatorConfig);
        Objects.requireNonNull(api);
        this.api = api;
        this.ramlObjects = Flowable.fromIterable(api.getUses()).flatMapIterable(libraryUse -> libraryUse.getLibrary().getTypes());

    }

    public final Api getApi() {
        return api;
    }

    public final Flowable<AnyType> getRamlObjects() {
        return ramlObjects;
    }

    public abstract Single<GenerationResult> generateStub();


}

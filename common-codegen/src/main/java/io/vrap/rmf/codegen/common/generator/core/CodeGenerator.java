package io.vrap.rmf.codegen.common.generator.core;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.raml.model.types.AnyType;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public abstract class CodeGenerator {

    private final String packagePrefix;

    private final Path outputFolder;

    private final JavaDocProcessor javaDocProcessor;

    private final Flowable<AnyType> ramlObjects;

    private final Map<String, String> customTypeMapping = Collections.EMPTY_MAP;

    private final TypeNameSwitch typeNameSwitch;


    public CodeGenerator(String packagePrefix, Path outputFolder, JavaDocProcessor javaDocProcessor, Flowable<AnyType> ramlObjects) {
        this.packagePrefix = packagePrefix;
        this.outputFolder = outputFolder;
        this.javaDocProcessor = javaDocProcessor;
        this.ramlObjects = ramlObjects;
        this.typeNameSwitch = TypeNameSwitch.of(getPackagePrefix(), customTypeMapping);
    }

    public String getPackagePrefix() {
        return packagePrefix;
    }

    public Path getOutputFolder() {
        return outputFolder;
    }

    public JavaDocProcessor getJavaDocProcessor() {
        return javaDocProcessor;
    }

    public Flowable<AnyType> getRamlObjects() {
        return ramlObjects;
    }

    public Map<String, String> getCustomTypeMapping() {
        return customTypeMapping;
    }

    public TypeNameSwitch getTypeNameSwitch() {
        return typeNameSwitch;
    }

    public abstract Single<GenerationResult> generateStub();

}

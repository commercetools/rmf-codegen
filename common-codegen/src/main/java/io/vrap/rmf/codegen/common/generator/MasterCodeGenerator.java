package io.vrap.rmf.codegen.common.generator;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.core.CodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.CodeGeneratorFactory;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.raml.model.types.AnyType;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.List;

@Singleton
public class MasterCodeGenerator {

    private final Flowable<AnyType> ramlObjects;
    private final List<CodeGeneratorFactory> codeGenerators;
    private final String packagePrefix;
    private final JavaDocProcessor javaDocProcessor;
    @Inject
    Path outputFolder;


    @Inject
    public MasterCodeGenerator(@Named(GeneratorConfig.PACKAGE_PREFIX) final String packagePrefix,
                               final @Named(GeneratorConfig.OUTPUT_FOLDER) Path outputFolder,
                               final Flowable<AnyType> ramlObjects,
                               final List<CodeGeneratorFactory> codeGenerators,
                               final JavaDocProcessor javaDocProcessor) {
        this.outputFolder = outputFolder;
        this.ramlObjects = ramlObjects;
        this.codeGenerators = codeGenerators;
        this.packagePrefix = packagePrefix;
        this.javaDocProcessor = javaDocProcessor;
    }


    public Single<GenerationResult> generateStub() {
        return Flowable.fromIterable(codeGenerators)
                .map(codeGeneratorFactory -> codeGeneratorFactory.createCodeGenerator(packagePrefix, outputFolder, javaDocProcessor, ramlObjects))
                .flatMapSingle(CodeGenerator::generateStub)
                .flatMapIterable(generationResult -> generationResult.getGeneratedFiles())
                .toList()
                .map(paths -> GenerationResult.of(paths));
    }
}

package io.vrap.rmf.codegen.common.generator;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.core.CodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.CodeGeneratorFactory;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.types.AnyType;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Singleton
public class MasterCodeGenerator {

    private final List<CodeGenerator> codeGenerators;


    @Inject
    public MasterCodeGenerator(final List<CodeGenerator> codeGenerators) {
        this.codeGenerators = codeGenerators;

    }
    public Single<GenerationResult> generateStub() {
        return Flowable.fromIterable(codeGenerators)
                .flatMapSingle(CodeGenerator::generateStub)
                .flatMapIterable(GenerationResult::getGeneratedFiles)
                .toList()
                .map(GenerationResult::of);
    }
}

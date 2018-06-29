package io.vrap.rmf.codegen.common.generator.client;

import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.core.CodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.resources.Resource;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ClientCodeGenerator extends CodeGenerator {


    public ClientCodeGenerator(GeneratorConfig generatorConfig, Api api) {
        super(generatorConfig, api);
    }

    @Override
    public Single<GenerationResult> generateStub() {

        return Single.just(GenerationResult.empty());
    }

}

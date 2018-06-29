package io.vrap.rmf.codegen.common.generator.core;

import io.reactivex.Flowable;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.types.AnyType;

import java.nio.file.Path;
import java.util.Map;

@FunctionalInterface
public interface CodeGeneratorFactory {

    CodeGenerator createCodeGenerator(GeneratorConfig config, Api api);

}

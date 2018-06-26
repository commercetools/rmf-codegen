package io.vrap.rmf.codegen.common.generator.core;

import io.reactivex.Flowable;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.raml.model.types.AnyType;

import java.nio.file.Path;

@FunctionalInterface
public interface CodeGeneratorFactory {

    CodeGenerator createCodeGenerator(String packagePrefix, Path outputFolder, JavaDocProcessor javaDocProcessor, Flowable<AnyType> ramlObjects);

}

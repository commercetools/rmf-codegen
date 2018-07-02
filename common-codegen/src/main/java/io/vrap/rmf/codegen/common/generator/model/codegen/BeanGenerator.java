package io.vrap.rmf.codegen.common.generator.model.codegen;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.core.CodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.types.AnyType;
import io.vrap.rmf.raml.model.types.ObjectType;
import io.vrap.rmf.raml.model.types.StringType;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.eclipse.emf.ecore.EObject;

import javax.tools.JavaFileObject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static io.vrap.rmf.codegen.common.generator.util.CodeGeneratorUtil.getObjectPackage;

public class BeanGenerator extends CodeGenerator {

    private final BaseClass baseClass;
    private final TransformerTypeSwitch transformerTypeSwitch = new TransformerTypeSwitch();


    public BeanGenerator(GeneratorConfig generatorConfig, Api api) {
        super(generatorConfig, api);
        baseClass=new BaseClass(this);
    }

    @Override
    public Single<GenerationResult> generateStub() {

        final Single<GenerationResult> generationResult = getRamlObjects()

                .flatMapMaybe(this::transformToJavaFile)
                .concatWith(Single.just(getJavaFileForBase()))
                .doOnNext(javaFile -> javaFile.writeTo(getOutputFolder()))
                .map(JavaFile::toJavaFileObject)
                .map(javaFile -> getPath(javaFile, getOutputFolder()))
                .toList()
                .map(GenerationResult::of);

        return generationResult;
    }


    public Maybe<JavaFile> transformToJavaFile(final AnyType object) {
        return transformerTypeSwitch.doSwitch(object).map(typeSpec -> JavaFile.builder(getObjectPackage(getPackagePrefix(), object), typeSpec).build());

    }

    private JavaFile getJavaFileForBase() {
        return JavaFile.builder(baseClass.getClassName().packageName(), baseClass.getTypeSpec()).build();
    }


    private Path getPath(JavaFileObject javaFile, Path outputFolder) {
        return Paths.get(outputFolder.toString(), javaFile.getName());
    }


    private class TransformerTypeSwitch extends TypesSwitch<Maybe<TypeSpec>>{

        private ObjectTypeTransformer objectTypeTransformer = new ObjectTypeTransformer(BeanGenerator.this);
        private StringTypeTransformer stringTypeTransformer = new StringTypeTransformer(BeanGenerator.this);

        @Override
        public Maybe<TypeSpec> doSwitch(EObject eObject) {
            if((eObject instanceof AnyType)&&(getCustomTypeMapping().get(((AnyType)eObject).getName()) != null) ){
                return Maybe.empty();
            }
            return Optional.ofNullable(super.doSwitch(eObject)).orElse(Maybe.error(new RuntimeException("No TypeSpec transformer found for " + eObject)));
        }

        @Override
        public Maybe<TypeSpec> caseStringType(StringType object) {
            return Maybe.just(stringTypeTransformer.toTypeSpec(object));
        }

        @Override
        public Maybe<TypeSpec> caseObjectType(ObjectType object) {
            return Maybe.just(objectTypeTransformer.toTypeSpec(object));
        }
    }

}

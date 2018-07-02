package io.vrap.rmf.codegen.common.generator.model.codegen;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.util.CodeGeneratorUtil;
import io.vrap.rmf.raml.model.types.StringInstance;
import io.vrap.rmf.raml.model.types.StringType;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;

import static io.vrap.rmf.codegen.common.generator.util.CodeGeneratorUtil.getClassName;
import static io.vrap.rmf.codegen.common.generator.util.CodeGeneratorUtil.getGeneratedAnnotation;

public class StringTypeTransformer extends TypeTransformer<StringType> {

    private final BaseClass baseClass;

    public StringTypeTransformer(GeneratorConfig generatorConfig) {
        super(generatorConfig);
        baseClass = new BaseClass(this);
    }

    @Override
    public TypeSpec toTypeSpec(StringType stringType) {

        if (!stringType.getEnum().isEmpty()) {
            TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(getClassName(getPackagePrefix(), stringType))
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(getGeneratedAnnotation(this));
            stringType.getEnum()
                    .stream()
                    .map(StringInstance.class::cast)
                    .map(StringInstance::getValue)
                    .forEach(value-> addConstantToEnumConstant(enumBuilder,value));
            enumBuilder.addJavadoc(getJavaDocProcessor().markDownToJavaDoc(stringType));
            return enumBuilder.build();
        } else {
            TypeSpec typeSpec = TypeSpec.classBuilder(getClassName(getPackagePrefix(), stringType))
                    .addAnnotation(getGeneratedAnnotation(this))
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(baseClass.getClassName())
                    .addJavadoc(getJavaDocProcessor().markDownToJavaDoc(stringType))
                    .build();
            return typeSpec;
        }
    }

    private void addConstantToEnumConstant(TypeSpec.Builder enumBuilder, final String enumValue) {

        TypeSpec anootationSpec = TypeSpec.anonymousClassBuilder("")
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class).addMember("value", "$S",enumValue).build())
                .build();

        enumBuilder.addEnumConstant(CodeGeneratorUtil.toEnumValueName(enumValue), anootationSpec);

    }
}

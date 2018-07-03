package io.vrap.rmf.codegen.common.generator.model.codegen;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.*;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.model.codegen.base.BaseClass;
import io.vrap.rmf.codegen.common.generator.util.CodeGeneratorUtil;
import io.vrap.rmf.raml.model.types.StringInstance;
import io.vrap.rmf.raml.model.types.StringType;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.Optional;

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

            final MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addParameter(ClassName.get(String.class),"jsonName",Modifier.FINAL)
                    .addModifiers(Modifier.PRIVATE)
                    .addCode("this.jsonName = jsonName;\n")
                    .build();

            final MethodSpec getJsonName = MethodSpec.methodBuilder("getJsonName")
                    .returns(String.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addCode(" return jsonName;\n")
                    .build();

            final MethodSpec findEnumViaJsonName = MethodSpec.methodBuilder("findEnumViaJsonName")
                    .returns(ParameterizedTypeName.get(ClassName.get(Optional.class),getClassName(getPackagePrefix(), stringType)))
                    .addParameter(ParameterSpec.builder(String.class,"jsonName").build())
                    .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                    .addCode("return $T.stream(values()).filter(t -> t.getJsonName().equals(jsonName)).findFirst();\n",Arrays.class)
                    .build();

            TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(getClassName(getPackagePrefix(), stringType))
                    .addModifiers(Modifier.PUBLIC)
                    .addField(FieldSpec.builder(ClassName.get(String.class),"jsonName",Modifier.FINAL).build())
                    .addMethod(constructor)
                    .addMethod(getJsonName)
                    .addMethod(findEnumViaJsonName)
                    .addAnnotation(getGeneratedAnnotation(this));

            stringType.getEnum()
                    .stream()
                    .map(StringInstance.class::cast)
                    .map(StringInstance::getValue)
                    .forEach(value-> addJsonNameToEnum(enumBuilder,value));

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

    private void addJsonNameToEnum(TypeSpec.Builder enumBuilder, final String enumValue) {

        TypeSpec anootationSpec = TypeSpec.anonymousClassBuilder("$S",enumValue)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S",enumValue).build())
                .build();

        enumBuilder.addEnumConstant(CodeGeneratorUtil.toEnumValueName(enumValue), anootationSpec);

    }
}

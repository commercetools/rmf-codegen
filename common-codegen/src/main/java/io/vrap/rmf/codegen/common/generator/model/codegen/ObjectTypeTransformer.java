package io.vrap.rmf.codegen.common.generator.model.codegen;

import com.fasterxml.jackson.annotation.*;
import com.squareup.javapoet.*;
import io.reactivex.Flowable;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.raml.model.types.ObjectType;
import io.vrap.rmf.raml.model.types.Property;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vrap.rmf.codegen.common.generator.util.CodeGeneratorUtil.*;

public class ObjectTypeTransformer extends TypeTransformer<ObjectType> {

    private final BaseClass baseClass;

    public ObjectTypeTransformer(GeneratorConfig generatorConfig) {
        super(generatorConfig);
        baseClass = new BaseClass(this);
    }


    @Override
    public TypeSpec toTypeSpec(ObjectType type) {
        return buildTypeSpecForObjectType(type);
    }

    private TypeSpec buildTypeSpecForObjectType(final ObjectType objectType) {


        // object.getPropertyType() return the supper type
        final ClassName className = getClassName(getPackagePrefix(), objectType);
        final TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addAnnotation(getGeneratedAnnotation(this))
                .addJavadoc(getJavaDocProcessor().markDownToJavaDoc(objectType))
                .addModifiers(Modifier.PUBLIC);

        getDiscriminatorAnnotations(objectType).forEach(typeSpecBuilder::addAnnotation);

        //Add super interfaces
        Optional.of(
                Optional.ofNullable(objectType.getType())
                        .map(anyType -> getClassName(getPackagePrefix(), anyType)).orElse(baseClass.getClassName())
        )
                .map(typeSpecBuilder::superclass)
                .orElse(null);

        Flowable.fromIterable(objectType.getProperties())
                .doOnNext(property -> typeSpecBuilder.addField(getFieldSpec(property)))
                .doOnNext(property -> typeSpecBuilder.addMethod(getFieldGetter(property)))
                .doOnNext(property -> typeSpecBuilder.addMethod(getFieldSetter(property)))
                .blockingSubscribe();


        return typeSpecBuilder.build();
    }

    private FieldSpec getFieldSpec(final Property property) {
        FieldSpec.Builder builder;
        if (property.getName().startsWith("/")) {
            final TypeName valueType = getTypeNameSwitch().doSwitch(property.getType());
            builder =  FieldSpec.builder(
                    ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), valueType), "values", Modifier.PRIVATE)
                    .initializer("new $T()",HashMap.class)
                    .addAnnotation(JsonAnySetter.class);
        } else {
            final TypeName valueType = getTypeNameSwitch().doSwitch(property.getType());
            builder = FieldSpec.builder(valueType, property.getName(), Modifier.PRIVATE);
        }
        if(property.getRequired()){
            builder.addAnnotation(NotNull.class);
        }

        return builder.build();


    }

    private MethodSpec getFieldSetter(final Property property) {
        final FieldSpec fieldSpec = getFieldSpec(property);
        final MethodSpec.Builder methodSpec;


        if (property.getName().startsWith("/")) {
            methodSpec = MethodSpec.methodBuilder("setValues");
        } else {
            methodSpec = MethodSpec.methodBuilder("set" + getPropertyMethodNameSuffix(property));
        }

        methodSpec.addParameter(fieldSpec.type, fieldSpec.name)
                .addModifiers(Modifier.PUBLIC)
                .addCode("this." + fieldSpec.name + " = " + fieldSpec.name + ";\n")
                .build();
        return methodSpec.build();
    }

    private MethodSpec getFieldGetter(final Property property) {
        FieldSpec fieldSpec = getFieldSpec(property);
        final MethodSpec.Builder methdBuilder;
        if (property.getName().startsWith("/")) {
            methdBuilder = MethodSpec.methodBuilder("values");
            methdBuilder.addAnnotation(JsonAnyGetter.class);
        } else {
            final String attributePrefix = isBoolean(fieldSpec) ? "is" : "get";
            methdBuilder = MethodSpec.methodBuilder(attributePrefix + getPropertyMethodNameSuffix(property))
                    .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                            .addMember("value", "$S", property.getName()).build());
        }
        methdBuilder.addModifiers(Modifier.PUBLIC)
                .returns(fieldSpec.type)
                .addCode("return " + fieldSpec.name + ";\n");
        return methdBuilder.build();
    }


    private static String getPropertyMethodNameSuffix(final Property property) {
        final String javaPropertyName = Arrays.stream(property.getName().split("_"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining());

        return javaPropertyName;
    }

    private static boolean isBoolean(FieldSpec fieldSpec) {
        return fieldSpec.type.equals(ClassName.BOOLEAN.box()) || fieldSpec.type.equals(ClassName.BOOLEAN);
    }



    protected Stream<AnnotationSpec> getDiscriminatorAnnotations(final ObjectType objectType) {
        if (StringUtils.isEmpty(objectType.getDiscriminator()) || objectType.getSubTypes().isEmpty()) {
            return Stream.empty();
        }
        final AnnotationSpec jsonTypeInfoAnnotation = AnnotationSpec.builder(JsonTypeInfo.class)
                .addMember("use", "$T.NAME", JsonTypeInfo.Id.class)
                .addMember("include", "$T.PROPERTY", JsonTypeInfo.As.class)
                .addMember("property", "$S", objectType.getDiscriminator())
                .addMember("visible", "true")
                .build();

        CodeBlock.Builder annotationBodyBuilder = CodeBlock.builder();
        List<ObjectType> children = getSubtypes(objectType).collect(Collectors.toList());
        if (!children.isEmpty()) {
            for (int i = 0; i < children.size(); i++) {
                annotationBodyBuilder.add(
                        (i == 0 ? "{" : ",") +
                                "\n@$T(value = $T.class, name = $S)"
                                + (i == children.size() - 1 ? "\n}" : "")
                        , JsonSubTypes.Type.class, getClassName(getPackagePrefix(), children.get(i)), children.get(i).getDiscriminatorValue());
            }
        }
        final AnnotationSpec jsonSubTypesAnnotation = AnnotationSpec.builder(JsonSubTypes.class)
                .addMember("value", annotationBodyBuilder.build())
                .build();

        return Stream.of(jsonSubTypesAnnotation, jsonTypeInfoAnnotation);
    }
}

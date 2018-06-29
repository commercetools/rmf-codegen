package io.vrap.rmf.codegen.common.generator.model.immutable;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.squareup.javapoet.*;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.core.CodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.codegen.common.generator.util.CtAnnotationProcessor;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.types.*;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vrap.rmf.codegen.common.generator.util.CodeGeneratorUtil.*;

public class ImmutableModelGenerator extends CodeGenerator {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public ImmutableModelGenerator(GeneratorConfig generatorConfig, Api api) {
        super(generatorConfig, api);
    }

    private static String getMethodName(final Property property) {
        final String javaPropertyName = Arrays.stream(property.getName().split("_"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining());
        if (property.getType() instanceof BooleanType) {
            return "is" + javaPropertyName;
        }
        return "get" + javaPropertyName;
    }

    public List<Path> getPaths(List<JavaFileObject> javaFiles, Path outputFolder) {
        return javaFiles.stream().map(javaFile -> Paths.get(outputFolder.toString(), javaFile.getName())).collect(Collectors.toList());
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

    protected Stream<AnnotationSpec> getAdditionalTypeAnnotations(final ObjectType objectType) {
        AnnotationSpec immutablesValueAnnotationSpec = AnnotationSpec.builder(Value.Style.class)
                .addMember("visibility", "$T.PRIVATE", Value.Style.ImplementationVisibility.class)
                .addMember("builderVisibility", "$T.PUBLIC", Value.Style.BuilderVisibility.class)
                .build();

        final ClassName builderName = ClassName.get(getObjectPackage(getPackagePrefix(), objectType), objectType.getName() + "Builder");
        AnnotationSpec jacksonAnnotationSpec = AnnotationSpec.builder(JsonDeserialize.class)
                .addMember("builder", "$T.class", builderName)
                .build();
        return Stream.of(immutablesValueAnnotationSpec, jacksonAnnotationSpec);
    }

    public Single<GenerationResult> generateStub() {

        Single<GenerationResult> generationResult = getRamlObjects().map(this::transformToJavaFile)
                .doOnNext(javaFile -> javaFile.writeTo(getOutputFolder()))
                .map(JavaFile::toJavaFileObject)
                .toList()
                .doOnSuccess(list -> CtAnnotationProcessor.processAnnotations(getOutputFolder().toFile(),
                        Arrays.asList(Nullable.class, Value.Immutable.class, JsonProperty.class, JsonDeserialize.class), list)
                )
                .map(javaFiles -> getPaths(javaFiles, getOutputFolder()))
                .map(GenerationResult::of);
        return generationResult;
    }

    public JavaFile transformToJavaFile(final AnyType anyType) {
        final TypeSpec resultTypeSpec = buildTypeSpec(anyType);
        if (resultTypeSpec == null) {
            return null;
        }
        final JavaFile javaFile = JavaFile.builder(getObjectPackage(getPackagePrefix(), anyType), resultTypeSpec).build();
        return javaFile;
    }

    private TypeSpec buildTypeSpec(final AnyType anyType) {
        if (getCustomTypeMapping().get(anyType.getName()) != null) {
            return null;
        } else if (anyType instanceof ObjectType) {
            return buildTypeSpecForObjectType(((ObjectType) anyType));
        } else if (anyType instanceof StringType) {
            return buildTypeSpecForStringType((StringType) anyType);
        } else throw new RuntimeException("unhandled type " + anyType);
    }

    private TypeSpec buildTypeSpecForStringType(final StringType stringType) {
        if (!stringType.getEnum().isEmpty()) {
            TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(getClassName(getPackagePrefix(), stringType)).addModifiers(Modifier.PUBLIC);
            stringType.getEnum()
                    .stream()
                    .map(StringInstance.class::cast)
                    .forEach(value -> enumBuilder.addEnumConstant(value.getValue()));
            enumBuilder.addJavadoc(getJavaDocProcessor().markDownToJavaDoc(stringType));
            return enumBuilder.build();
        } else {
            TypeSpec typeSpec = TypeSpec.interfaceBuilder(getClassName(getPackagePrefix(), stringType))
                    .addModifiers(Modifier.PUBLIC)
                    .addJavadoc(getJavaDocProcessor().markDownToJavaDoc(stringType))
                    .build();
            return typeSpec;
        }
    }

    private TypeSpec buildTypeSpecForObjectType(final ObjectType object) {


        // object.getPropertyType() return the supper type
        final ClassName interfaceName = getClassName(getPackagePrefix(), object);
        final TypeSpec.Builder typeSpecBuilder = TypeSpec.interfaceBuilder(interfaceName)
                .addJavadoc(getJavaDocProcessor().markDownToJavaDoc(object))
                .addModifiers(Modifier.PUBLIC);

        Stream.concat(getAdditionalTypeAnnotations(object), getDiscriminatorAnnotations(object)).forEach(typeSpecBuilder::addAnnotation);


        //Add super interfaces
        Optional.ofNullable(object.getType())
                .map(anyType -> getClassName(getPackagePrefix(), anyType))
                .map(typeSpecBuilder::addSuperinterface)
                .orElse(null);

        //Add Properties
        object.getAllProperties().stream()
                .map(property -> mapField(object, property))
                .forEach(typeSpecBuilder::addMethod);
        return typeSpecBuilder.build();
    }

    private MethodSpec mapField(final ObjectType objectType, Property property) {

        if (property.getName().startsWith("/")) {
            return mapFieldWithPattern(objectType, property);
        }
        return getFieldGetter(property);
    }

    private MethodSpec mapFieldWithPattern(final ObjectType objectType, Property property) {
        final ClassName keyType = ClassName.get(String.class);
        final TypeName valueType = getTypeNameSwitch().doSwitch(property.getType());

        String reaKeyType;
        try {
            reaKeyType = (String) ((PropertyValue) ((EObjectContainmentEList) objectType.getAnnotation("asMap").getValue().getValue()).get(0)).getValue().getValue();
        } catch (Exception e) {
            LOGGER.error("Error while parsing key for asMap annotation in object " + objectType, e);
            reaKeyType = "string";
        }

        final MethodSpec valuesGetter = MethodSpec.methodBuilder("getValues")
                .returns(ParameterizedTypeName.get(ClassName.get(Map.class), keyType, valueType))
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .addAnnotation(JsonAnySetter.class)
                .addAnnotation(JsonAnyGetter.class)
                .build();

        return valuesGetter;
    }

    private MethodSpec getFieldGetter(final Property property) {

        final MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(getMethodName(property));
        if (!property.getRequired()) {
            methodSpecBuilder.addAnnotation(Nullable.class);
        }

        final AnnotationSpec jsonAnnotationSpec = AnnotationSpec.builder(JsonProperty.class)
                .addMember("value", "$S", property.getName())
                .build();

        methodSpecBuilder
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .addAnnotation(jsonAnnotationSpec)
                .returns(getTypeNameSwitch().doSwitch(property.getType()));

        return methodSpecBuilder.build();
    }


}

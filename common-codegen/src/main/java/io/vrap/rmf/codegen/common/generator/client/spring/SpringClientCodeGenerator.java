package io.vrap.rmf.codegen.common.generator.client.spring;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.collect.LinkedListMultimap;
import com.squareup.javapoet.*;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vrap.rmf.codegen.common.generator.core.CodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.raml.model.modules.Api;
import io.vrap.rmf.raml.model.resources.HttpMethod;
import io.vrap.rmf.raml.model.resources.Method;
import io.vrap.rmf.raml.model.resources.Resource;
import io.vrap.rmf.raml.model.types.AnyType;
import io.vrap.rmf.raml.model.types.ArrayType;

import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.vrap.rmf.codegen.common.generator.util.CodeGeneratorUtil.getGeneratedAnnotation;

/**
 * Generates a very simple spring client based on the RestTemplate.
 */
public class SpringClientCodeGenerator extends CodeGenerator {
    private final Converter<String, String> classNameMapper =
            CaseFormat.LOWER_HYPHEN.converterTo(CaseFormat.UPPER_CAMEL);


    public SpringClientCodeGenerator(final GeneratorConfig generatorConfig, final Api api) {
        super(generatorConfig, api);
    }

    @Override
    public Single<GenerationResult> generateStub() {
        return getResources()
                .filter(r -> r.getResourcePathName().length() > 0)
                .collect(LinkedListMultimap::<String, Resource>create, (byPathName, r) -> byPathName.put(r.getResourcePathName(), r))
                .flattenAsFlowable(map -> map.asMap().entrySet())
                .flatMapMaybe(this::transformToJavaFile)
                .doOnNext(javaFile -> javaFile.writeTo(getOutputFolder()))
                .map(JavaFile::toJavaFileObject)
                .map(javaFile -> getPath(javaFile, getOutputFolder()))
                .toList()
                .map(GenerationResult::of);
    }

    private Path getPath(JavaFileObject javaFile, Path outputFolder) {
        return Paths.get(outputFolder.toString(), javaFile.getName());
    }

    private Maybe<JavaFile> transformToJavaFile(final Map.Entry<String, Collection<Resource>> resources) {
        final String className = classNameMapper.convert(resources.getKey()) + "Requests";
        final TypeSpec.Builder classBuilder = createRequestBuilder(className);
        final String pkg = getPackagePrefix() + ".client";
        for (final Resource resource : resources.getValue()) {
            for (final Method method : resource.getMethods()) {
                final String methodName = method.getMethodName();
                final AnyType returnType = method.getResponses().stream().filter(r -> r.getStatusCode().equals("200")).findFirst().orElse(null).getBodies().get(0).getType();
                final TypeName resourceTypeName = getTypeNameSwitch().doSwitch(returnType);
                final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName).addModifiers(Modifier.PUBLIC)
                        .returns(resourceTypeName);
                final List<ParameterSpec> parameters = resource.getAllUriParameters().stream()
                        .map(uriParameter -> ParameterSpec.builder(getTypeNameSwitch().doSwitch(uriParameter.getType()), uriParameter.getName(), Modifier.FINAL))
                        .map(ParameterSpec.Builder::build)
                        .collect(Collectors.toList());
                final AnyType bodyType;
                if (method.getMethod() == HttpMethod.POST) {
                    bodyType = method.getBodies().get(0).getType();
                    parameters.add(ParameterSpec.builder(getTypeNameSwitch().doSwitch(bodyType), "body", Modifier.FINAL).build());
                } else {
                    bodyType = null;
                }
                methodBuilder.addParameters(parameters);
                final CodeBlock.Builder codeBuilder = CodeBlock.builder()
                        .add("final $T<String, Object> parameters = new $T<>();\n\n",
                            ClassName.get(Map.class), ClassName.get(HashMap.class));
                resource.getAllUriParameters().stream()
                        .forEach(uriParameter -> codeBuilder.add("parameters.put($S, $L);\n", uriParameter.getName(), uriParameter.getName()));
                final MethodSpec methodSpec = methodBuilder
                        .addCode(codeBuilder.build())
                        .addCode(generateRequest(resource, method, returnType, bodyType))
                        .build();
                classBuilder.addMethod(methodSpec);
            }
        }
        final TypeSpec typeSpec = classBuilder.build();
        return Maybe.just(JavaFile.builder(pkg, typeSpec).build());
    }

    private CodeBlock generateRequest(final Resource resource, final Method method, final AnyType returnType, final AnyType bodyType) {
        final CodeBlock.Builder builder = CodeBlock.builder();
        final TypeName typeName = getTypeNameSwitch().doSwitch(returnType);
        builder.add("\n");
        if (returnType instanceof ArrayType) {
            builder.add("final $T<$T> type = new ParameterizedTypeReference<$T>() {};\n",
                    ClassName.get("org.springframework.core","ParameterizedTypeReference"), typeName, typeName);
        } else {
            builder.add("final Class<$T> type = $T.class;\n", typeName, typeName);
        }
        switch (method.getMethod()) {
            case DELETE:
            case GET:
                final String springMethodName = method.getMethod().getLiteral().toUpperCase();
                builder.add("final String fullUri = baseUri + $S;\n\nreturn restTemplate.exchange(fullUri, $T.$L, null, type, parameters).getBody();\n",
                        resource.getFullUri().getTemplate(), ClassName.get("org.springframework.http", "HttpMethod"), springMethodName);
                break;
            case POST:
                final TypeName bodyTypeName = getTypeNameSwitch().doSwitch(bodyType);
                final ClassName httpEntityTypeName = ClassName.get("org.springframework.http", "HttpEntity");
                builder.add("final $T<$T> entity = new $T<>(body);",
                        httpEntityTypeName, bodyTypeName, httpEntityTypeName);
                builder.add("final String fullUri = baseUri + $S;\n\nreturn restTemplate.exchange(fullUri, $T.POST, entity, type, parameters).getBody();\n",
                        resource.getFullUri().getTemplate(), ClassName.get("org.springframework.http", "HttpMethod"));
                break;
            default:
                builder.add("return null;\n");
        }

        return builder.build();
    }

    public TypeSpec.Builder createRequestBuilder(final String className) {
        final ClassName componentAnnotationTypeName = ClassName.get("org.springframework.stereotype", "Component");
        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getGeneratedAnnotation(getClass()))
                .addAnnotation(componentAnnotationTypeName);

        classBuilder.addField(ClassName.get(String.class), "baseUri", Modifier.PRIVATE, Modifier.FINAL);
        classBuilder.addField(ClassName.get("org.springframework.web.client", "RestTemplate"), "restTemplate", Modifier.PRIVATE, Modifier.FINAL);

        final ClassName valueAnnotationTypeName = ClassName.get("org.springframework.beans.factory.annotation", "Value");
        final AnnotationSpec valueAnnotation = AnnotationSpec.builder(valueAnnotationTypeName).addMember("value", "$S", "${sdk.baseUri}").build();

        final ParameterSpec baseUri = ParameterSpec.builder(ClassName.get(String.class), "baseUri", Modifier.FINAL)
                .addAnnotation(valueAnnotation).build();
        final MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(baseUri)
                .addParameter(ClassName.get("org.springframework.web.client", "RestTemplate"), "restTemplate", Modifier.FINAL)
                .addCode("this.baseUri = baseUri;\n")
                .addCode("this.restTemplate = restTemplate;\n")
                .build();
        classBuilder.addMethod(constructor);

        return classBuilder;
    }
}

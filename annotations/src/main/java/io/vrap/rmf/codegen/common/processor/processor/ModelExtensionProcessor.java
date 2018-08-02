package io.vrap.rmf.codegen.common.processor.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapper;
import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapperFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ModelExtensionProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
//            annotatedElements.forEach(o -> generateMapper(o));
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ModelExtension.class.getCanonicalName());
    }

    public void generateMapper(Element element) {

        if (!(element instanceof TypeElement)) {
            messager.printMessage(Diagnostic.Kind.ERROR, element.getSimpleName().toString() + "should be of type  TypeElement");
            return;
        }
        TypeElement typeElement = ((TypeElement) element);
        ModelExtension extension = typeElement.getAnnotation(ModelExtension.class);
        TypeMirror typeMirror = null;
        try {
            extension.extend();
        } catch (MirroredTypeException mte) {
            typeMirror = mte.getTypeMirror();
        }

        validateTypeElement(typeMirror, typeElement);

        TypeSpec resultTypeSpec = createJavaFile(typeElement, typeMirror);
        JavaFile javaFile = JavaFile.builder(elementUtils.getPackageOf(typeElement).getQualifiedName().toString(), resultTypeSpec).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }


    TypeSpec createJavaFile(final TypeElement typeElement, TypeMirror extendedTypeName) {

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(getExtensionMapperName(typeElement))
                .addSuperinterface(ExtensionMapper.class)
                .addSuperinterface(ExtensionMapperFactory.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(AutoService.class).addMember("value", "$T.class", ExtensionMapperFactory.class).build())
                .addMethods(getMethodSpecs(typeElement, extendedTypeName))
                .addFields(getFiledsSpec(typeElement));

        return typeSpecBuilder.build();

    }

    private List<MethodSpec> getMethodSpecs(TypeElement typeElement, TypeMirror extendedTypeName) {


        MethodSpec getExtension = MethodSpec.methodBuilder("getExtension")
                .addModifiers(Modifier.PUBLIC)
                .returns(Object.class)
                .addAnnotation(Override.class)
                .addCode("return extension;\n")
                .build();

        MethodSpec getHandledType = MethodSpec.methodBuilder("getHandledType")
                .addModifiers(Modifier.PUBLIC)
                .returns(Class.class)
                .addAnnotation(Override.class)
                .addCode("return $T.class;\n", extendedTypeName)
                .build();


        MethodSpec create = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .returns(ExtensionMapper.class)
                .addAnnotation(Override.class)
                .addCode("return new $T();\n", getExtensionMapperName(typeElement))
                .build();

        MethodSpec apply = MethodSpec.methodBuilder("apply")
                .addModifiers(Modifier.PUBLIC)
                .returns(Maybe.class)
                .addAnnotation(Override.class)
                .addParameter(ParameterSpec.builder(Object.class, "self", Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(String.class, "methodName", Modifier.FINAL).build())
                .addCode(getApplyCodeBlock(typeElement, (ClassName) TypeName.get(extendedTypeName)))
                .build();

        return Arrays.asList(getExtension, getHandledType, create, apply);
    }

    private List<FieldSpec> getFiledsSpec(TypeElement typeElement) {
        TypeName typeName = TypeName.get(typeUtils.getDeclaredType(typeElement));
        FieldSpec extension = FieldSpec.builder(typeName, "extension", Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T()", typeName)
                .build();

        FieldSpec logger = FieldSpec.builder(ClassName.get(Logger.class), "LOGGER", Modifier.PRIVATE, Modifier.FINAL,Modifier.STATIC)
                .initializer("$T.getLogger($T.class)", LoggerFactory.class,getExtensionMapperName(typeElement))
                .build();
        return Arrays.asList(extension,logger);
    }

    CodeBlock getApplyCodeBlock(TypeElement typeElement, ClassName extendedTypeName) {

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder()
                .addStatement("$T.requireNonNull(self)", Objects.class)
                .addStatement("$T.requireNonNull(methodName)", Objects.class)
                .addStatement("if(!(self instanceof $T)){\n" +
                        "   return $T.empty();\n" +
                        "}", extendedTypeName, Maybe.class)
                .addStatement("final $T castedSelf = ($T) self", extendedTypeName, extendedTypeName)
                .addStatement("final String trimmedMethodName = methodName.trim()")
                .addStatement(" Object result = null")
                .add("switch (trimmedMethodName) {\n");

        //result = extension.getTypeName(castedSelf);
        Flowable.fromIterable(elementUtils.getAllMembers(typeElement))
                .filter(m -> m.getAnnotation(ExtensionMethod.class) != null)
                .cast(ExecutableElement.class)
                .forEach(executableElement -> codeBlockBuilder.add(
                        "   case $S:\n" +
                                "       result = extension." + executableElement.getSimpleName().toString() + (executableElement.getParameters().isEmpty() ? "()" : "(castedSelf)") + ";\n" +
                                "       break;\n", propertyName(executableElement)
                ));

        codeBlockBuilder.add("  default:\n")
                .addStatement("       LOGGER.debug(\"property '{}' not found in type {}\", trimmedMethodName, $T.class )",getExtensionMapperName(typeElement))
                .add("       result = null;\n")
                .add("}\n")
                .addStatement(" return result != null ? $T.just(result) : $T.empty()", Maybe.class, Maybe.class)
                .build();

        return codeBlockBuilder.build();
    }


    String propertyName(ExecutableElement executableElement) {
        String property = executableElement.getSimpleName().toString();
        if (property.startsWith("get")) {
            property = property.replaceFirst("get", "");
        } else if (property.startsWith("is")) {
            property = property.replaceFirst("is", "");
        }
        return StringUtils.uncapitalize(property);
    }


    void validateTypeElement(final TypeMirror extendedTypeName, final TypeElement typeElement) {
        Flowable.fromIterable(elementUtils.getAllMembers(typeElement))
                .filter(o -> o instanceof ExecutableElement)
                .cast(ExecutableElement.class)
                .filter(executableElement -> executableElement.getAnnotation(ExtensionMethod.class) != null)
                .doOnNext(executableElement -> checkExecutableElement(executableElement, extendedTypeName))
                .count()
                .subscribe(
                        aLong -> {
                            if (aLong == 0)
                                messager.printMessage(Diagnostic.Kind.ERROR, typeElement + " must contain at least one method annotated " + ExtensionMethod.class.getName());
                        });
    }

    void checkExecutableElement(ExecutableElement executableElement, TypeMirror extendedTypeName) {
        if (executableElement.getParameters().size() > 1) {
            messager.printMessage(Diagnostic.Kind.ERROR, " a method annotated by " + ExtensionMethod.class + " should have at most one parameter of type " + extendedTypeName);
        }

        if (executableElement.getParameters().size() == 1) {
            TypeMirror parameterTypeMirror = executableElement.getParameters().get(0).asType();
            Flowable.fromIterable(typeUtils.directSupertypes(parameterTypeMirror))
                    .filter(o -> typeUtils.isAssignable(extendedTypeName, o ))
                    .count()
                    .subscribe(count -> {
                        if(count == 0 && !(extendedTypeName.equals(parameterTypeMirror))){
                            messager.printMessage(Diagnostic.Kind.ERROR,"the parameter "+parameterTypeMirror+" of method " + executableElement + " must accept a type assignable to "+extendedTypeName);
                        }
                    });
        }
    }

    private ClassName getExtensionMapperName(TypeElement typeElement){
        ClassName extensionTypeName = (ClassName) ClassName.get(typeUtils.getDeclaredType(typeElement));
        return ClassName.get(extensionTypeName.packageName(), extensionTypeName.simpleName() + "Mapper");
    }
}

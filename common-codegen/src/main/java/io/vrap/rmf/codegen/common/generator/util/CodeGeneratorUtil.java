package io.vrap.rmf.codegen.common.generator.util;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import io.vrap.rmf.raml.model.modules.Library;
import io.vrap.rmf.raml.model.types.Annotation;
import io.vrap.rmf.raml.model.types.AnyType;
import io.vrap.rmf.raml.model.types.ObjectType;
import io.vrap.rmf.raml.model.types.StringInstance;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;

import javax.annotation.Generated;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class CodeGeneratorUtil {


    public static Stream<ObjectType> getSubtypes(final ObjectType objectType) {
        return Stream.concat(
                Stream.of(objectType),
                objectType.getSubTypes()
                        .stream()
                        .filter(ObjectType.class::isInstance)
                        .map(ObjectType.class::cast)
                        .flatMap(CodeGeneratorUtil::getSubtypes)
        ).filter(obj -> StringUtils.isNotEmpty(obj.getDiscriminatorValue()));
    }


    public static ClassName getClassName(final String basePackage, final AnyType anyType) {
        return ClassName.get(getObjectPackage(basePackage, anyType), anyType.getName());
    }


    public static String getObjectPackage(final String basePackage, final AnyType anyType) {

        AnyType type = anyType;
        while (type.getType() != null) {
            if ((type.eContainer() instanceof Library) && (((Library) type.eContainer()).getAnnotation("package") != null)) {
                break;
            }
            type = type.getType();
        }
        EObject eContainer = type.eContainer();
        while (eContainer != null) {
            if (eContainer instanceof Library) {
                final String resultPackage = Optional.of(eContainer)
                        .map(Library.class::cast)
                        .map(library -> library.getAnnotation("package"))
                        .map(Annotation::getValue)
                        .map(StringInstance.class::cast)
                        .map(StringInstance::getValue)
                        .map(s -> basePackage + "." + s)
                        .orElse(basePackage);
                return resultPackage;
            }
            eContainer = eContainer.eContainer();
        }
        return basePackage;
    }

    public static AnnotationSpec getGeneratedAnnotation(Class clazz){
        Objects.requireNonNull(clazz);
        AnnotationSpec result = AnnotationSpec.builder(Generated.class)
                .addMember("value","$S",clazz.getCanonicalName() )
                .addMember("comments","$S","https://github.com/vrapio/rmf-codegen" )
                .build();
        return result;
    }

    public static AnnotationSpec getGeneratedAnnotation(Object object){
        Objects.requireNonNull(object);
        return getGeneratedAnnotation(object.getClass());
    }


    public static String toJavaIdentifier(final String input){
        if (input.length() == 0) {
            return "_";
        }
        CharacterIterator ci = new StringCharacterIterator(input);
        StringBuilder sb = new StringBuilder();
        for (char c = ci.first(); c != CharacterIterator.DONE; c = ci.next()) {
            if (c == ' ')
                c = '_';
            if (sb.length() == 0) {
                if (Character.isJavaIdentifierStart(c)) {
                    sb.append(c);
                    continue;
                } else
                    sb.append('_');
            }
            if (Character.isJavaIdentifierPart(c)) {
                sb.append(c);
            } else {
                sb.append('_');
            }
        };
        return sb.toString();

    }

    public static String toEnumValueName(final String input){
        return toJavaIdentifier(input).toUpperCase();
    }
}

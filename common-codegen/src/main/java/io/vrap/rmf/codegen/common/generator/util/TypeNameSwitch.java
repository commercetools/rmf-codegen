package io.vrap.rmf.codegen.common.generator.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.raml.model.elements.NamedElement;
import io.vrap.rmf.raml.model.types.*;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.eclipse.emf.ecore.EObject;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

public class TypeNameSwitch extends TypesSwitch<TypeName> {

    private final String basePackageName;
    private final Map<String, String> customTypeMapping;

    private TypeNameSwitch(final String basePackageName, final Map<String, String> customTypeMapping) {
        this.basePackageName = basePackageName;
        this.customTypeMapping = customTypeMapping;
    }

    public static TypeNameSwitch of(final String basePackageName, @Nullable final Map<String, String> customTypeMapping) {
        return new TypeNameSwitch(basePackageName, Optional.ofNullable(customTypeMapping).orElseGet(HashMap::new));
    }

    public static TypeNameSwitch of(final GeneratorConfig generatorConfig) {
        return new TypeNameSwitch(generatorConfig.getPackagePrefix(), generatorConfig.getCustomTypeMapping());
    }

    public Map<String, String> getCustomTypeMapping() {
        return customTypeMapping;
    }

    @Override
    public TypeName doSwitch(EObject eObject) {
        if ((eObject instanceof NamedElement) && (getCustomTypeMapping().get(((NamedElement) eObject).getName()) != null)) {
            return ClassName.bestGuess(getCustomTypeMapping().get(((NamedElement) eObject).getName()));
        }
        return Optional.ofNullable(super.doSwitch(eObject)).orElse(TypeName.get(Object.class));
    }

    @Override
    public TypeName caseNumberType(NumberType object) {
        return TypeName.get(Integer.class);
    }

    @Override
    public TypeName caseIntegerType(IntegerType object) {
        return TypeName.get(Integer.class);
    }

    @Override
    public TypeName caseBooleanType(BooleanType object) {
        return TypeName.get(Boolean.class);
    }

    @Override
    public TypeName caseDateTimeType(DateTimeType object) {
        return TypeName.get(ZonedDateTime.class);
    }

    @Override
    public TypeName caseTimeOnlyType(TimeOnlyType object) {
        return TypeName.get(LocalTime.class);
    }

    @Override
    public TypeName caseDateOnlyType(DateOnlyType object) {
        return TypeName.get(LocalDate.class);
    }

    @Override
    public TypeName caseArrayType(ArrayType arrayType) {
        return ParameterizedTypeName.get(ClassName.get(List.class), doSwitch(arrayType.getItems()));
    }

    @Override
    public TypeName caseObjectType(ObjectType objectType) {
        return CodeGeneratorUtil.getClassName(basePackageName, objectType);
    }

    @Override
    public TypeName caseStringType(StringType stringType) {
        if (stringType.getName().equalsIgnoreCase("string")) {
            return TypeName.get(String.class);
        }
        //This case happens for enumerations
        return CodeGeneratorUtil.getClassName(basePackageName, stringType);
    }


}
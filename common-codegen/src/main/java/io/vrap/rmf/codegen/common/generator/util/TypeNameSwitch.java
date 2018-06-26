package io.vrap.rmf.codegen.common.generator.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.vrap.rmf.raml.model.elements.NamedElement;
import io.vrap.rmf.raml.model.types.*;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.eclipse.emf.ecore.EObject;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TypeNameSwitch extends TypesSwitch<TypeName> {

    private final String basePackageName;
    private final Map<String, String> customTypeMapping;

    private TypeNameSwitch(final String basePackageName, final Map<String, String> customTypeMapping) {
        this.basePackageName = basePackageName;
        this.customTypeMapping = customTypeMapping;
    }

    public static TypeNameSwitch of(final String basePackageName, final Map<String, String> customTypeMapping) {
        Objects.requireNonNull(customTypeMapping);
        return new TypeNameSwitch(basePackageName, customTypeMapping);
    }

    public Map<String, String> getCustomTypeMapping() {
        return customTypeMapping;
    }

    @Override
    public TypeName doSwitch(EObject eObject) {
        if ((eObject instanceof NamedElement) && (getCustomTypeMapping().get(((NamedElement) eObject).getName()) != null)) {
            return ClassName.bestGuess(getCustomTypeMapping().get(((NamedElement) eObject).getName()));
        }
        final TypeName result = super.doSwitch(eObject);
        if (result != null) {
            return result;
        }
        return TypeName.get(Object.class);
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
        return TypeName.get(ZonedDateTime.class);
    }

    @Override
    public TypeName caseDateOnlyType(DateOnlyType object) {
        return TypeName.get(ZonedDateTime.class);
    }

    @Override
    public TypeName caseArrayType(ArrayType arrayType) {
        return ParameterizedTypeName.get(ClassName.get(List.class), doSwitch(arrayType.getItems()));
    }

    @Override
    public TypeName caseObjectType(ObjectType object) {
        return CodeGeneratorUtil.getClassName(basePackageName, object);
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
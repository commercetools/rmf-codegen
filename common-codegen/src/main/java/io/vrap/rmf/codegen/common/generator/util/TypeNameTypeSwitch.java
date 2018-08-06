package io.vrap.rmf.codegen.common.generator.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.vrap.rmf.raml.model.types.*;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.joda.time.LocalTime;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TypeNameTypeSwitch extends TypesSwitch<TypeName> {

    private final PackageSwitch packageSwitch;

    public TypeNameTypeSwitch(PackageSwitch packageSwitch) {
        this.packageSwitch = packageSwitch;
    }

    @Override
    public TypeName caseAnyType(AnyType object) {
        return TypeName.get(Object.class);
    }

    @Override
    public TypeName caseUnionType(final UnionType unionType) {
        final List<AnyType> oneOfWithoutNilType = unionType.getOneOf().stream()
                .filter(t -> !(t instanceof NilType)).collect(Collectors.toList());
        if (oneOfWithoutNilType.size() == 1) {
            return doSwitch(oneOfWithoutNilType.get(0));
        } else {
            return TypeName.get(Object.class);
        }
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
        return ClassName.get(packageSwitch.doSwitch(objectType), objectType.getName());
    }

    @Override
    public TypeName caseNilType(NilType object) {
        return TypeName.VOID;
    }

    @Override
    public TypeName caseStringType(StringType stringType) {
        if (stringType.getName().equalsIgnoreCase("string") || stringType.getEnum() == null || stringType.getEnum().isEmpty()) {
            return TypeName.get(String.class);
        }
        //This case happens for enumerations
        return ClassName.get(packageSwitch.doSwitch(stringType), stringType.getName());
    }


}
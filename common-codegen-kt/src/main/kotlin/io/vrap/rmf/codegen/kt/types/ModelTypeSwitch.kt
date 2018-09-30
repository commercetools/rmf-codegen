package io.vrap.rmf.codegen.kt.types

import com.google.inject.Inject
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.util.TypesSwitch

class ModelTypeSwitch @Inject constructor(val packageSwitch: PackageSwitch, val languageBaseTypes: LanguageBaseTypes) : TypesSwitch<VrapType>() {

    override fun caseUnionType(unionType: UnionType): VrapType {
        val oneOfWithoutNilType = unionType.oneOf
                .filter { t -> t !is NilType }
                .toList()
        return if (oneOfWithoutNilType.size == 1) {
            doSwitch(oneOfWithoutNilType[0])
        } else {
            languageBaseTypes.objectType
        }
    }

    override fun caseAnyType(`object`: AnyType) = languageBaseTypes.objectType

    override fun caseNumberType(`object`: NumberType) = languageBaseTypes.integerType

    override fun caseIntegerType(`object`: IntegerType) = languageBaseTypes.integerType

    override fun caseBooleanType(`object`: BooleanType) = languageBaseTypes.booleanType

    override fun caseDateTimeType(`object`: DateTimeType) = languageBaseTypes.dateTimeType

    override fun caseTimeOnlyType(`object`: TimeOnlyType) = languageBaseTypes.timeOnlyType

    override fun caseDateOnlyType(`object`: DateOnlyType) = languageBaseTypes.dateOnlyType

    override fun caseArrayType(arrayType: ArrayType) = VrapArrayType(arrayType.items?.let {doSwitch(arrayType.items) } ?: languageBaseTypes.objectType)

    override fun caseObjectType(objectType: ObjectType) : VrapType {
        if(objectType.name == "object"){
            return languageBaseTypes.objectType;
        }
        return VrapObjectType(`package` = packageSwitch.doSwitch(objectType), simpleClassName = objectType.name)
    }

    override fun caseNilType(`object`: NilType) = VrapNilType()

    override fun caseStringType(stringType: StringType): VrapType {
        if (stringType.isInlineType) {
            if (stringType.type == null) {
                return languageBaseTypes.stringType
            }
            // for inline types we have to check their itemType and they always have a non null itemType
            return doSwitch(stringType.type)
        } else if (stringType.name == "string" || stringType.enum == null || stringType.enum.isEmpty()) {
            return languageBaseTypes.stringType
        } else {
            //This case happens for enumerations
            return VrapObjectType(`package` = packageSwitch.doSwitch(stringType), simpleClassName = stringType.name)
        }
    }
}
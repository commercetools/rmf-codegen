package io.vrap.codegen.languages.csharp

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType

object CsharpBaseTypes : LanguageBaseTypes(
        anyType = fromCsharpType("", "Object"),
        objectType = fromCsharpType("", "Object"),
        integerType = fromScalarCsharpType("int"),
        longType = fromScalarCsharpType("long"),
        doubleType = fromScalarCsharpType("decimal"),
        stringType = fromScalarCsharpType("string"),
        booleanType = fromScalarCsharpType("bool"),
        dateTimeType = fromCsharpType("", "DateTime"),
        dateOnlyType = fromCsharpType("", "DateTime"),
        timeOnlyType = fromCsharpType("", "TimeSpan"),
        file = fromCsharpType("", "Stream")
)

fun  fromCsharpType(`package`: String, simpleName: String): VrapObjectType {
    return VrapObjectType(`package`, simpleName)
}

fun fromScalarCsharpType(scalarType: String): VrapScalarType {
    return VrapScalarType(scalarType)
}

package io.vrap.codegen.languages.php

import io.vrap.rmf.codegen.types.*

object PhpBaseTypes : LanguageBaseTypes(
        objectType = fromPhpType("", "stdClass"),
        integerType = fromScalarPhpType("int"),
        longType = fromScalarPhpType("int"),
        doubleType = fromScalarPhpType("float"),
        stringType = fromScalarPhpType("string"),
        booleanType = fromScalarPhpType("bool"),
        dateTimeType = fromDateTimeType("","DateTimeImmutable", DateTimeTypes.DateTime),
        dateOnlyType = fromDateTimeType("","DateTimeImmutable",DateTimeTypes.DateOnly),
        timeOnlyType = fromDateTimeType("","DateTimeImmutable",DateTimeTypes.TimeOnly),
        file = fromPhpType("","File")
)

fun fromDateTimeType(`package`: String, simpleName: String, dateTimeType: DateTimeTypes):VrapDateTimeType{
   return VrapDateTimeType(`package`, simpleName, dateTimeType)
}

fun fromPhpType(`package`: String, simpleName: String):VrapObjectType{
   return VrapObjectType(`package`, simpleName)
}

fun fromScalarPhpType(scalarType: String):VrapScalarType{
   return VrapScalarType(scalarType)
}

package io.vrap.codegen.languages.php

import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapDateTimeType
import io.vrap.rmf.codegen.types.VrapObjectType

object PhpBaseTypes : LanguageBaseTypes(
        objectType = fromPhpType("", "stdClass"),
        integerType = fromScalarPhpType("int"),
        longType = fromScalarPhpType("int"),
        doubleType = fromScalarPhpType("float"),
        stringType = fromScalarPhpType("string"),
        booleanType = fromScalarPhpType("bool"),
        dateTimeType = fromDateTimeType("","DateTimeImmutable", "date-time"),
        dateOnlyType = fromDateTimeType("","DateTimeImmutable","date"),
        timeOnlyType = fromDateTimeType("","DateTimeImmutable","time"),
        file = fromPhpType("","File")
)

fun fromDateTimeType(`package`: String, simpleName: String, format: String):VrapDateTimeType{
   return VrapDateTimeType(`package`, simpleName, format)
}

fun fromPhpType(`package`: String, simpleName: String):VrapObjectType{
   return VrapObjectType(`package`, simpleName)
}

fun fromScalarPhpType(scalarType: String):VrapScalarType{
   return VrapScalarType(scalarType)
}

package io.vrap.codegen.kt.languages.php

import io.vrap.rmf.codegen.kt.types.LanguageBaseTypes
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapScalarType

object PhpBaseTypes : LanguageBaseTypes(
        objectType = fromPhpType("", "stdClass"),
        integerType = fromScalarPhpType("int"),
        longType = fromScalarPhpType("int"),
        doubleType = fromScalarPhpType("float"),
        stringType = fromScalarPhpType("string"),
        booleanType = fromScalarPhpType("bool"),
        dateTimeType = fromPhpType("", "DateTimeImmutable"),
        dateOnlyType = fromPhpType("", "DateTimeImmutable"),
        timeOnlyType = fromPhpType("", "DateTimeImmutable")
)

fun  fromPhpType(`package`: String, simpleName: String):VrapObjectType{
   return VrapObjectType(`package`, simpleName)
}

fun  fromScalarPhpType(scalarType: String):VrapScalarType{
   return VrapScalarType(scalarType)
}

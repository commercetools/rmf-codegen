package io.vrap.codegen.kt.languages.php

import io.vrap.rmf.codegen.kt.types.LanguageBaseTypes
import io.vrap.rmf.codegen.kt.types.VrapObjectType

object PhpBaseTypes : LanguageBaseTypes(
        objectType = fromPhpType("", "stdObject"),
        integerType = fromPhpType("", "int"),
        longType = fromPhpType("", "int"),
        doubleType = fromPhpType("", "float"),
        stringType = fromPhpType("", "string"),
        booleanType = fromPhpType("", "bool"),
        dateTimeType = fromPhpType("", "DateTimeImmutable"),
        dateOnlyType = fromPhpType("", "DateTimeImmutable"),
        timeOnlyType = fromPhpType("", "DateTimeImmutable")
)

fun  fromPhpType(`package`: String, simpleName: String):VrapObjectType{
   return VrapObjectType(`package`, simpleName)
}

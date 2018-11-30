package io.vrap.codegen.languages.php

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapObjectType

object PhpBaseTypes : LanguageBaseTypes(
        objectType = fromPhpType("", "stdClass"),
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

package io.vrap.codegen.languages.ramldoc.model

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapScalarType

object RamldocBaseTypes : LanguageBaseTypes(
        objectType = nativeRamldocType("object"),
        integerType = nativeRamldocType("number"),
        longType = nativeRamldocType("number"),
        doubleType = nativeRamldocType("number"),
        stringType = nativeRamldocType("string"),
        booleanType = nativeRamldocType("boolean"),
        dateTimeType = nativeRamldocType("string"),
        dateOnlyType = nativeRamldocType("string"),
        timeOnlyType = nativeRamldocType("string"),
        file =  nativeRamldocType("Buffer")
)

fun nativeRamldocType(typeName: String): VrapScalarType = VrapScalarType(typeName)

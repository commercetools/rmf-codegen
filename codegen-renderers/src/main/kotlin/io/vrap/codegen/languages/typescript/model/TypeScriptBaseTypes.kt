package io.vrap.codegen.languages.typescript.model

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapScalarType

object TypeScriptBaseTypes : LanguageBaseTypes(

    objectType = nativeTypeScriptType("unknown"),
    integerType = nativeTypeScriptType("number"),
    longType = nativeTypeScriptType("number"),
    doubleType = nativeTypeScriptType("number"),
    stringType = nativeTypeScriptType("string"),
    booleanType = nativeTypeScriptType("boolean"),
    dateTimeType = nativeTypeScriptType("string"),
    dateOnlyType = nativeTypeScriptType("string"),
    timeOnlyType = nativeTypeScriptType("string")
)

fun nativeTypeScriptType(typeName: String): VrapScalarType = VrapScalarType(typeName)

package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapScalarType

object PostmanBaseTypes : LanguageBaseTypes(
        objectType = nativeTypeScriptType("object"),
        integerType = nativeTypeScriptType("number"),
        longType = nativeTypeScriptType("number"),
        doubleType = nativeTypeScriptType("number"),
        stringType = nativeTypeScriptType("string"),
        booleanType = nativeTypeScriptType("boolean"),
        dateTimeType = nativeTypeScriptType("string"),
        dateOnlyType = nativeTypeScriptType("string"),
        timeOnlyType = nativeTypeScriptType("string"),
        file =  nativeTypeScriptType("Buffer")
)

fun nativeTypeScriptType(typeName: String): VrapScalarType = VrapScalarType(typeName)

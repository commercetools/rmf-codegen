package io.vrap.codegen.languages.oas.model

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapScalarType

object OasBaseTypes : LanguageBaseTypes(
        anyType = nativeOasType("any"),
        objectType = nativeOasType("object"),
        integerType = nativeOasType("number"),
        longType = nativeOasType("number"),
        doubleType = nativeOasType("number"),
        stringType = nativeOasType("string"),
        booleanType = nativeOasType("boolean"),
        dateTimeType = nativeOasType("string"),
        dateOnlyType = nativeOasType("string"),
        timeOnlyType = nativeOasType("string"),
        file =  nativeOasType("Buffer")
)

fun nativeOasType(typeName: String): VrapScalarType = VrapScalarType(typeName)

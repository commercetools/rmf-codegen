package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapScalarType

object PostmanBaseTypes : LanguageBaseTypes(
        objectType = nativePostmanType("object"),
        integerType = nativePostmanType("number"),
        longType = nativePostmanType("number"),
        doubleType = nativePostmanType("number"),
        stringType = nativePostmanType("string"),
        booleanType = nativePostmanType("boolean"),
        dateTimeType = nativePostmanType("string"),
        dateOnlyType = nativePostmanType("string"),
        timeOnlyType = nativePostmanType("string"),
        file =  nativePostmanType("Buffer")
)

fun nativePostmanType(typeName: String): VrapScalarType = VrapScalarType(typeName)

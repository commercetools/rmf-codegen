package io.vrap.codegen.languages.jsonschema.model

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapScalarType

object JsonSchemaBaseTypes : LanguageBaseTypes(
    anyType = nativeJsonSchemaType("any"),
    objectType = nativeJsonSchemaType("object"),
    integerType = nativeJsonSchemaType("integer"),
    longType = nativeJsonSchemaType("integer"),
    doubleType = nativeJsonSchemaType("number"),
    stringType = nativeJsonSchemaType("string"),
    booleanType = nativeJsonSchemaType("boolean"),
    dateTimeType = nativeJsonSchemaType("date-time"),
    dateOnlyType = nativeJsonSchemaType("date"),
    timeOnlyType = nativeJsonSchemaType("time"),
    file = nativeJsonSchemaType("string")
)

fun nativeJsonSchemaType(typeName: String): VrapScalarType = VrapScalarType(typeName)

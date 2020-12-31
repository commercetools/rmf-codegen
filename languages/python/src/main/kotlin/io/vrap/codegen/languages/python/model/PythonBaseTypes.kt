/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.model

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapScalarType

object PythonBaseTypes : LanguageBaseTypes(
    anyType = nativePythonType("typing.Any"),
    objectType = nativePythonType("object"),
    integerType = nativePythonType("int"),
    longType = nativePythonType("int"),
    doubleType = nativePythonType("float"),
    stringType = nativePythonType("str"),
    booleanType = nativePythonType("bool"),
    dateTimeType = nativePythonType("datetime.datetime"),
    dateOnlyType = nativePythonType("datetime.date"),
    timeOnlyType = nativePythonType("datetime.time"),
    file = nativePythonType("typing.BinaryIO")
)

fun nativePythonType(typeName: String): VrapScalarType = VrapScalarType(typeName)

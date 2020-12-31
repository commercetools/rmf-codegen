package io.vrap.codegen.languages.python.model

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType

object PythonBaseTypes : LanguageBaseTypes(
    anyType = nativePythonType("any"),
    objectType = nativePythonType("any"),
    integerType = nativePythonType("int"),
    longType = nativePythonType("int"),
    doubleType = nativePythonType("float"),
    stringType = nativePythonType("str"),
    booleanType = nativePythonType("bool"),
    dateTimeType = nativePythonType("datetime.datetime"),
    dateOnlyType = nativePythonType("datetime.date"),
    timeOnlyType = nativePythonType("datetime.time"),
    file =  nativePythonType("Buffer")
)

fun nativePythonType(typeName: String): VrapScalarType = VrapScalarType(typeName)

package io.vrap.codegen.kt.languages.typescript

import io.vrap.rmf.codegen.kt.types.LanguageBaseTypes
import io.vrap.rmf.codegen.kt.types.VrapDefaultObjectType
import io.vrap.rmf.codegen.kt.types.VrapObjectType

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

fun typeScriptType(module: String, typeName: String) :VrapObjectType = VrapDefaultObjectType(`package` = module, simpleClassName = typeName)

fun nativeTypeScriptType(typeName: String) :VrapObjectType = typeScriptType("", typeName)
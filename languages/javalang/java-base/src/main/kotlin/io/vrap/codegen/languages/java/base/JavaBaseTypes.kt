package io.vrap.codegen.languages.java.base

import com.fasterxml.jackson.databind.JsonNode
import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import java.io.File
import java.lang.Boolean
import java.lang.Double
import java.lang.Long
import java.lang.String
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import kotlin.reflect.KClass

object JavaBaseTypes : LanguageBaseTypes(

    objectType = fromJavaType(JsonNode::class),
    integerType = fromDefaultJavaType(Integer::class),
    longType = fromDefaultJavaType(Long::class),
    doubleType = fromDefaultJavaType(Double::class),
    stringType = fromDefaultJavaType(String::class),
    booleanType = fromDefaultJavaType(Boolean::class),
    dateTimeType = fromJavaType(ZonedDateTime::class),
    dateOnlyType = fromJavaType(LocalDate::class),
    timeOnlyType = fromJavaType(LocalTime::class),
    file = fromJavaType(File::class)

)

fun fromJavaType(kClass: KClass<out Any>): VrapObjectType {
    return VrapObjectType(kClass.java.`package`.name, kClass.java.simpleName)
}

fun fromDefaultJavaType(kClass: KClass<out Any>): VrapScalarType = fromJavaType(kClass).let { VrapScalarType(it.simpleClassName) }

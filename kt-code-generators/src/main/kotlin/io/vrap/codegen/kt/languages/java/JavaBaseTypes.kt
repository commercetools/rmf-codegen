package io.vrap.codegen.kt.languages.java

import io.vrap.rmf.codegen.kt.types.LanguageBaseTypes
import io.vrap.rmf.codegen.kt.types.VrapDefaultObjectType
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import kotlin.reflect.KClass

object JavaBaseTypes : LanguageBaseTypes(

        objectType = fromDefaultJavaType(java.lang.Object::class),
        integerType = fromDefaultJavaType(java.lang.Integer::class),
        doubleType = fromDefaultJavaType(java.lang.Double::class),
        stringType = fromDefaultJavaType(java.lang.String::class),
        booleanType = fromDefaultJavaType(java.lang.Boolean::class),
        dateTimeType = fromJavaType(ZonedDateTime::class),
        dateOnlyType = fromJavaType(LocalDate::class),
        timeOnlyType = fromJavaType(LocalTime::class)


)

fun  fromJavaType(kClass: KClass<out Any>):VrapObjectType{
   return VrapObjectType(kClass.java.`package`.name,kClass.java.simpleName)
}
fun  fromDefaultJavaType(kClass: KClass<out Any>):VrapObjectType{
   return VrapDefaultObjectType(kClass.java.`package`.name,kClass.java.simpleName)
}
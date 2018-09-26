package io.vrap.codegen.kt.languagages.languages.java

import io.vrap.rmf.codegen.kt.types.LanguageBaseTypes
import io.vrap.rmf.codegen.kt.types.VrapDefaultObjectType
import io.vrap.rmf.codegen.kt.types.VrapObjectType

object JavaBaseTypes : LanguageBaseTypes(

        objectType = VrapDefaultObjectType(`package` = "java.lang", simpleClassName = "Object"),
        integerType = VrapDefaultObjectType(`package` = "java.lang", simpleClassName = "Integer"),
        doubleType = VrapDefaultObjectType(`package` = "java.lang", simpleClassName = "Double"),
        stringType = VrapDefaultObjectType(`package` = "java.lang", simpleClassName = "String"),
        booleanType = VrapDefaultObjectType(`package` = "java.lang", simpleClassName = "Boolean"),
        dateTimeType = VrapObjectType(`package` = "java.time", simpleClassName = "ZonedDateTime"),
        dateOnlyType = VrapObjectType(`package` = "java.time", simpleClassName = "TimeOnlyType"),
        timeOnlyType = VrapObjectType(`package` = "java.time", simpleClassName = "LocalDate")


)
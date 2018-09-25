package io.vrap.rmf.codegen.kt.types

abstract class LanguageBaseTypes(

        val objectType: VrapType,
        val integerType: VrapType,
        val booleanType: VrapType,
        val doubleType : VrapType,
        val dateTimeType: VrapType,
        val dateOnlyType: VrapType,
        val timeOnlyType: VrapType,
        val stringType: VrapType

)
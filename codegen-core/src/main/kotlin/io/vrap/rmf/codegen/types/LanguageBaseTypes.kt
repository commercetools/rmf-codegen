package io.vrap.rmf.codegen.types

abstract class LanguageBaseTypes(

        val objectType: VrapObjectType,
        val integerType: VrapObjectType,
        val longType: VrapObjectType,
        val booleanType: VrapObjectType,
        val doubleType : VrapObjectType,
        val dateTimeType: VrapObjectType,
        val dateOnlyType: VrapObjectType,
        val timeOnlyType: VrapObjectType,
        val stringType: VrapObjectType

)
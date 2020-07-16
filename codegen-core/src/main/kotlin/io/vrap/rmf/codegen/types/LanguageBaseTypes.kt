package io.vrap.rmf.codegen.types

abstract class LanguageBaseTypes(
        val anyType: VrapType,
        val objectType: VrapType,
        val integerType: VrapType,
        val longType: VrapType,
        val booleanType: VrapType,
        val doubleType : VrapType,
        val dateTimeType: VrapType,
        val dateOnlyType: VrapType,
        val timeOnlyType: VrapType,
        val stringType: VrapType,
        val file: VrapType
)

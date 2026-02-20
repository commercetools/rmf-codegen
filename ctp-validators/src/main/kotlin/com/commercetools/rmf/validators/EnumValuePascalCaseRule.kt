package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class EnumValuePascalCaseRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }
            ?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseStringType(type: StringType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (exclude.contains(type.name).not() && type.name != "string" && type.enum.isNullOrEmpty().not()) {
            type.enum.forEach { enumValue ->
                val enumName = enumValue.value as? String
                if (enumName != null && !isPascalCase(enumName)) {
                    validationResults.add(
                        error(
                            type,
                            "Enum value \"{0}\" in type \"{1}\" must be PascalCase",
                            enumName,
                            type.name
                        )
                    )
                }
            }
        }

        return validationResults
    }

    private fun isPascalCase(value: String): Boolean {
        return value == StringCaseFormat.UPPER_CAMEL_CASE.apply(value)
    }

    companion object : ValidatorFactory<EnumValuePascalCaseRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): EnumValuePascalCaseRule {
            return EnumValuePascalCaseRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): EnumValuePascalCaseRule {
            return EnumValuePascalCaseRule(severity, options)
        }
    }
}

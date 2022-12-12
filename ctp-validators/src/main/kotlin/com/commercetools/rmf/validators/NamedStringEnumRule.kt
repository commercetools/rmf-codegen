package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.StringType
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@RulesSet
class NamedStringEnumRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseStringType(type: StringType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (exclude.contains(type.name).not() && type.name != "string") {
            if (!type.isInlineType() && type.enum.isNullOrEmpty() && type.pattern == null) {
                validationResults.add(create(type, "Named string type \"{0}\" must define enum values", type.name))
            }
            if (type.isInlineType() && type.type.enum.isNullOrEmpty() && (type.type as StringType).pattern == null) {
                validationResults.add(create(type, "Named string type \"{0}\" must define enum values", type.name))
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<NamedStringEnumRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): NamedStringEnumRule {
            return NamedStringEnumRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): NamedStringEnumRule {
            return NamedStringEnumRule(severity, options)
        }
    }
}

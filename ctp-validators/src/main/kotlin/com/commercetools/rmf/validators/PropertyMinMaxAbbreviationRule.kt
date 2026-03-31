package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class PropertyMinMaxAbbreviationRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        type.properties.forEach { property ->
            if (exclude.contains("${type.name}:${property.name}").not()) {
                if (property.name.matches(Regex("^(minimum|maximum)([A-Z].*)?$"))) {
                    validationResults.add(create(type, "Property \"{0}\" of type \"{1}\" must use \"min\"/\"max\" instead of \"minimum\"/\"maximum\"", property.name, type.name))
                }
            }
        }
        return validationResults
    }

    companion object : ValidatorFactory<PropertyMinMaxAbbreviationRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): PropertyMinMaxAbbreviationRule {
            return PropertyMinMaxAbbreviationRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): PropertyMinMaxAbbreviationRule {
            return PropertyMinMaxAbbreviationRule(severity, options)
        }
    }
}

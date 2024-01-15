package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class BooleanPropertyNameRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        type.properties.filter { property -> property.type.isBoolean() }.forEach { property ->
            if (exclude.contains(property.name).not()) {
                if (property.name.matches(Regex("^is[A-Z].*$"))) {
                    validationResults.add(error(type, "Property \"{0}\" of type \"{1}\" must not have \"is\" as a prefix", property.name, type.name))
                }
            }
        }
        return validationResults
    }

    private fun AnyType.isBoolean(): Boolean {
        return when(this) {
            is BooleanType -> true
            else -> false
        }
    }

    companion object : ValidatorFactory<BooleanPropertyNameRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): BooleanPropertyNameRule {
            return BooleanPropertyNameRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): BooleanPropertyNameRule {
            return BooleanPropertyNameRule(severity, options)
        }
    }
}

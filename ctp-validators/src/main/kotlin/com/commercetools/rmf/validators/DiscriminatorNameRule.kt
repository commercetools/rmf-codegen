package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

//@RulesSet
class DiscriminatorNameRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseAnyType(type: AnyType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (exclude.contains(type.name).not()) {
            val parentType = type.type
            if (type is ObjectType && parentType != null && parentType.name.endsWith("UpdateAction")) {
                if (type.discriminatorValue.isNullOrEmpty()) {
                    validationResults.add(create(type, "Update action type \"{0}\" must have a discriminator value set", type.name))
                } else if (!type.name.contains(type.discriminatorValue, true)) {
                    validationResults.add(create(type, "Update action type \"{0}\" name must contain the discriminator value \"{1}\"", type.name, type.discriminatorValue))
                }
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<DiscriminatorNameRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): DiscriminatorNameRule {
            return DiscriminatorNameRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): DiscriminatorNameRule {
            return DiscriminatorNameRule(severity, options)
        }    }
}

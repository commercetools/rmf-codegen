package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.BuiltinType
import io.vrap.rmf.raml.model.types.ObjectType
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class DiscriminatorValueRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (type.discriminator != null && type.discriminatorValue != null && !type.isInlineType && exclude.contains(type.name).not()) {
            validationResults.add(create(type, "Discriminator and DiscriminatorValue property defined in the same type: {0}", type.name))
        }

        return validationResults
    }

    companion object : ValidatorFactory<DiscriminatorValueRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): DiscriminatorValueRule {
            return DiscriminatorValueRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): DiscriminatorValueRule {
            return DiscriminatorValueRule(severity, options)
        }    }
}

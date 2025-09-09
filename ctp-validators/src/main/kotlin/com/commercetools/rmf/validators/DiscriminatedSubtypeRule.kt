package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.ObjectType
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList
import java.util.Locale

@ValidatorSet
class DiscriminatedSubtypeRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options)  {

    // implement
    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    // implement
    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        val parent = type.type
        if (parent != null && (parent as ObjectType).discriminator != null && type.discriminatorValue == null && exclude.contains(type.name).not()) {
            validationResults.add(create(type,
                "Discriminator was added to supertype, it should be set to not null for all subtypes. Discriminator is set to null for subtype: {0}, while there is a discriminator for parent type: {1}",
                type.name, parent.name))
        }

        return validationResults
    }

    companion object : ValidatorFactory<DiscriminatedSubtypeRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): DiscriminatedSubtypeRule {
            return DiscriminatedSubtypeRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): DiscriminatedSubtypeRule {
            return DiscriminatedSubtypeRule(severity, options)
        }    }
}
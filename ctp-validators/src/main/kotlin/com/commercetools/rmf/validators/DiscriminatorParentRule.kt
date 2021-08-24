package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.BuiltinType
import io.vrap.rmf.raml.model.types.ObjectType
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class DiscriminatorParentRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (type.discriminator != null && !type.isInlineType && exclude.contains(type.name).not()) {
            val parentType = type.type
            if (parentType != null && !BuiltinType.of(parentType.name).isPresent && parentType is ObjectType && parentType.getAllProperties().firstOrNull {  p ->  p.name == type.discriminator } != null ) {
                validationResults.add(error(type, "Discriminator property \"{0}\" must defined in the type {1} only", type.discriminator(), type.name))
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<DiscriminatorParentRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): DiscriminatorParentRule {
            return DiscriminatorParentRule(options)
        }
    }
}

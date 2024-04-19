package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.modules.Library
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.BuiltinType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.TypeTemplate
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

//@ValidatorSet
class PolymorphicSubtypesRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
            (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseAnyType(type: AnyType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (exclude.contains(type.name).not() && type is ObjectType && type.subTypes.filterNot { it.isInlineType }.any() && type.discriminator == null) {
            validationResults.add(create(type, "Type \"{0}\" has subtypes but no discriminator is set", type.name))
        }
        return validationResults
    }

    companion object : ValidatorFactory<PolymorphicSubtypesRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): PolymorphicSubtypesRule {
            return PolymorphicSubtypesRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): PolymorphicSubtypesRule {
            return PolymorphicSubtypesRule(severity, options)
        }
    }
}
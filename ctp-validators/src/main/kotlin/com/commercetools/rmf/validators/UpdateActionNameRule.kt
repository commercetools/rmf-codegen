package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.AnyType
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class UpdateActionNameRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseAnyType(type: AnyType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (exclude.contains(type.name).not()) {
            if (type.type != null && type.type.name.endsWith("UpdateAction") && !type.name.endsWith("Action")) {
                validationResults.add(create(type, "Update action type \"{0}\" name must end with \"Action\"", type.name))
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<UpdateActionNameRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): UpdateActionNameRule {
            return UpdateActionNameRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): UpdateActionNameRule {
            return UpdateActionNameRule(severity, options)
        }
    }
}

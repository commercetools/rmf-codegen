package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.AnyType
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class UpdateActionNameRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseAnyType(type: AnyType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (exclude.contains(type.name).not()) {
            if (type.type != null && type.type.name.endsWith("UpdateAction") && !type.name.endsWith("Action")) {
                validationResults.add(error(type, "Update action type \"{0}\" name must end with \"Action\"", type.name))
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<UpdateActionNameRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): UpdateActionNameRule {
            return UpdateActionNameRule(options)
        }
    }
}

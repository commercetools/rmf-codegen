package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class MethodResponseRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseMethod(method: Method): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (method.responses.isEmpty() && exclude.contains("${method.method.name} ${(method.eContainer() as Resource).fullUri.template}").not()) {
            validationResults.add(create(method, "Method \"{0} {1}\" must have at least one response defined", method.method.name, (method.eContainer() as Resource).fullUri.template))
        }
        return validationResults
    }

    companion object : ValidatorFactory<MethodResponseRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): MethodResponseRule {
            return MethodResponseRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): MethodResponseRule {
            return MethodResponseRule(severity, options)
        }
    }
}

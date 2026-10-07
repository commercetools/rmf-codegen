package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class UriParameterCamelCaseRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseResource(resource: Resource): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        resource.relativeUri.variables.forEach { variableName ->
            if (exclude.contains(variableName).not() && StringCaseFormat.LOWER_CAMEL_CASE.apply(variableName) != variableName) {
                validationResults.add(create(resource, "URI parameter \"{0}\" in resource \"{1}\" must be lowerCamelCase", variableName, resource.fullUri.template))
            }
        }
        return validationResults
    }

    companion object : ValidatorFactory<UriParameterCamelCaseRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): UriParameterCamelCaseRule {
            return UriParameterCamelCaseRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): UriParameterCamelCaseRule {
            return UriParameterCamelCaseRule(severity, options)
        }
    }
}

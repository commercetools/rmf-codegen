package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class QueryParameterCamelCaseRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseMethod(method: Method): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        method.queryParameters.forEach { queryParameter ->
            run {
                if (exclude.contains(queryParameter.name).not() && queryParameter.pattern == null) {
                    if (!queryParameter.name.matches(Regex("^[.a-zA-Z0-9]+$"))) {
                        validationResults.add(create(queryParameter, "Query parameter \"{0}\" name must use alphanum and dot only", queryParameter.name))
                    } else if (StringCaseFormat.LOWER_CAMEL_CASE.apply(queryParameter.name) != queryParameter.name) {
                        validationResults.add(create(queryParameter, "Query parameter \"{0}\" must be lower camel cased", queryParameter.name))
                    }
                }
            }
        }
        return validationResults
    }

    companion object : ValidatorFactory<QueryParameterCamelCaseRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): QueryParameterCamelCaseRule {
            return QueryParameterCamelCaseRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): QueryParameterCamelCaseRule {
            return QueryParameterCamelCaseRule(RuleSeverity.ERROR, options)
        }
    }
}

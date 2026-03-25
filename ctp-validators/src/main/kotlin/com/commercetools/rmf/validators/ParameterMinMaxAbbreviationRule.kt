package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.Method
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class ParameterMinMaxAbbreviationRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseMethod(method: Method): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        method.queryParameters.forEach { queryParameter ->
            run {
                if (exclude.contains(queryParameter.name).not() && queryParameter.pattern == null) {
                    if (queryParameter.name.matches(Regex("^(minimum|maximum)([A-Z].*)?$"))) {
                        validationResults.add(create(queryParameter, "Query parameter \"{0}\" must use \"min\"/\"max\" instead of \"minimum\"/\"maximum\"", queryParameter.name))
                    }
                }
            }
        }

        method.headers.forEach { header ->
            run {
                if (exclude.contains(header.name).not()) {
                    if (header.name.matches(Regex("^(minimum|maximum)([A-Z].*)?$"))) {
                        validationResults.add(create(header, "Header \"{0}\" must use \"min\"/\"max\" instead of \"minimum\"/\"maximum\"", header.name))
                    }
                }
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<ParameterMinMaxAbbreviationRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): ParameterMinMaxAbbreviationRule {
            return ParameterMinMaxAbbreviationRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): ParameterMinMaxAbbreviationRule {
            return ParameterMinMaxAbbreviationRule(severity, options)
        }
    }
}

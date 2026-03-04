package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class WithTotalDefaultRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseMethod(method: Method): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        method.queryParameters.forEach { queryParameter ->
            run {
                if (queryParameter.name == "withTotal") {
                    val resourcePath = (method.eContainer() as Resource).fullUri.template
                    if (exclude.contains("${method.method.name} $resourcePath").not()) {
                        val defaultValue = queryParameter.type.default
                        if (defaultValue == null) {
                            validationResults.add(
                                create(
                                    queryParameter,
                                    "Query parameter \"withTotal\" of method \"{0} {1}\" must have a default value of \"false\"",
                                    method.method.name,
                                    resourcePath
                                )
                            )
                        } else if (defaultValue.value.toString() != "false") {
                            validationResults.add(
                                create(
                                    queryParameter,
                                    "Query parameter \"withTotal\" of method \"{0} {1}\" must have a default value of \"false\", found \"{2}\"",
                                    method.method.name,
                                    resourcePath,
                                    defaultValue.value.toString()
                                )
                            )
                        }
                    }
                }
            }
        }
        return validationResults
    }

    companion object : ValidatorFactory<WithTotalDefaultRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): WithTotalDefaultRule {
            return WithTotalDefaultRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): WithTotalDefaultRule {
            return WithTotalDefaultRule(severity, options)
        }
    }
}

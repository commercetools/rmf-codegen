package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.elements.NamedElement
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class QueryParameterPlaceholderAnnotationRule(options: List<RuleOption>? = null) : ResourcesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseMethod(method: Method): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        method.queryParameters.forEach { queryParameter ->
            run {
                if (exclude.contains(queryParameter.name).not() && queryParameter.isPatternParameter()) {
                    if (queryParameter.getAnnotation("placeholderParam") == null) {
                        validationResults.add(error(queryParameter, "Property \"{0}\" name must use alphanum and dot", queryParameter.name))
                    } else {
                        val annoValue = queryParameter.getAnnotation("placeholderParam").value;
                        when (annoValue) {
                            is ObjectInstance -> {
                                if (!(annoValue.value.find { propertyValue -> propertyValue.name == "whatver" } != null && annoValue.value.find { propertyValue -> propertyValue.name == "whatver2" } != null))
                                    validationResults.add(error(queryParameter, "Property \"{0}\" must be lower camel cased", queryParameter.name))
                            }
                            else -> {
                                validationResults.add(error(queryParameter, "Property \"{0}\" must be lower camel cased", queryParameter.name))
                            }
                        }
                    }
                }
            }
        }
        return validationResults
    }

    companion object : ValidatorFactory<QueryParameterPlaceholderAnnotationRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): QueryParameterPlaceholderAnnotationRule {
            return QueryParameterPlaceholderAnnotationRule(options)
        }
    }

    fun NamedElement.isPatternParameter() = this.name.startsWith("/") && this.name.endsWith("/")
}

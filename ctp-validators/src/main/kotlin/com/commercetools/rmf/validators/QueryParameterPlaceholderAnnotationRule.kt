package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class QueryParameterPlaceholderAnnotationRule(options: List<RuleOption>? = null) : ResourcesRule(options) {

    override fun caseMethod(method: Method): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        method.queryParameters.forEach( queryParameter ->
            run {

            }
        )
        return validationResults
    }

    companion object : ValidatorFactory<QueryParameterPlaceholderAnnotationRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): QueryParameterPlaceholderAnnotationRule {
            return QueryParameterPlaceholderAnnotationRule(options)
        }
    }
}
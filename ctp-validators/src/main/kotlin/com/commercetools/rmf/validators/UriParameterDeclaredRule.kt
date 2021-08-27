package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceBase
import io.vrap.rmf.raml.model.resources.ResourceContainer
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class UriParameterDeclaredRule(options: List<RuleOption>? = null) : ResolvedResourcesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseResource(resource: Resource): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        if (exclude.contains(resource.fullUri.template).not() && resource.relativeUri.variables.filter { v -> resource.uriParameters.firstOrNull { p -> p.name == v } == null }.size > 0 ) {
            validationResults.add(error(resource, "Resource \"{0}\" must define all uri parameters", resource.fullUri.template))
        }
        return validationResults
    }

    companion object : ValidatorFactory<UriParameterDeclaredRule> {
        private val defaultExcludes by lazy { emptyList<String>() }

        @JvmStatic
        override fun create(options: List<RuleOption>?): UriParameterDeclaredRule {
            return UriParameterDeclaredRule(options)
        }
    }
}

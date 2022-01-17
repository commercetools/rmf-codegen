package com.commercetools.rmf.validators

import com.damnhandy.uri.template.Literal
import io.vrap.rmf.raml.model.resources.Resource
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class ResourceCatchAllRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseResource(resource: Resource): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val resourcePathName = resource.resourcePathName
        if (exclude.contains(resourcePathName).not() && resource.resources.filter { r -> r.relativeUri.components.filter { c -> c is Literal && c.value == "/" }.size == 1 && r.relativeUri.variables.size == 1 }.size > 1) {
            validationResults.add(create(resource, "Resource \"{0}\" define only one catch all sub resource", resourcePathName))
        }
        return validationResults
    }

    companion object : ValidatorFactory<ResourceCatchAllRule> {
        private val defaultExcludes by lazy { listOf("", "inventory", "login", "me", "import", "in-store") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): ResourceCatchAllRule {
            return ResourceCatchAllRule(RuleSeverity.ERROR, options)
        }

        override fun create(severity: RuleSeverity, options: List<RuleOption>?): ResourceCatchAllRule {
            return ResourceCatchAllRule(severity, options)
        }
    }
}

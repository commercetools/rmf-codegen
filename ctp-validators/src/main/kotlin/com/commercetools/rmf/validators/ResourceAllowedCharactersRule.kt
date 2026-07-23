package com.commercetools.rmf.validators

import com.damnhandy.uri.template.Literal
import io.vrap.rmf.raml.model.resources.Resource
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class ResourceAllowedCharactersRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseResource(resource: Resource): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (exclude.contains(resource.fullUri.template).not()) {
            resource.relativeUri.components.filterIsInstance(Literal::class.java)
                .forEach { literal ->
                    val segments = literal.value.split("/").filter { it.isNotEmpty() }
                    segments.forEach { segment ->
                        val cleaned = segment.removeSuffix("=")
                        if (cleaned.isNotEmpty() && !cleaned.matches(Regex("^[a-z0-9-]+$")) && exclude.contains(cleaned).not()) {
                            validationResults.add(create(resource, "Resource \"{0}\" path segment \"{1}\" must only contain lowercase letters, digits, and hyphens", resource.fullUri.template, cleaned))
                        }
                    }
                }
        }
        return validationResults
    }

    companion object : ValidatorFactory<ResourceAllowedCharactersRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): ResourceAllowedCharactersRule {
            return ResourceAllowedCharactersRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): ResourceAllowedCharactersRule {
            return ResourceAllowedCharactersRule(severity, options)
        }
    }
}

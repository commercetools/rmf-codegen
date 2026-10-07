package com.commercetools.rmf.validators

import com.damnhandy.uri.template.Literal
import io.vrap.rmf.raml.model.resources.Resource
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class ResourceNoFileExtensionRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    private val fileExtensionPattern = Regex("\\.[a-zA-Z]{2,4}$")

    override fun caseResource(resource: Resource): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (exclude.contains(resource.fullUri.template).not()) {
            resource.relativeUri.components.filterIsInstance(Literal::class.java)
                .forEach { literal ->
                    val segments = literal.value.split("/").filter { it.isNotEmpty() }
                    segments.forEach { segment ->
                        val cleaned = segment.removeSuffix("=")
                        if (cleaned.isNotEmpty() && fileExtensionPattern.containsMatchIn(cleaned) && exclude.contains(cleaned).not()) {
                            validationResults.add(create(resource, "Resource \"{0}\" path segment \"{1}\" must not contain a file extension", resource.fullUri.template, cleaned))
                        }
                    }
                }
        }
        return validationResults
    }

    companion object : ValidatorFactory<ResourceNoFileExtensionRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): ResourceNoFileExtensionRule {
            return ResourceNoFileExtensionRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): ResourceNoFileExtensionRule {
            return ResourceNoFileExtensionRule(severity, options)
        }
    }
}

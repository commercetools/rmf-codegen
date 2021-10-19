package com.commercetools.rmf.validators

import com.damnhandy.uri.template.Literal
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class ResourceLowerCaseHyphenRule(options: List<RuleOption>? = null) : ResourcesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseResource(resource: Resource): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (resource.relativeUri.components.filterIsInstance(Literal::class.java)
                .any { literal -> literal.value != StringCaseFormat.LOWER_HYPHEN_CASE.apply(literal.value) }
        ) {
            validationResults.add(error(resource, "Resource \"{0}\" must be lower case hyphen separated", resource.fullUri.template))
        }
        return validationResults
    }

    companion object : ValidatorFactory<ResourceLowerCaseHyphenRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): ResourceLowerCaseHyphenRule {
            return ResourceLowerCaseHyphenRule(options)
        }
    }
}

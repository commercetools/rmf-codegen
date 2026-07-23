package com.commercetools.rmf.validators

import com.hypertino.inflector.English
import io.vrap.rmf.raml.model.resources.Resource
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class ResourceSingularRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    private val actionVerbs: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.ACTION_VERB.toString() }?.map { ruleOption -> ruleOption.value }
            ?: emptyList())

    override fun caseResource(resource: Resource): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        val category = ResourceClassifier.classify(resource, actionVerbs)
        if (category != ResourceCategory.SINGLETON) return emptyList()

        val resourcePathName = resource.resourcePathName
        val singularName = English.singular(resourcePathName)
        if (exclude.contains(resourcePathName).not() && singularName != resourcePathName) {
            validationResults.add(create(resource, "Singleton resource \"{0}\" must be singular", resourcePathName))
        }
        return validationResults
    }

    companion object : ValidatorFactory<ResourceSingularRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): ResourceSingularRule {
            return ResourceSingularRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): ResourceSingularRule {
            return ResourceSingularRule(severity, options)
        }
    }
}

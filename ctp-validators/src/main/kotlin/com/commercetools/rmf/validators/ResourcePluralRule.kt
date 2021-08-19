package com.commercetools.rmf.validators

import com.hypertino.inflector.English
import io.vrap.rmf.raml.model.resources.Resource
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class ResourcePluralRule(options: List<RuleOption>? = null) : ResourcesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseResource(resource: Resource): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val resourcePathName = resource.resourcePathName
        val pluralName = English.plural(English.singular(resourcePathName))
        if (exclude.contains(resourcePathName).not() && pluralName != resourcePathName) {
            validationResults.add(error(resource, "Resource \"{0}\" must be plural", resourcePathName))
        }
        return validationResults
    }

    companion object : ValidatorFactory<ResourcePluralRule> {
        private val defaultExcludes by lazy { listOf("", "inventory", "login", "me", "import", "in-store") }

        override fun create(options: List<RuleOption>): ResourcePluralRule {
            return ResourcePluralRule(options)
        }
    }
}

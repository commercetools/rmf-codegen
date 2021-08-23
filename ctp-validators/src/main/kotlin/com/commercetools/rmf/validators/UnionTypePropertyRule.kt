package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.UnionType
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class UnionTypePropertyRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseProperty(property: Property): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val propertyName = property.name ?: ""

        if (property.type is UnionType && exclude.contains(propertyName).not()) {

            validationResults.add(error(property, "Usage of union type is not allowed for property \"{0}\"", propertyName))
        }
        return validationResults
    }

    companion object : ValidatorFactory<StringPropertySingularRule> {
        private val defaultExcludes by lazy { listOf("") }

        override fun create(options: List<RuleOption>): StringPropertySingularRule {
            return StringPropertySingularRule(options)
        }
    }
}

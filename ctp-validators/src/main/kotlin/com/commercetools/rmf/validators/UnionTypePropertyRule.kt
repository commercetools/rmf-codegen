package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.UnionType
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class UnionTypePropertyRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseProperty(property: Property): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val propertyName = property.name ?: ""

        if (property.type is UnionType && exclude.contains(propertyName).not()) {

            validationResults.add(error(property, "Usage of union type is not allowed for property \"{0}\"", propertyName))
        }
        return validationResults
    }

    companion object : ValidatorFactory<UnionTypePropertyRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): UnionTypePropertyRule {
            return UnionTypePropertyRule(options)
        }
    }
}

package com.commercetools.rmf.validators

import com.hypertino.inflector.English
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.Property
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class PropertyPluralRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseProperty(property: Property): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val propertyName = property.name ?: ""
        val pluralName = English.plural(English.singular(propertyName))

        if (property.type is ArrayType && property.pattern == null && propertyName != pluralName && exclude.contains(propertyName).not()) {

            validationResults.add(error(property, "Array property \"{0}\" must be plural", propertyName))
        }
        return validationResults
    }

    companion object : ValidatorFactory<PropertyPluralRule> {
        private val defaultExcludes by lazy { listOf("error_description") }

        override fun create(options: List<RuleOption>): PropertyPluralRule {
            return PropertyPluralRule(options)
        }
    }
}

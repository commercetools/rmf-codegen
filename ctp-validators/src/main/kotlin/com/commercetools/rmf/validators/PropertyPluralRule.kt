package com.commercetools.rmf.validators

import com.hypertino.inflector.English
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class PropertyPluralRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseProperty(property: Property): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val propertyName = property.name ?: ""
        val pluralName = English.plural(English.singular(propertyName))

        if (property.type is ArrayType && property.pattern == null && propertyName != pluralName && exclude.contains(propertyName).not()) {
            val containerName = when(val propertyContainer = property.eContainer()) {
                is ObjectType -> propertyContainer.name
                else -> "unknown"
            }
            validationResults.add(create(property, "Array property \"{0}\" of type \"{1}\" must be plural", propertyName, containerName))
        }
        return validationResults
    }

    companion object : ValidatorFactory<PropertyPluralRule> {
        private val defaultExcludes by lazy { listOf("error_description") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): PropertyPluralRule {
            return PropertyPluralRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): PropertyPluralRule {
            return PropertyPluralRule(severity, options)
        }
    }
}

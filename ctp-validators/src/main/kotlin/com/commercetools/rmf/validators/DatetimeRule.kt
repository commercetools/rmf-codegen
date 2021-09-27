package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class DatetimeRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseProperty(property: Property): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val propertyName = property.name ?: ""
        if (exclude.contains(propertyName).not()) {
            if (propertyName.endsWith("At").not() && propertyName.endsWith("From").not() && propertyName.endsWith("To").not()) {
                validationResults.add(error(property, "Property \"{0}\" must finish with 'At' or 'From' or \'To\'", propertyName))
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<DatetimeRule> {
        private val defaultExcludes by lazy { listOf("error_description") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): DatetimeRule {
            return DatetimeRule(options)
        }
    }
}

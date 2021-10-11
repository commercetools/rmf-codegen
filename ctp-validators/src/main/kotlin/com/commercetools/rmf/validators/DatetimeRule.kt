package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.ObjectType
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class DatetimeRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val attributeName = type.name ?: ""

        if (exclude.contains(attributeName).not()) {
            if (type.name.endsWith("Date").not() && type.name.endsWith("Time").not() && type.name.endsWith("DateTime").not()) {
                validationResults.add(error(type, "Attribute Name \"{0}\" must finish with 'DateTime' or 'Date' or \'Time\'", attributeName))
            }
            type.properties.forEach { property ->
                kotlin.run {
                    if (property.name.endsWith("At").not() && property.name.endsWith("From").not() && property.name.endsWith("To").not()) {
                        validationResults.add(error(type, "Property \"{0}\" must finish with 'At' or 'From' or \'To\'", property.name))
                    }
                    else {
                if (type.properties.size == 1 && !property.name.endsWith("At")) {
                    if (property.name.endsWith("From")) {
                        validationResults.add(error(type, "Property \"{0}\" must need a property that finish with \'To\'", property.name))
                    }
                    if (property.name.endsWith("To")) {
                        validationResults.add(error(type, "Property \"{0}\" must need a property that finish with \'From\'", property.name))
                    }
                        }
                    }
                }
            }
        }
            return validationResults
    }

    companion object : ValidatorFactory<DatetimeRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): DatetimeRule {
            return DatetimeRule(options)
        }
    }
}

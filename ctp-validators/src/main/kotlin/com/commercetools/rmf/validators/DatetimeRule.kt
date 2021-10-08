package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.ObjectTypeFacet
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class DatetimeRule(options: List<RuleOption>? = null) : TypesRule(options) {
    val FROM = "From"
    val TO = "To"
    val AT = "At"

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val propertyName = type.properties.get(0).name ?: ""
        val propertyName2 = type.properties.get(1).name ?: ""
        val propertyType = type.properties.get(0).type.name ?: ""

        if (exclude.contains(type.name).not()) {
            if ((propertyType == "date-only").not() && (propertyType == "time-only").not() && (propertyType == "datetime").not()) {
                validationResults.add(error(type, "Property Type \"{0}\" must be 'datetime' or 'date-only' or \'time-only\'", propertyType))
            }
            if (propertyName.endsWith(AT).not() && propertyName.endsWith(FROM).not() && propertyName.endsWith(TO).not()) {
                validationResults.add(error(type, "Property \"{0}\" must finish with 'At' or 'From' or \'To\'", propertyName))
            }
            else {
                if (type.properties.size == 2) {
                    if (propertyName.endsWith(FROM).not() && propertyName2.endsWith(TO)) {
                        validationResults.add(error(type, "Property \"{0}\" must need a property that finish with \'From\'", propertyName))
                    } else if (propertyName.endsWith(FROM) && propertyName2.endsWith(TO).not()) {
                        validationResults.add(error(type, "Property \"{0}\" must need a property that finish with \'To\'", propertyName))
                    }
                } else if (type.properties.size == 1 && !propertyName.endsWith(AT)) {
                    if (propertyName.endsWith(FROM).not()) {
                        validationResults.add(error(type, "Property \"{0}\" must need a property that finish with \'From\'", propertyName))
                    }
                    if (propertyName.endsWith(TO).not()) {
                        validationResults.add(error(type, "Property \"{0}\" must need a property that finish with \'To\'", propertyName))
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

package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class DatetimeRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        type.properties.filter { property -> property.type.isDateTime() }.forEach { property ->
            if (exclude.contains(property.name).not()) {
                if (property.name.endsWith("At").not() && property.name.endsWith("From").not() && property.name.endsWith("To").not()) {
                    validationResults.add(error(type, "Property \"{0}\" must end with \"At\", \"From\" or \"To\"", property.name))
                }
                if (property.name.endsWith("From") && type.properties.none { p -> p.name.endsWith("To") }) {
                    validationResults.add(error(type, "Property \"{0}\" indicates a range, property ending with \"To\" is missing", property.name))
                }
                if (property.name.endsWith("To") && type.properties.none { p -> p.name.endsWith("From") }) {
                    validationResults.add(error(type, "Property \"{0}\" indicates a range, property ending with \"From\" is missing", property.name))
                }
            }
        }
        return validationResults
    }

    private fun AnyType.isDateTime(): Boolean {
        return when(this) {
            is DateTimeType -> true
            is TimeOnlyType -> true
            is DateOnlyType -> true
            is DateTimeOnlyType -> true
            else -> false
        }
    }

    companion object : ValidatorFactory<DatetimeRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): DatetimeRule {
            return DatetimeRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): DatetimeRule {
            return DatetimeRule(severity, options)
        }
    }
}

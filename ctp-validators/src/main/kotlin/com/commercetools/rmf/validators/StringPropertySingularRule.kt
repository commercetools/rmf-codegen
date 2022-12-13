package com.commercetools.rmf.validators

import com.hypertino.inflector.English
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.StringType
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class StringPropertySingularRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseProperty(property: Property): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val propertyName = property.name ?: ""
        val propertySingular = English.singular(propertyName)

        if (property.type is StringType && property.pattern == null && propertyName != propertySingular && exclude.contains(propertyName).not()) {

            validationResults.add(create(property, "Non array property \"{0}\" must be singular", propertyName))
        }
        return validationResults
    }


    companion object : ValidatorFactory<StringPropertySingularRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): StringPropertySingularRule {
            return StringPropertySingularRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): StringPropertySingularRule {
            return StringPropertySingularRule(severity, options)
        }
    }
}

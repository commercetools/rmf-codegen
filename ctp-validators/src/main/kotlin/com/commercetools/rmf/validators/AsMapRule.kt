package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class AsMapRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseProperty(property: Property): List<Diagnostic> {
         val validationResults: MutableList<Diagnostic> = ArrayList()
         val propertyName = property.name ?: ""

        // write the property rule validation here...
        if (exclude.contains(propertyName).not()) {
            if (propertyName.contains("-").not()) {
                validationResults.add(error(property, "Property \"{0}\" must finish with be in the format 'country-REGION", propertyName))
            }
        }
        return validationResults
    }


    companion object : ValidatorFactory<AsMapRule> {
        private val defaultExcludes by lazy { listOf("error_description") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): AsMapRule {
            return AsMapRule(options)
        }
    }
}

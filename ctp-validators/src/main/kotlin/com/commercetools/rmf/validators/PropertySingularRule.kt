package com.commercetools.rmf.validators

import com.hypertino.inflector.English
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.StringType
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class PropertySingularRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseProperty(property: Property?): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val propertyName = property?.name ?: ""
        val propertySingular = English.singular(propertyName)

        if (property != null && property.type is StringType && property.pattern == null && propertyName != propertySingular && exclude.contains(propertyName).not()) {

            validationResults.add(error(property, "Non array property \"{0}\" must be singular", propertyName))
        }
        return validationResults
    }


    companion object : ValidatorFactory<PropertySingularRule> {
        private val defaultExcludes by lazy { listOf("error_description") }

        override fun create(options: List<RuleOption>): PropertySingularRule {
            return PropertySingularRule(options)
        }
    }
}

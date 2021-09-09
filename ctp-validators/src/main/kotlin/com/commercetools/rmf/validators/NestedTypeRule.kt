package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.BuiltinType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class NestedTypeRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseProperty(property: Property): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val propertyName = property.name ?: ""
        val propertyType = property.type
        if (propertyType is ObjectType && (propertyType.name == BuiltinType.OBJECT.getName() || propertyType.name.isNullOrBlank())) {
            validationResults.add(error(property, "Property \"{0}\" must not use inline types", propertyName))
        }
        if (propertyType is ArrayType && (propertyType.items.name == BuiltinType.OBJECT.getName() || propertyType.items.name.isNullOrBlank())) {
            validationResults.add(error(property, "Property \"{0}\" must not use inline array type", propertyName))
        }
        return validationResults
    }


    companion object : ValidatorFactory<NestedTypeRule> {
        private val defaultExcludes by lazy { emptyList<String>() }

        @JvmStatic
        override fun create(options: List<RuleOption>?): NestedTypeRule {
            return NestedTypeRule(options)
        }
    }
}


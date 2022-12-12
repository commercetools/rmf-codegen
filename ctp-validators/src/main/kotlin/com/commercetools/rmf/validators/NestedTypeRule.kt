package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.BuiltinType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@RulesSet
class NestedTypeRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        type.properties.filter { it.type is ObjectType || it.type is ArrayType }.map {
            val propertyType = it.type
            when (propertyType) {
                is ObjectType -> {
                    if (exclude.contains(type.name + "#" + it.name).not() && (propertyType.name == BuiltinType.OBJECT.getName() || propertyType.name.isNullOrBlank()) && propertyType.properties.isNotEmpty()) {
                        validationResults.add(create(it, "Type \"{0}\" must not use nested inline types for property \"{1}\"", type.name, it.name))
                    } else { }
                }
                is ArrayType -> {
                    if (exclude.contains(type.name + "#" + it.name).not() && (propertyType.items.name == BuiltinType.OBJECT.getName() || propertyType.items.name.isNullOrBlank())) {
                        validationResults.add(create(it, "Type \"{0}\" must not use nested inline types for property \"{1}\"", type.name, it.name))
                    } else if (exclude.contains(type.name + "#" + it.name).not() && (propertyType.items.name == BuiltinType.ARRAY.getName() || propertyType.items.name.isNullOrBlank())) {
                        validationResults.add(create(it, "Type \"{0}\" must not use nested inline types for property \"{1}\"", type.name, it.name))
                    } else { }
                }
                else -> {}
            }
        }
        return validationResults
    }

    companion object : ValidatorFactory<NestedTypeRule> {
        private val defaultExcludes by lazy { emptyList<String>() }

        @JvmStatic
        override fun create(options: List<RuleOption>?): NestedTypeRule {
            return NestedTypeRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): NestedTypeRule {
            return NestedTypeRule(severity, options)
        }
    }
}


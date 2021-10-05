package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
//import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class AsMapRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        // write the property rule validation here...
        if (exclude.contains(type.name).not()) {
            if (type.properties.size == 1 && type.properties[0].isPatternProperty()) {
                if (type.getAnnotation("asMap") == null) {
                    validationResults.add(error(type, "Property \"{0}\" be in the format 'country-REGION", type.name))
                }
            }
        }
        return validationResults
    }

    private fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")

    companion object : ValidatorFactory<AsMapRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): AsMapRule {
            return AsMapRule(options)
        }
    }
}

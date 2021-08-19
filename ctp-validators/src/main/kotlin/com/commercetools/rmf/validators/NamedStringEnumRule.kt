package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.StringType
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class NamedStringEnumRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseStringType(type: StringType?): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (type != null && exclude.contains(type.name).not() && type.name != "string") {
            if (!type.isInlineType() && type.enum.isNullOrEmpty() && type.pattern == null) {
                validationResults.add(error(type, "Named string type \"{0}\" must define enum values", type.name))
            }
            if (type.isInlineType() && type.type.enum.isNullOrEmpty() && (type.type as StringType).pattern == null) {
                validationResults.add(error(type, "Named string type \"{0}\" must define enum values", type.name))
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<CamelCaseRule> {
        private val defaultExcludes by lazy { listOf("") }

        override fun create(options: List<RuleOption>): CamelCaseRule {
            return CamelCaseRule(options)
        }
    }
}

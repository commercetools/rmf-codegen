package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.common.util.Diagnostic
import java.util.*
import javax.print.attribute.standard.Severity

class AsMapRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (exclude.contains(type.name).not()) {
            if (type.properties.size == 1 && type.properties[0].isPatternProperty() && type.properties[0].type.isScalar()) {
                if (type.getAnnotation("asMap") == null) {
                    validationResults.add(create(type, "Pattern property \"{0}\" must define an asMap annotation", type.name))
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
            return AsMapRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): AsMapRule {
            return AsMapRule(severity, options)
        }
    }

    private fun AnyType.isScalar(): Boolean {
        return when(this) {
            is StringType -> true
            is IntegerType -> true
            is NumberType -> true
            is BooleanType -> true
            is DateTimeType -> true
            is DateOnlyType -> true
            is DateTimeOnlyType -> true
            is TimeOnlyType -> true
            is ArrayType -> this.items == null || this.items.isScalar()
            else -> false
        }
    }
}

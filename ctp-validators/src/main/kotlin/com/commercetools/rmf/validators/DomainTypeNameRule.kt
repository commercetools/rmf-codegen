package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.StringInstance
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

class DomainTypeNameRule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseAnyType(type: AnyType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
//        if (type != null && exclude.contains(type.name).not()) {
//            val packageAnnotation = type.getAnnotation("package")
//            if (packageAnnotation != null) {
//                val packageName = (type.getAnnotation("package").value as StringInstance).value
//                if (!type.name.contains(packageName, true)) {
//                    validationResults.add(create(type, "Type {0} must start with domain name {1}", type.name, packageName))
//                }
//            }
//        }

        return validationResults
    }

    companion object : ValidatorFactory<DomainTypeNameRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): DomainTypeNameRule {
            return DomainTypeNameRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): DomainTypeNameRule {
            return DomainTypeNameRule(severity, options)
        }
    }
}

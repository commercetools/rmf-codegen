package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.BuiltinType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.TypeTemplate
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class PackageDefinedRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseAnyType(type: AnyType?): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        if (type != null && type.name != null && type !is TypeTemplate && exclude.contains(type.name).not() && !BuiltinType.of(type.name).isPresent()) {
            val packageAnno = type.getAnnotation("package", true)
            if (packageAnno == null) {
                validationResults.add(error(type, "Type \"{0}\" should have package annotation defined", type.name))
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

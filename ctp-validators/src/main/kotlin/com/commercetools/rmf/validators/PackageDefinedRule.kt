package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.modules.Library
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.BuiltinType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.TypeTemplate
import org.eclipse.emf.common.util.Diagnostic
import java.util.ArrayList

class PackageDefinedRule(options: List<RuleOption>? = null) : TypesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseAnyType(type: AnyType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        if (type.name != null && type !is TypeTemplate && exclude.contains(type.name).not() && !BuiltinType.of(type.name).isPresent()) {
            val packageAnno = type.getAnnotation("package", true)

            if (packageAnno == null ) {
                if (type.eContainer() is Library || (type.isInlineType && type.type.eContainer() is Library)) {
                    val currentType = when (type.isInlineType) {
                        true -> type.type
                        else -> type
                    }
                    if ((currentType.eContainer() as Library).getAnnotation("package") == null) {
                        validationResults.add(error(currentType.eContainer(), "Library type \"{0}\" must have package annotation defined", currentType.name))
                    }
                } else {
                    validationResults.add(error(type, "Type \"{0}\" must have package annotation defined", type.name))
                }
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<PackageDefinedRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): PackageDefinedRule {
            return PackageDefinedRule(options)
        }
    }
}

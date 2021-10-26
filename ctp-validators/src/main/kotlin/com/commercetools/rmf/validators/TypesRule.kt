package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject

abstract class TypesRule(override val severity: RuleSeverity = RuleSeverity.ERROR, val options: List<RuleOption>? = null) : TypesSwitch<List<Diagnostic>>(), DiagnosticsAware, Validator<TypesValidator> {

    override fun defaultCase(`object`: EObject?): List<Diagnostic> {
        return emptyList()
    }

    override fun ValidatorType() = TypesValidator::class.java
}

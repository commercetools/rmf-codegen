package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.modules.util.ModulesSwitch
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject

abstract class ModulesRule(override val severity: RuleSeverity = RuleSeverity.ERROR, val options: List<RuleOption>? = null) : ModulesSwitch<List<Diagnostic>>(), DiagnosticsAware, Validator<ModulesValidator> {

    override fun defaultCase(`object`: EObject?): List<Diagnostic> {
        return emptyList()
    }

    override fun ValidatorType() = ModulesValidator::class.java
}

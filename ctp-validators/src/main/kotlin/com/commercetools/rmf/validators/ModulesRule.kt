package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.modules.util.ModulesSwitch
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject

abstract class ModulesRule(val options: List<RuleOption>? = null) : ModulesSwitch<List<Diagnostic>>(), DiagnosticsCreator, Validator<ModulesValidator> {

    override fun defaultCase(`object`: EObject?): List<Diagnostic> {
        return emptyList()
    }

    override fun ValidatorType() = ModulesValidator::class.java
}

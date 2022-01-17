package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject

abstract class ResolvedResourcesRule(override val severity: RuleSeverity = RuleSeverity.ERROR, val options: List<RuleOption>? = null) : ResourcesSwitch<List<Diagnostic>>(), DiagnosticsAware, Validator<ResolvedResourcesValidator> {

    override fun defaultCase(`object`: EObject?): List<Diagnostic> {
        return emptyList()
    }

    override fun ValidatorType() = ResolvedResourcesValidator::class.java
}

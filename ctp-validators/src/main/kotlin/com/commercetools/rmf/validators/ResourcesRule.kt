package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject

abstract class ResourcesRule(override val severity: RuleSeverity = RuleSeverity.ERROR, val options: List<RuleOption>? = null) : ResourcesSwitch<List<Diagnostic>>(), DiagnosticsAware, Validator<ResourcesValidator> {

    override fun defaultCase(`object`: EObject?): List<Diagnostic> {
        return emptyList()
    }

    override fun ValidatorType() = ResourcesValidator::class.java
}

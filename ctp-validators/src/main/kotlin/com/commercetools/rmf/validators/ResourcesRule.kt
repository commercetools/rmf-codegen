package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject

abstract class ResourcesRule(val options: List<RuleOption>? = null) : ResourcesSwitch<List<Diagnostic>>(), DiagnosticsCreator, Validator<ResourcesValidator> {

    override fun defaultCase(`object`: EObject?): List<Diagnostic> {
        return emptyList()
    }

    override fun ValidatorType() = ResourcesValidator::class.java
}

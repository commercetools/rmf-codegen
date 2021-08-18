package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import io.vrap.rmf.raml.validation.DiagnosticsCreator
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject

abstract class TypesRule(val options: List<RuleOption>? = null) : TypesSwitch<List<Diagnostic>>(), DiagnosticsCreator, Validator<TypesValidator> {

    override fun defaultCase(`object`: EObject?): List<Diagnostic> {
        return emptyList()
    }

    override fun ValidatorType() = TypesValidator::class.java
}

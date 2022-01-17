package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import io.vrap.rmf.raml.validation.AbstractRamlValidator
import io.vrap.rmf.raml.validation.RamlValidator
import io.vrap.rmf.raml.validation.ResolvedRamlValidator
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.common.util.DiagnosticChain
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import java.util.function.Consumer
import java.util.stream.Collectors

class ResolvedResourcesValidator(private val validators: List<ResourcesSwitch<List<Diagnostic>>> = emptyList()) : AbstractRamlValidator(), ResolvedRamlValidator {

    override fun validate(
        eClass: EClass,
        eObject: EObject,
        diagnostics: DiagnosticChain,
        context: Map<Any, Any>
    ): Boolean {
        val validationResults = validators.stream()
            .flatMap { validator: ResourcesSwitch<List<Diagnostic>> -> validator.doSwitch(eObject).stream() }
            .collect(Collectors.toList())
        validationResults.forEach(Consumer { diagnostic: Diagnostic? -> diagnostics.add(diagnostic) })
        return validationResults.isEmpty()
    }

}

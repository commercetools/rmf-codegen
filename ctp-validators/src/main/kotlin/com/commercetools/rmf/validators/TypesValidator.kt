package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import io.vrap.rmf.raml.validation.AbstractRamlValidator
import io.vrap.rmf.raml.validation.RamlValidator
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.common.util.DiagnosticChain
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import java.util.function.Consumer
import java.util.stream.Collectors

class TypesValidator(private val validators: List<TypesSwitch<List<Diagnostic>>> = emptyList()) : AbstractRamlValidator(), RamlValidator {

    override fun validate(
        eClass: EClass,
        eObject: EObject,
        diagnostics: DiagnosticChain,
        context: Map<Any, Any>
    ): Boolean {
        val validationResults = validators.stream()
            .flatMap { validator: TypesSwitch<List<Diagnostic>> -> validator.doSwitch(eObject).stream() }
            .collect(Collectors.toList())
        validationResults.forEach(Consumer { diagnostic: Diagnostic? -> diagnostics.add(diagnostic) })
        return validationResults.isEmpty()
    }

}

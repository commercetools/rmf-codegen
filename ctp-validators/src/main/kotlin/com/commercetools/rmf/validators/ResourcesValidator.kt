package com.commercetools.rmf.validators

import com.hypertino.inflector.English
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import io.vrap.rmf.raml.validation.AbstractRamlValidator
import io.vrap.rmf.raml.validation.RamlValidator
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.common.util.DiagnosticChain
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

class ResourcesValidator : AbstractRamlValidator(), RamlValidator {
    private val validators: List<ResourcesSwitch<List<Diagnostic>>> = Arrays.asList(
        PluralRuleValidator()
    )

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

    private inner class PluralRuleValidator : ResourcesSwitch<List<Diagnostic>>() {
        val exclude: List<String> = Arrays.asList("", "inventory", "login", "me", "import", "in-store")


        override fun defaultCase(`object`: EObject?): List<Diagnostic> {
            return emptyList()
        }

        override fun caseResource(resource: Resource): List<Diagnostic> {
            val validationResults: MutableList<Diagnostic> = ArrayList()
            val resourcePathName = resource.resourcePathName
            val pluralName = English.plural(English.singular(resourcePathName))
            if (exclude.contains(resourcePathName).not() && pluralName != resourcePathName) {
                validationResults.add(error(resource, "Resource should be plural"))
            }
            return validationResults;
        }
    }
}

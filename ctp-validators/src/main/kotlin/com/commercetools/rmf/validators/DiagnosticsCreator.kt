package com.commercetools.rmf.validators

import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject

interface DiagnosticsCreator: io.vrap.rmf.raml.validation.DiagnosticsCreator

interface DiagnosticsAware: DiagnosticsCreator {
    val severity: RuleSeverity

    fun create(eObject: EObject, messagePattern: String, vararg messageArgs: Any?): Diagnostic {
        return when (severity) {
            RuleSeverity.ERROR -> error(eObject, messagePattern, *messageArgs)
            RuleSeverity.WARN -> warning(eObject, messagePattern, *messageArgs)
            else -> create(Diagnostic.INFO, eObject, messagePattern, *messageArgs)
        }
    }
}

package com.commercetools.rmf.validators

import org.eclipse.emf.common.util.BasicDiagnostic
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject
import java.text.MessageFormat

interface DiagnosticsCreator {
    fun error(eObject: EObject, messagePattern: String, vararg messageArgs: Any?): Diagnostic {
        return create(Diagnostic.ERROR, eObject, messagePattern, *messageArgs)
    }

    fun warning(eObject: EObject, messagePattern: String, vararg messageArgs: Any?): Diagnostic {
        return create(Diagnostic.WARNING, eObject, messagePattern, *messageArgs)
    }

    fun create(
        severity: Int,
        eObject: EObject,
        messagePattern: String,
        vararg messageArgs: Any?
    ): Diagnostic {
        val message = MessageFormat.format("{0}: {1}", this.javaClass.simpleName, MessageFormat.format(messagePattern, *messageArgs))
        return BasicDiagnostic(severity, null, -1, message, arrayOf<Any?>(eObject, this.javaClass.simpleName))
    }

}

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

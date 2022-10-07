package com.commercetools.rmf.validators

import com.fasterxml.jackson.annotation.JsonIgnore
import io.vrap.rmf.nodes.antlr.NodeTokenProvider
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import java.text.MessageFormat

data class Violation(
    val rule: Class<DiagnosticsCreator>,
    @get:JsonIgnore val eObject: EObject,
    val messagePattern: String,
    val messageArgs: List<*>,
    val severity: Int = Diagnostic.INFO,
    val source: Source? = eObject.getSource()
) {
    val message = this.createMessage()

    private fun createMessage(): String {
        return MessageFormat.format("{0}: {1}", rule.simpleName, MessageFormat.format(messagePattern, *messageArgs.toTypedArray()))
    }
}

data class Source(val location: String, val position: Position) {
    override fun toString(): String {
        return "Source(location='$location', line=${position.line}, charPositionInLine=${position.charPositionInLine})"
    }
}

data class Position(var line: Long, val charPositionInLine: Long)

fun EObject.getSource(): Source? {
    val nodeTokenProvider = EcoreUtil.getExistingAdapter(this, NodeTokenProvider::class.java)
    if (nodeTokenProvider is NodeTokenProvider) {
        val nodeToken = nodeTokenProvider.start
        return Source(nodeToken.location, Position(nodeToken.line.toLong(), nodeToken.charPositionInLine.toLong()))
    }
    return null
}

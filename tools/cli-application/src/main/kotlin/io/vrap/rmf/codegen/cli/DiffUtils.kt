package io.vrap.rmf.codegen.cli

import io.vrap.rmf.nodes.antlr.NodeTokenProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.types.AnyType
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil

fun Api.allAnyTypes(): List<AnyType> {
    return listOf<AnyType>()
        .plus(this.types)
        .plus(this.uses?.flatMap { it.library.types } ?: emptyList())
}

fun List<AnyType>.allAnyTypeMap(): Map<String, AnyType> {
    return this.associateBy { it.name }
}

fun EObject.getSource(): Source? {
    val nodeTokenProvider = EcoreUtil.getExistingAdapter(this, NodeTokenProvider::class.java)
    if (nodeTokenProvider is NodeTokenProvider) {
        val nodeToken = nodeTokenProvider.start
        return Source(nodeToken.location, Position(nodeToken.line.toLong(), nodeToken.charPositionInLine.toLong()))
    }
    return null
}

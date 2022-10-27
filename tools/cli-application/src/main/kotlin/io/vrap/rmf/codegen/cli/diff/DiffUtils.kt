package io.vrap.rmf.codegen.cli.diff

import io.vrap.codegen.languages.extensions.resource
import io.vrap.rmf.nodes.antlr.NodeTokenProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.QueryParameter
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil

fun Api.allAnyTypes(): List<AnyType> {
    return listOf<AnyType>()
        .plus(this.types)
        .plus(this.uses?.flatMap { it.library.types } ?: emptyList())
}

fun List<AnyType>.toAnyTypeMap(): Map<String, AnyType> {
    return this.associateBy { it.name }
}

fun List<ObjectType>.toObjectTypeMap(): Map<String, ObjectType> {
    return this.associateBy { it.name }
}

fun List<Resource>.toResourcesMap(): Map<String, Resource> {
    return this.associateBy { it.fullUri.template }
}

fun List<Method>.toMethodMap(): Map<String, Method> {
    return this.associateBy { "${it.methodName} ${it.resource().fullUri.template}" }
}

fun List<Property>.toPropertyMap(): Map<String, Property> {
    return this.associateBy { it.name }
}

fun List<QueryParameter>.toParameterMap(): Map<String, QueryParameter> {
    return this.associateBy { it.name }
}

fun Api.allResourceMethods(): List<Method> = this.allContainedResources.flatMap { it.methods }

fun EObject.getSource(): Source? {
    val nodeTokenProvider = EcoreUtil.getExistingAdapter(this, NodeTokenProvider::class.java)
    if (nodeTokenProvider is NodeTokenProvider) {
        val nodeToken = nodeTokenProvider.start
        return Source(nodeToken.location, Position(nodeToken.line.toLong(), nodeToken.charPositionInLine.toLong()))
    }
    return null
}

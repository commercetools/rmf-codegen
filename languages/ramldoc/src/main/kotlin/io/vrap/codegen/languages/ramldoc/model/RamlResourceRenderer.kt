package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.hasReturnPayload
import io.vrap.codegen.languages.extensions.isSuccessfull
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.ramldoc.extensions.renderType
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.UriParameter
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.responses.Response
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.QueryParameter
import org.eclipse.emf.ecore.EObject

class RamlResourceRenderer @Inject constructor(val api: Api, val vrapTypeProvider: VrapTypeProvider) : ResourceRenderer {
    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val content = """
            |#${type.toResourceName()}
            |${if (type.fullUriParameters.size > 0) """
            |uriParameters:
            |  <<${type.fullUriParameters.joinToString("\n") { renderUriParameter(it) }}>>""" else ""}
            |${type.methods.joinToString("\n") { renderMethod(type.fullUri.template, it) }}
        """.trimMargin().keepIndentation("<<", ">>")
        val relativePath = "resources/" + type.toResourceName()+ ".raml"
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun renderMethod(fullUri: String, method: Method): String {
        return """
            |${method.methodName}:
            |  (resourcePathUri): "${fullUri.trimStart('/')}"${if (method.description != null) """
            |  description: |-
            |    <<${method.description.value.trim()}>>""" else ""}${if (method.queryParameters.size > 0) """
            |  queryParameters:
            |    <<${method.queryParameters.joinToString("\n") { renderQueryParameter(it) }}>>""" else ""}${if (method.bodies.any { it.type != null }) """
            |  body:
            |    <<${method.bodies.filter { it.type != null }.joinToString("\n") { renderBody(it) } }>>""" else ""}${if (method.responses.any { response -> response.isSuccessfull() }) """
            |  responses:
            |    <<${method.responses.filter { response -> response.isSuccessfull() }.joinToString("\n") { renderResponse(it) }}>>""" else ""}
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderResponse(response: Response): String {
        return """
            |${response.statusCode}:
            |  body:
            |    <<${response.bodies.joinToString("\n") { renderBody(it) } }>>
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderBody(body: Body): String {
        return """
            |${body.contentType}:
            |  <<${body.type.renderType()}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }
    private fun renderUriParameter(uriParameter: UriParameter): String {
        return """
            |${uriParameter.name}:
            |  <<${uriParameter.type.renderType(false)}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderQueryParameter(queryParameter: QueryParameter): String {
        return """
            |${queryParameter.name}:
            |  <<${queryParameter.type.renderType(false)}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }
}



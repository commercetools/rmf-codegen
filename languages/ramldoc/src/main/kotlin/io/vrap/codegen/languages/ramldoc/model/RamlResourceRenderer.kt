package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.isSuccessfull
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.ramldoc.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.UriParameter
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.responses.Response
import io.vrap.rmf.raml.model.security.SecuredBy
import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.ecore.EObject

class RamlResourceRenderer @Inject constructor(val api: Api, val vrapTypeProvider: VrapTypeProvider) : ResourceRenderer {
    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val content = """
            |# Resource
            |(resourceName): ${type.toResourceName()}
            |(resourcePathUri): ${type.fullUri.template}${if (type.displayName != null) """
            |displayName: ${type.displayName.value.trim()}""" else ""}${if (type.description != null) """
            |description: |-
            |  <<${type.description.value.trim()}>>""" else ""}
            |${if (type.fullUriParameters.size > 0) """
            |uriParameters:
            |  <<${type.fullUriParameters.joinToString("\n") { it.renderUriParameter() }}>>""" else ""}
            |${type.methods.joinToString("\n") { renderMethod(it) }}
        """.trimMargin().keepAngleIndent()
        val relativePath = "resources/" + type.toResourceName()+ ".raml"
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun renderMethod(method: Method): String {
        return """
            |${method.methodName}:${if (method.securedBy.isNotEmpty()) """
            |  securedBy:
            |  <<${method.securedBy.joinToString("\n") { renderScheme(it)}}>>""" else ""}${if (method.displayName != null) """
            |  displayName: ${method.displayName.value.trim()}""" else ""}${if (method.description != null) """
            |  description: |-
            |    <<${method.description.value.trim()}>>""" else ""}${if (method.queryParameters.isNotEmpty()) """
            |  queryParameters:
            |    <<${method.queryParameters.joinToString("\n") { renderQueryParameter(it) }}>>""" else ""}${if (method.bodies.any { it.type != null }) """
            |  body:
            |    <<${method.bodies.filter { it.type != null }.joinToString("\n") { renderBody(it, method) } }>>""" else ""}${if (method.responses.any { response -> response.isSuccessfull() }) """
            |  responses:
            |    <<${method.responses.filter { response -> response.isSuccessfull() }.joinToString("\n") { renderResponse(it, method) }}>>""" else ""}
        """.trimMargin().keepAngleIndent()
    }

    private fun renderScheme(securedBy: SecuredBy): String {
        return """
            |- ${securedBy.scheme.name}:
            |    <<${securedBy.parameters.toYaml()}>>
        """.trimMargin().keepAngleIndent()
    }

    private fun renderResponse(response: Response, method: Method): String {
        return """
            |${response.statusCode}:${if (response.description != null) """
            |  description: |-
            |    <<${response.description.value.trim()}>>""" else ""}
            |  body:
            |    <<${response.bodies.joinToString("\n") { renderBody(it, method, response) } }>>
        """.trimMargin().keepAngleIndent()
    }

    private fun renderBody(body: Body, method: Method): String {
        val bodyExamples = body.inlineTypes.flatMap { inlineType -> inlineType.examples.map { example -> "${method.toRequestName()}-${if (example.name.isNotEmpty()) example.name else "default"}" to example } }.toMap()
        return """
            |${body.contentType}:
            |  <<${body.type.renderType(false)}>>${if (bodyExamples.isNotEmpty()) """
            |  examples:
            |    <<${bodyExamples.map { it.value.renderExample(it.key) }.joinToString("\n")}>>""" else ""}
            |
        """.trimMargin().keepAngleIndent()
    }

    private fun renderBody(body: Body, method: Method, response: Response): String {
        val bodyExamples = body.inlineTypes.flatMap { inlineType -> inlineType.examples.map { example -> "${method.toRequestName()}-${response.statusCode}-${if (example.name.isNotEmpty()) example.name else "default"}" to example } }.toMap()
        return """
            |${body.contentType}:
            |  <<${body.type.renderType(false)}>>${if (bodyExamples.isNotEmpty()) """
            |  examples:
            |    <<${bodyExamples.map { it.value.renderExample(it.key) }.joinToString("\n")}>>""" else ""}
            |
        """.trimMargin().keepAngleIndent()
    }

    public fun renderUriParameter(uriParameter: UriParameter): String {
        return """
            |${uriParameter.name}:${if (uriParameter.type.enum.size > 0) """
            |  enum:
            |  <<${uriParameter.type.enum.joinToString("\n") { "- ${it.value}"}}>>""" else ""}
            |  <<${uriParameter.type.renderType()}>>
            |  required: ${uriParameter.required}
        """.trimMargin().keepAngleIndent()
    }

    private fun renderQueryParameter(queryParameter: QueryParameter): String {
        val parameterExamples = queryParameter.inlineTypes.flatMap { inlineType -> inlineType.examples }
        return """
            |${queryParameter.name}:${if (queryParameter.type.default != null) """
            |  default: ${queryParameter.type.default.toYaml()}""" else ""}
            |  required: ${queryParameter.required}
            |  <<${queryParameter.type.renderType()}>>${if (parameterExamples.isNotEmpty()) """
            |  examples:
            |    <<${parameterExamples.joinToString("\n") { it.renderSimpleExample() }}>>""" else ""}
        """.trimMargin().keepAngleIndent()
    }
}

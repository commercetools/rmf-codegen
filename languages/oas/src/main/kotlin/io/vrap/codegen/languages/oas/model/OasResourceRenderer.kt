package io.vrap.codegen.languages.oas.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.*
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.oas.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.UriParameter
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.responses.Response
import io.vrap.rmf.raml.model.security.SecuredBy
import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.ecore.EObject

class OasResourceRenderer @Inject constructor(val api: Api, val vrapTypeProvider: VrapTypeProvider) : ResourceRenderer {
    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val content = """
            |${type.fullUri.template}:${if (type.description != null) """
            |  description: |-
            |    <<${type.description.value.trim()}>>""" else ""}${if (type.fullUriParameters.size > 0) """
            |  parameters:
            |    <<${type.fullUriParameters.joinToString("\n") { it.renderUriParameter() }}>>""" else ""}
            |  <<${type.methods.joinToString("\n") { renderMethod(it) }}>>
        """.trimMargin().keepAngleIndent()
        val relativePath = "resources/" + type.toResourceName()+ ".raml"
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun renderMethod(method: Method): String {
        return """
            |${method.methodName}:
            |  tags:
            |    <<${method.resource().fullUri.template.split("/").filterNot(String::isEmpty).joinToString(separator = "\"\n- \"", prefix = "- \"", postfix = "\"") { s -> s.replace(Regex("[{}]"), "")}} >>${if (method.securedBy.isNotEmpty()) """
            |  security:
            |    <<${method.securedBy.joinToString("\n") { renderScheme(it)}}>>""" else ""}
            |  operationId: ${method.toRequestName()}${if (method.description != null) """
            |  description: |-
            |    <<${method.description.value.trim()}>>""" else ""}${if (method.queryParameters.isNotEmpty()) """
            |  parameters:
            |    <<${method.queryParameters.joinToString("\n") { renderQueryParameter(it) }}>>""" else ""}${if (method.bodies.any { it.type != null }) """
            |  requestBody:
            |    <<${method.bodies.filter { it.type != null }.joinToString("\n") { renderBody(it, method) } }>>""" else ""}
            |  responses:
            |    <<${method.responses.joinToString("\n") { renderResponse(it, method) }}>>
        """.trimMargin().keepAngleIndent()
    }

    private fun renderCurlExample(method: Method) : String {
        val r = method.resource()
        val params = method.queryParameters.filter { p -> p.required }
        val queryParameters = "${if (params.isNotEmpty()) "?" else ""}${params.joinToString("&") { p -> "${p.name}={${p.name}}" }}"
        return when (method.method) {
            HttpMethod.PATCH,
            HttpMethod.PUT,
            HttpMethod.POST ->
                """
                    |curl -X ${method.methodName.toUpperCase()} ${api.baseUri.template}${r.fullUri.template}$queryParameters -i \\
                    |--header 'Authorization: Bearer ${'$'}{BEARER_TOKEN}' \\
                    |--header 'Content-Type: application/json' \\
                    |--data-binary @- \<< DATA 
                    |${requestExamples(method.bodies.filter { it.type != null }.first(), method).values.firstOrNull()?.value?.toJson() ?: ""}
                    |DATA
                """
            else ->
                """
                    |curl -X ${method.methodName.toUpperCase()} ${api.baseUri.template}${r.fullUri.template}$queryParameters -i \\
                    |--header 'Authorization: Bearer ${'$'}{BEARER_TOKEN}'
                """
        }.trimMargin().escapeAll()
    }

    private fun renderScheme(securedBy: SecuredBy): String {
        return """
            |- ${securedBy.scheme.name}:
            |    <<${securedBy.parameters.getValue("scopes").toYaml()}>>
        """.trimMargin().keepAngleIndent()
    }

    private fun renderResponse(response: Response, method: Method): String {
        return """
            |${response.statusCode}:
            |  description: |-
            |    <<${response.description?.value?.trim() ?: response.statusCode}>>
            |  content:
            |    <<${response.bodies.joinToString("\n") { renderBody(it, method, response) } }>>
        """.trimMargin().keepAngleIndent()
    }

    private fun Method.hasExampleBody(): Boolean = this.hasBody() && requestExamples(this.bodies.filter { it.type != null }.first(), this).values.isNotEmpty()

    private fun Method.sendsBody(): Boolean = listOf(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH).contains(this.method)

    private fun requestExamples(body: Body, method: Method): Map<String, Example> {
        return body.inlineTypes.flatMap { inlineType -> inlineType.examples.map { example -> "${method.toRequestName()}-${if (example.name.isNotEmpty()) example.name else "default"}" to example } }.toMap()
    }

    private fun renderBody(body: Body, method: Method): String {
        val bodyExamples = requestExamples(body, method)
        return """
            |content:
            |  ${body.contentType}: {}
        """.trimMargin().keepAngleIndent()
//        |  <<${body.type.renderType(false)}>>${if (bodyExamples.isNotEmpty()) """
//            |  examples:
//            |    <<${bodyExamples.map { it.value.renderExample(it.key) }.joinToString("\n")}>>""" else ""}
//        |
    }

    private fun renderBody(body: Body, method: Method, response: Response): String {
        val bodyExamples = body.inlineTypes.flatMap { inlineType -> inlineType.examples.map { example -> "${method.toRequestName()}-${response.statusCode}-${if (example.name.isNotEmpty()) example.name else "default"}" to example } }.toMap()
        return """
            |${body.contentType}: {}
        """.trimMargin().keepAngleIndent()
//        |  <<${body.type.renderType(false)}>>${if (bodyExamples.isNotEmpty()) """
//            |  examples:
//            |    <<${bodyExamples.map { it.value.renderExample(it.key) }.joinToString("\n")}>>""" else ""}
//        |
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
        return """
            |- name: ${queryParameter.name}${if (queryParameter.type.default != null) """
            |  default: ${queryParameter.type.default.toYaml()}""" else ""}
            |  in: query
            |  required: ${queryParameter.required}
        """.trimMargin().keepAngleIndent()
    }
}

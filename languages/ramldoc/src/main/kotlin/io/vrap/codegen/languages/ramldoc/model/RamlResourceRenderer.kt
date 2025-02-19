package io.vrap.codegen.languages.ramldoc.model

import io.vrap.codegen.languages.extensions.hasBody
import io.vrap.codegen.languages.extensions.isSuccessfull
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.ramldoc.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ResourceRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
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
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.util.*

class RamlResourceRenderer constructor(val api: Api, val vrapTypeProvider: VrapTypeProvider, val inlineExamples: Boolean = false) : ResourceRenderer {
    override fun render(type: Resource): TemplateFile {
        val content = """
            |# Resource
            |(resourceName): ${type.toResourceName()}
            |(resourcePathUri): ${type.fullUri.normalize().template}${if (type.displayName != null) """
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
            |    <<${method.queryParameters.joinToString("\n") { renderQueryParameter(it) }}>>""" else ""}${if (method.headers.isNotEmpty()) """
            |  headers:
            |    <<${method.headers.joinToString("\n") { renderHeader(it) } }>>""" else ""}${if (method.bodies.any { it.type != null }) """
            |  body:
            |    <<${method.bodies.filter { it.type != null }.joinToString("\n") { renderBody(it, method).escapeAll() } }>>""" else ""}${if (method.responses.any { response -> response.isSuccessfull() }) """
            |  responses:
            |    <<${method.responses.filter { response -> response.isSuccessfull() }.joinToString("\n") { renderResponse(it, method).escapeAll() }}>>""" else ""}${if (method.hasExampleBody() || !method.sendsBody()) """
            |  (codeExamples):
            |    curl: |-
            |      <<${renderCurlExample(method)}>>""" else ""}
        """.trimMargin().keepAngleIndent()
    }

    private fun renderHeader(header: Header): String {
        return """
            |${header.name}:
            |  <<${header.type.renderType()}>>
            |  required: ${header.required}
        """.trimMargin().keepAngleIndent()
    }

    private fun renderCurlExample(method: Method) : String {
        val docsBaseUri = api.getAnnotation("docsBaseUri")
        val baseUri = if (docsBaseUri != null) { docsBaseUri.value.value as String} else { api.baseUri.template }

        val addBearerToken = method.securedBy.isNullOrEmpty().not()
        val r = method.resource()
        val params = method.queryParameters.filter { p -> p.required }
        val queryParameters = "${if (params.isNotEmpty()) "?" else ""}${params.joinToString("&") { p -> "${p.name}={${p.name}}" }}"

        val headers = method.headers.filter { it.required }.map { "--header '${it.name}: ${'$'}{${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it.name)}}'" }
        val xParam = when (method.method) {
            HttpMethod.POST -> ""
            HttpMethod.GET -> " --get"
            HttpMethod.HEAD -> " --head"
            else -> " -X ${method.methodName.uppercase(Locale.getDefault())}"
        }
        return when (method.method) {
            HttpMethod.PATCH,
            HttpMethod.PUT,
            HttpMethod.POST ->
                """
                    |curl$xParam ${baseUri}${r.fullUri.normalize().template}$queryParameters -i \\${if (addBearerToken) """
                    |--header "Authorization: Bearer ${'$'}{BEARER_TOKEN}" \\""" else ""}
                    |--header 'Content-Type: application/json' \\${if (headers.isNotEmpty()) """
                    |${headers.joinToString("\\\\\n")} \\""" else ""}
                    |--data-binary @- \<< DATA 
                    |${requestExamples(
                        method.bodies.filter { it.type != null }.first(),
                        method).values.firstOrNull()?.value?.toJson() ?: ""}
                    |DATA
                """
            else ->
                """
                    |curl$xParam ${baseUri}${r.fullUri.normalize().template}$queryParameters -i${if (addBearerToken) """ \\
                    |--header "Authorization: Bearer ${'$'}{BEARER_TOKEN}" """ else ""}${if (headers.isNotEmpty()) """ \\
                    |${headers.joinToString("\\\\\n")}
                    """ else ""}
                """
        }.trimMargin().escapeAll()
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
            |    <<${response.bodies.joinToString("\n") { renderBody(it, method, response).escapeAll() } }>>
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
            |${body.contentType}:
            |  <<${body.type.renderType(false)}>>${if (bodyExamples.isNotEmpty()) """
            |  examples:
            |    <<${bodyExamples.map { it.value.renderExample(it.key, inlineExamples).escapeAll() }.joinToString("\n")}>>""" else ""}
            |
        """.trimMargin().keepAngleIndent()
    }

    private fun renderBody(body: Body, method: Method, response: Response): String {
        val bodyExamples = body.inlineTypes.flatMap { inlineType -> inlineType.examples.map { example -> "${method.toRequestName()}-${response.statusCode}-${if (example.name.isNotEmpty()) example.name else "default"}" to example } }.toMap()
        return """
            |${body.contentType}:
            |  <<${body.type.renderType(false)}>>${if (bodyExamples.isNotEmpty()) """
            |  examples:
            |    <<${bodyExamples.map { it.value.renderExample(it.key, inlineExamples).escapeAll() }.joinToString("\n")}>>""" else ""}
            |
        """.trimMargin().keepAngleIndent()
    }

    public fun renderUriParameter(uriParameter: UriParameter): String {
        return """
            |${uriParameter.name}:${if (uriParameter.type.enum.size > 0) """
            |  enum:
            |  <<${uriParameter.type.enum.joinToString("\n") { "- '${it.value}'"}}>>""" else ""}
            |  <<${uriParameter.type.renderType()}>>
            |  required: ${uriParameter.required}
        """.trimMargin().keepAngleIndent()
    }

    private fun renderQueryParameter(queryParameter: QueryParameter): String {
        val parameterExamples = queryParameter.inlineTypes.flatMap { inlineType -> inlineType.examples }
        val parameterName = if (queryParameter.getAnnotation("placeholderParam", true) != null) {
            val anno = queryParameter.getAnnotation("placeholderParam", true)
            val o = anno.value as ObjectInstance
            (o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance).value ?: queryParameter.name
        } else {
            queryParameter.name
        }
        return """
            |${parameterName}:${if (queryParameter.type.default != null) """
            |  default: ${queryParameter.type.default.toYaml()}""" else ""}
            |  required: ${queryParameter.required}
            |  <<${queryParameter.type.renderType()}>>${if (parameterExamples.isNotEmpty()) """
            |  examples:
            |    <<${parameterExamples.joinToString("\n") { it.renderSimpleExample().escapeAll() }}>>""" else ""}
        """.trimMargin().keepAngleIndent()
    }
}

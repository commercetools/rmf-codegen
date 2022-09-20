package io.vrap.codegen.languages.ramldoc.model

import com.damnhandy.uri.template.UriTemplate
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.PathItem.HttpMethod
import io.swagger.v3.oas.models.media.ArraySchema
import io.swagger.v3.oas.models.media.ComposedSchema
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.media.UUIDSchema
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.ramldoc.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.OasPathItemRenderer
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.types.*

class OasResourceRenderer constructor(val api: OpenAPI) : OasPathItemRenderer {
    override fun render(type: Map.Entry<String, PathItem>): TemplateFile {
        val uri = UriTemplate.fromTemplate(type.key)
        val value = type.value

        val parameters = value.readOperationsMap().entries.filterNot { it.value.parameters.isNullOrEmpty() }.flatMap { entry -> entry.value.parameters.filter { it.`in` == "path" } }.plus(value.parameters ?: listOf()).distinctBy { parameter -> parameter.name }
        val content = """
            |# Resource
            |(resourceName): ${UriTemplate.fromTemplate(type.key).toResourceName()}
            |(resourcePathUri): ${uri.normalize().template}${if (value.description != null) """
            |description: |-
            |  <<${value.description}>>""" else ""}
            |${if (parameters.isNotEmpty()) """
            |uriParameters:
            |  <<${parameters.joinToString("\n") { renderUriParameter(it) }}>>""" else ""}
            |${value.readOperationsMap().toMap().entries.joinToString("\n") { renderMethod(it) }}
        """.trimMargin().keepAngleIndent()
//        |${value.methods.joinToString("\n") { renderMethod(it) }}
//            |(resourcePathUri): ${uri.normalize().template}${if (type.displayName != null) """
//            |displayName: ${type.displayName.value.trim()}""" else ""}${if (type.description != null) """
//            |description: |-
//            |  <<${type.description.value.trim()}>>""" else ""}
//            |${if (type.fullUriParameters.size > 0) """
//            |uriParameters:
//            |  <<${type.fullUriParameters.joinToString("\n") { it.renderUriParameter() }}>>""" else ""}

        val relativePath = "resources/" + uri.toResourceName() + ".raml"
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun renderMethod(method: Map.Entry<HttpMethod, Operation>): String {
        val operation = method.value
        val parameters = operation.parameters?.filter { it.`in` == "query" } ?: listOf()
        return """
            |${method.key.name.lowercase()}:${if (operation.security?.isNotEmpty() == true) """
            |  securedBy:
            |  <<${operation.security.joinToString("\n") { renderScheme(it)}}>>""" else ""}${if (operation.operationId != null) """
            |  displayName: ${operation.operationId.trim()}""" else ""}${if (operation.description != null) """
            |  description: |-
            |    <<${operation.description.trim()}>>""" else ""}${if (parameters.isNotEmpty()) """
            |  queryParameters:
            |    <<${parameters.joinToString("\n") { renderQueryParameter(it) }}>>""" else ""}${if (operation.requestBody?.content?.any { it.value.schema != null } == true) """
            |  body:
            |    <<${operation.requestBody.content.toMap().entries.joinToString("\n") { renderBody(operation.requestBody, it, method) } }>>""" else ""}${if (operation.responses.any { response -> response.isSuccessful() }) """
            |  responses:
            |    <<${operation.responses.filter { response -> response.isSuccessful() }.entries.joinToString("\n") { renderResponse(it, method) }}>>""" else ""}${if (operation.hasExampleBody() || !method.sendsBody()) """
            |  (codeExamples):
            |    curl: |-""" else ""}
        """.trimMargin().keepAngleIndent()
//            |      <<${renderCurlExample(method)}>>""" else ""}
    }

    fun Map.Entry<String, ApiResponse>.isSuccessful(): Boolean = this.key.toInt() in (200..299)

//    private fun renderCurlExample(method: Method) : String {
//        val docsBaseUri = api.getAnnotation("docsBaseUri")
//        val baseUri = if (docsBaseUri != null) { docsBaseUri.value.value as String} else { api.baseUri.template }
//
//        val r = method.resource()
//        val params = method.queryParameters.filter { p -> p.required }
//        val queryParameters = "${if (params.isNotEmpty()) "?" else ""}${params.joinToString("&") { p -> "${p.name}={${p.name}}" }}"
//        return when (method.method) {
//            HttpMethod.PATCH,
//            HttpMethod.PUT,
//            HttpMethod.POST ->
//                """
//                    |curl -X ${method.methodName.uppercase(Locale.getDefault())} ${baseUri}${r.fullUri.normalize().template}$queryParameters -i \\
//                    |--header 'Authorization: Bearer ${'$'}{BEARER_TOKEN}' \\
//                    |--header 'Content-Type: application/json' \\
//                    |--data-binary @- \<< DATA
//                    |${
//                    requestExamples(
//                        method.bodies.filter { it.type != null }.first(),
//                        method
//                    ).values.firstOrNull()?.value?.toJson() ?: ""
//                }
//                    |DATA
//                """
//            else ->
//                """
//                    |curl -X ${method.methodName.uppercase(Locale.getDefault())} ${baseUri}${r.fullUri.normalize().template}$queryParameters -i \\
//                    |--header 'Authorization: Bearer ${'$'}{BEARER_TOKEN}'
//                """
//        }.trimMargin().escapeAll()
//    }

    private fun renderScheme(securedBy: SecurityRequirement): String {
        return ""
//        return """
//            |- ${securedBy.name}:
//            |    <<${securedBy.parameters.toYaml()}>>
//        """.trimMargin().keepAngleIndent()
    }

    private fun renderResponse(apiResponse: Map.Entry<String, ApiResponse>, method: Map.Entry<HttpMethod, Operation>): String {
        val response = apiResponse.value
        val statusCode = apiResponse.key
        return """
            |${statusCode}:${if (response.description != null) """
            |  description: |-
            |    <<${response.description.trim()}>>""" else ""}
            |  body:
            |    <<${response.content?.toMap()?.entries?.joinToString("\n") { renderResponseBody(it, method, response) } ?: "{}"}>>
        """.trimMargin().keepAngleIndent()
    }

    private fun renderResponseBody(
        mediaTypeEntry: Map.Entry<String, MediaType>,
        method: Map.Entry<HttpMethod, Operation>,
        response: ApiResponse
    ): String {
        return """
            |${mediaTypeEntry.key}:
            |  <<${mediaTypeEntry.value.schema.renderResponseType()}>>
        """.trimMargin().keepAngleIndent()
    }

    private fun Operation.hasExampleBody(): Boolean = false //this.hasBody() && requestExamples(this.bodies.filter { it.type != null }.first(), this).values.isNotEmpty()

    private fun Map.Entry<HttpMethod, Operation>.sendsBody(): Boolean = listOf(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH).contains(this.key)

    private fun requestExamples(body: Body, method: Method): Map<String, Example> {
        return body.inlineTypes.flatMap { inlineType -> inlineType.examples.map { example -> "${method.toRequestName()}-${if (example.name.isNotEmpty()) example.name else "default"}" to example } }.toMap()
    }

    private fun renderBody(body: RequestBody, mediaType: Map.Entry<String, MediaType>, method: Map.Entry<HttpMethod, Operation>): String {
        val bodyExamples = emptyList<Map.Entry<String, String>>() // requestExamples(body, method)
        return ""
//        return """
//            |${mediaType.key}:
//            |  ${if (bodyExamples.isNotEmpty()) """
//            |  examples:
//            |    <<${bodyExamples.map { it.value.renderExample(it.key) }.joinToString("\n")}>>""" else ""}
//            |
//        """.trimMargin().keepAngleIndent()
        //<<${mediaType.value.schema.renderType(false)}>>
    }

//    private fun renderBody(body: Body, method: Method, response: Response): String {
//        val bodyExamples = body.inlineTypes.flatMap { inlineType -> inlineType.examples.map { example -> "${method.toRequestName()}-${response.statusCode}-${if (example.name.isNotEmpty()) example.name else "default"}" to example } }.toMap()
//        return """
//            |${body.contentType}:
//            |  <<${body.type.renderType(false)}>>${if (bodyExamples.isNotEmpty()) """
//            |  examples:
//            |    <<${bodyExamples.map { it.value.renderExample(it.key) }.joinToString("\n")}>>""" else ""}
//            |
//        """.trimMargin().keepAngleIndent()
//    }

    public fun renderUriParameter(uriParameter: Parameter): String {
        return """
            |${uriParameter.name}:
            |  <<${uriParameter.schema.renderType()}>>
            |  required: ${uriParameter.required}
        """.trimMargin().keepAngleIndent()
//        |  <<${uriParameter.schema.renderType()}>>
    }

    private fun renderQueryParameter(queryParameter: Parameter): String {
//        return ""
//        val parameterExamples = queryParameter.inlineTypes.flatMap { inlineType -> inlineType.examples }
        return """
            |${queryParameter.name}:${if (queryParameter.schema.default != null) """
            |  default: ${queryParameter.schema.default.toYaml()}""" else ""}
            |  required: ${queryParameter.required}
            |  <<${queryParameter.schema.renderType()}>>
        """.trimMargin().keepAngleIndent()

//        ${if (parameterExamples.isNotEmpty()) """
//            |  examples:
//            |    <<${parameterExamples.joinToString("\n") { it.renderSimpleExample().escapeAll() }}>>""" else ""}
    }

    private fun Schema<Any>.renderResponseType(): String {
        return """
                |type: ${this.renderTypeName()}
                |(builtinType): ${this.renderBuiltinType()}
            """.trimMargin()
    }

    private fun Schema<Any>.renderType(): String {
        return """
                |type: ${this.renderTypeName()}
                |(builtinType): ${this.renderBuiltinType()}${this.renderExtras()}
            """.trimMargin()
    }

    private fun Schema<Any>.renderExtras(): String {
        return when(this) {
            is StringSchema -> this.renderSchemaExtras()
            is ComposedSchema -> this.renderSchemaExtras()
            else -> ""
        }
    }

    private fun ComposedSchema.renderSchemaExtras(): String {
        var subTypes = this.anyOf?.map { it.renderTypeName() } ?: listOf()
        subTypes = subTypes.plus(this.oneOf?.map { it.renderTypeName() } ?: listOf())

        return """${if (subTypes.isNotEmpty()) """
                |
                |(oneOf):
                |  <<${subTypes.joinToString("\n") { "- $it"}}>>""" else ""}
        """.trimMargin().keepAngleIndent()
    }


    private fun StringSchema.renderSchemaExtras(): String {
        return """${if (this.enum != null && this.enum.size > 0) """
                |enum:
                |  <<${this.enum.joinToString("\n") { "- $it"}}>>""" else ""}
        """.trimMargin().keepAngleIndent()
    }

    private fun Schema<Any>.renderTypeName(): String {
        if (this.`$ref`?.isNotEmpty() == true) {
            return this.`$ref`.replace("#/components/schemas/", "")
        }
        return when (this) {
            is StringSchema -> this.type
            is ObjectSchema -> this.type
            is ArraySchema -> this.type
            is ComposedSchema -> this.renderComposedTypeName()
            is UUIDSchema -> this.type
            else -> ""
        }
    }

    private fun Schema<Any>.renderBuiltinType(): String {
        if (this.`$ref`?.isNotEmpty() == true) {
            val typeName = this.`$ref`.replace("#/components/schemas/", "")
            return api.components.schemas[typeName]?.renderBuiltinType() ?: "any"
        }
        return when (this) {
            is StringSchema -> "string"
            is ObjectSchema -> "object"
            is ArraySchema -> "array"
            is ComposedSchema -> if (this.isInheritedObject()) "object" else "any"
            else -> "any"
        }
    }

    private fun ComposedSchema.isInheritedObject(): Boolean {
        if (this.allOf?.size == 2) {
            if (this.allOf.any { it.`$ref`.isNullOrEmpty().not() } && this.allOf.any { it is ObjectSchema }) {
                return true
            }
        }
        return false
    }

    private fun ComposedSchema.renderComposedTypeName(): String {

        return if (this.anyOf?.isNotEmpty() == true) {
            this.anyOf.joinToString(" | ") { it.renderTypeName() }
        } else if (this.allOf?.isNotEmpty() == true) {
            this.allOf.joinToString("," , "[", "]") { it.renderTypeName() }
        } else if (this.oneOf?.isNotEmpty() == true) {
            this.oneOf.joinToString(" | ") { it.renderTypeName() }
        } else {
            "any"
        }
    }
}

package io.vrap.codegen.languages.ramldoc.model

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.google.common.collect.Lists
import io.vrap.codegen.languages.extensions.*
import io.vrap.codegen.languages.ramldoc.extensions.*
import io.vrap.codegen.languages.ramldoc.extensions.toResourceName
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.security.SecuritySchemeType
import io.vrap.rmf.raml.model.types.*

class MarkdownRenderer constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider, @AllAnyTypes val anyTypeList: List<AnyType>, @ModelPackageName val modelPackageName: String) : EObjectExtensions, FileProducer {
    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                apiRaml(api),
                jonsl(api)
        )
    }

    private fun apiRaml(api: Api): TemplateFile {
        val docsBaseUri = api.getAnnotation("docsBaseUri")
        val baseUri = if (docsBaseUri != null) { docsBaseUri.value.value as String} else { api.baseUri.template }
        val oAuth20Settings = api.securitySchemes.filter { it.type == SecuritySchemeType.OAUTH_20 }
        val content = """
            |The API base uri is ${baseUri}${if (api.baseUriParameters.size > 0) """
            |
            |The parameters to complete the URI are:
            |
            |${api.baseUriParameters.joinToString("\n") { "* ${it.name.replace("^ID$".toRegex(RegexOption.IGNORE_CASE), "id")}" }}""" else ""}
            |
            |${if (oAuth20Settings.isNotEmpty()) """The API is secured using OAuth 2.0.""" else ""}
            |
            |## Types
            |
            |${anyTypeList.filterNot{ it.deprecated() }.filterNot { it is UnionType }.sortedWith(compareBy { it.name }).joinToString("\n") { """
            |### ${it.name}
            |
            |${renderType(it)}
            |
            |""" }}
            |  
            |## Resources
            |${api.allContainedResources.sortedWith(compareBy { it.resourcePath }).joinToString("\n") { renderResource(it) }}
        """.trimMargin()
//        |${api.allContainedResources.sortedWith(compareBy { it.resourcePath }).joinToString("\n") { "${it.fullUri.normalize().template }: !include resources/${it.toResourceName()}.raml" }}

        return TemplateFile(relativePath = "api.md",
                content = content
        )
    }

    private fun jonsl(api: Api): TemplateFile {
        val content = """
            |[
            |${api.allContainedResources.sortedWith(compareBy { it.resourcePath }).map { renderJsonLResource(it) }.filter { it.isNotEmpty() }.joinToString(",\n")}
            |]
        """.trimMargin()

        return TemplateFile(relativePath = "api.jsonl",
                content = content
        )
    }

    private fun renderJsonLResource(resource: Resource): String {
        return """
            |${resource.methods.map { it to parameterTestProvider(resource, it)?.toJson()?.trim('\"') }.filterNot { pair -> pair.second.isNullOrEmpty() }.joinToString(",\n") { renderJsonLMethod(resource, it.first, it.second) }}
        """.trimMargin()
    }

    private fun renderJsonLMethod(resource: Resource, method: Method, chain: String?): String {
        return """
            |{
            |  "input_text": ${method.displayName?.value?.toJson() ?: method.toRequestName().toJson()},
            |  "output_text": "return apiRoot.$chain.execute()"
            |}
        """.trimMargin()
    }

    private fun parameterTestProvider(resource: Resource, method: Method): String? {
        var shouldPassBody = resource.resourcePath.contains("product-projections/search")
                && method.methodName == "post"

        if(method.queryParameters.any{p ->p.required})
            return null
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "{${r.relativeUri.paramValues().joinToString(", ") { p -> " ${if(p.contains('.')) "\"${p}\"" else "$p"}: \"test_$p\""} }}" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null || shouldPassBody)  "{body: null,\nheaders:null}" else ""})")
        //.plus("${method.method}(${if (method.methodName=="post") "{body: null,\nheaders:null}" else ""})")

        return builderChain.joinToString("\n.")
    }

    fun UriTemplate.paramValues(): List<String> {
        return this.components.filterIsInstance<Expression>().flatMap { expression -> expression.varSpecs.map { varSpec -> varSpec.variableName  } }
    }

    fun Resource.resourcePathList(): List<Resource> {
        val path = Lists.newArrayList<Resource>()
        if (this.fullUri.template == "/") {
            return path
        }
        path.add(this)
        var t = this.eContainer()
        while (t is Resource) {
            val template = t.fullUri.template
            if (template != "/") {
                path.add(t)
            }
            t = t.eContainer()
        }
        return Lists.reverse(path)
    }

    fun Method.firstBody(): Body? = this.bodies.stream().findFirst().orElse(null)


    private fun renderResource(resource: Resource): String {
        return """
            |### ${resource.fullUri.normalize().template}
            |
            |${if (resource.description?.value != null) resource.description.value else ""}
            |
            |#### Methods:
            |${resource.methods.joinToString("\n") { renderMethod(it) }}
        """.trimMargin()
    }

    private fun renderMethod(method: Method): String {
        val methodBody = if (method.bodies != null && method.bodies.isNotEmpty()) method.bodies[0].type else null
        val responseBody = method.returnType()

        return """
            |* ${method.methodName}
            |  ${if (methodBody != null) "The body of this method is ${methodBody.renderTypeFacet()}." else "" } 
            |  ${if (responseBody != null) "The response type of this method is ${responseBody.renderTypeFacet()}." else "" } 
            |"""
    }

    private fun renderType(type: AnyType): String {
        return when(type) {
            is ObjectType -> renderObject(type)
            else -> ""
        }
    }
    private fun renderObject(type: ObjectType): String {
        val properties = type.allProperties

        val postmanExampleAnno = type.getAnnotation("postman-example")
        val postmanExample = if (postmanExampleAnno != null) {
            val example = TypesFactory.eINSTANCE.createExample()
            val boolInstance = TypesFactory.eINSTANCE.createBooleanInstance()
            boolInstance.value = true
            example.name = if (type.examples.firstOrNull { e -> e.name.isNullOrEmpty() } != null) "postman" else ""
            example.strict = boolInstance
            example
        } else {
            null
        }
        val examples = type.examples.plus(postmanExample).filterNotNull().sortedWith(compareBy { it.name })

        return """${if (type.description?.value != null) """
            |${type.description.value.trim()}""" else ""}
            |
            |Properties of this type are:
            |
            |${properties.joinToString("\n") { renderProperty(it) }}
            |
            |${if (examples.isNotEmpty()) """
            |#### Example
            |
            |```json
            |${examples.first().toJson()}
            |```
            """ else ""}
            """.trimMargin()
    }

    private fun renderProperty(property: Property): String {
        val description = if ((property.type.isInlineType || property.type.isScalar())  && property.type.description?.value.isNullOrBlank().not()) {
            property.type.description.value.trim()} else ""
        val typeName = property.type.renderTypeFacet()

        return """
            |* `${property.name}`:
            |  The type of `${property.name}` is ${typeName}.
            |  `${property.name}` is ${if (property.required) "required" else "optional"}.
            |  <<${description.escapeAll()}>>
        """.trimMargin().keepAngleIndent()
    }

    private fun AnyType.renderTypeFacet(): String {
        return when (this) {
            is ArrayType -> this.renderArrayType()
            is UnionType -> this.renderUnionType()
            is ObjectType -> this.name
            is NumberType -> this.renderNumberType()
            else -> this.name}
    }

    private fun Method.returnType(): AnyType? {
        return this.responses
                .filter { it.isSuccessfull() }
                .firstOrNull { it.bodies?.isNotEmpty() ?: false }
                ?.let { it.bodies[0].type }
    }

    private fun UnionType.renderUnionType(): String {
        return this.oneOf.joinToString(", ", "one of ") { when(it) { is ArrayType -> "${it.items.name}[]" else -> it.name } }
    }

    private fun ArrayType.renderArrayType(): String {
        return "an array of ${this.items.renderTypeFacet()}"
    }

    private fun NumberType.renderNumberType(): String {
        val name = this.name ?: BuiltinType.of(this.eClass()).get().getName()
        return if (name == "number" && this.format.literal.findAnyOf(listOf("int", "long")) != null) "integer" else name
    }
}
package io.vrap.codegen.languages.postman.model

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.damnhandy.uri.template.UriTemplateComponent
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance
import org.eclipse.emf.ecore.EObject

class PostmanUrl (private val resource: Resource, private val method: Method, val renameParam: Function2<Resource, String, String>) {

    fun host(): String {
        return "{{host}}"
    }

    private fun transformUri(resource: Resource, uriComponent: UriTemplateComponent): String {
        if (uriComponent is Expression && uriComponent.varSpecs.size == 1) {
            val paramName = uriComponent.varSpecs.first().value
            val uriParameter = resource.uriParameters.find { it.name.equals(paramName) && it.type?.getAnnotation("paramName")  != null }
            val param = if (uriParameter != null) uriParameter.type.getAnnotation("paramName").value.value else renameParam(resource, paramName)
            return "{{${param}}}"
        }
        else return uriComponent.toString()
    }

    private fun postmanUrlPath(): String {
        return postmanUrlPath(resource).joinToString("")
    }

    private fun postmanUrlPath(eObject: EObject): List<String> {
        return when (eObject) {
            is Resource -> {
                val pathes = postmanUrlPath(eObject.eContainer())
                pathes.plus(eObject.relativeUri.components.joinToString("") { uriTemplateComponent -> transformUri(eObject, uriTemplateComponent) })
            }
            else ->
                emptyList()
        }
    }


    fun raw(): String {
        return "${host()}${postmanUrlPath()}"
    }

    fun path(drop: Int = 1): String {
        return postmanUrlPath().split("/").drop(drop).joinToString("\",\n\"")
    }
    fun pathVars(drop: Int = 1): String {
        val regex = Regex(".*\\{\\{(.+)}}.*")
        val parts = postmanUrlPath().split("/").drop(drop).map { it.trim() }.mapNotNull { segment ->
            val groups = regex.matchEntire(segment)?.groupValues
            // If the segment matches our regex, extract the key and return the zod schema string.
            groups?.get(1)?.let { key ->
                "\"$key\": z.string()"
            }
        }
        return parts.joinToString(",\n")
    }


    fun query(): String {
        return method.queryParameters.joinToString(",\n") { it.queryParam() }
    }

    fun querySchema(): String {
        return method.queryParameters.filterNot { it.name.startsWith("/") }.joinToString(",\n") { it.queryParamSchema() }
    }

    private fun QueryParameter.queryParam() :   String {
        return """
            |{
            |    "key": "${this.name}",
            |    "value": "${this.defaultValue()}",
            |    "equals": true,
            |    "disabled": ${this.required.not()}
            |}
        """.trimMargin()
    }


    private fun QueryParameter.queryParamSchema() : String {
        if (this.name.startsWith("/")) {
            return ""
        }
        val optional = if (this.required.not()) ".optional()" else ""
        val description = (this.type.description?.value?.let { ".describe(\"${it.replace("\n", " ")}\") " } ?: "").replace(Regex("\\[(.*?)\\]\\([^)]*\\)"), "$1")

//        val min = if (this.type.minimum != null) ".min(${this.type})" else ""
//        val max = if (this.type.maximum != null) ".max(${this.type.maximum})" else ""


//        val default = if (this.defaultValue().isNotEmpty()) ".default('${this.defaultValue()}')" else ""
        return """
            | "${this.name}": z.string()$optional$description
        """.trimMargin()
    }

    fun QueryParameter.defaultValue(): String {
        if (this.name == "version") {
            return "{{" + this.getParent(Resource::class.java)?.resourcePathName?.singularize() + "-version}}"
        }
        val defaultValue = this.getAnnotation("postman-default-value")
        if (defaultValue != null && defaultValue.value is StringInstance) {
            val value = (defaultValue.value.value as String).replace("{{", "").replace("}}", "")

            return "{{" + this.getParent(Resource::class.java)?.resourcePathName?.singularize() + "-" + value + "}}"
        }

        return ""
    }
}

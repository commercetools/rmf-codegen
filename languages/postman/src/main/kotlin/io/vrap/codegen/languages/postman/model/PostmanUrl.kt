package io.vrap.codegen.languages.postman.model

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.damnhandy.uri.template.UriTemplateComponent
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance

class PostmanUrl (private val resource: Resource, private val method: Method, val renameParam: Function1<String, String>) {

    fun host(): String {
        return "{{host}}"
    }

    private fun transformUri(uriComponent: UriTemplateComponent): String {
        if (uriComponent is Expression && uriComponent.varSpecs.size == 1) {
            val paramName = uriComponent.varSpecs.first().value
            val uriParameter = resource.uriParameters.find { it.name.equals(paramName) && it.type?.getAnnotation("paramName")  != null }
            val param = if (uriParameter != null) uriParameter.type.getAnnotation("paramName").value.value else renameParam(paramName)
            return "{{${param}}}"
        }
        else return uriComponent.toString()
    }

    private fun postmanUrlPath(): String {
        return resource.fullUri.components.joinToString("", transform = this::transformUri)
    }

    fun raw(): String {
        return "${host()}${postmanUrlPath()}"
    }

    fun path(): String {
        return postmanUrlPath().split("/").drop(1).joinToString("\",\n\"")
    }

    fun query(): String {
        return method.queryParameters.joinToString(",\n") { it.queryParam() }
    }

    private fun QueryParameter.queryParam() : String {
        return """
            |{
            |    "key": "${this.name}",
            |    "value": "${this.defaultValue()}",
            |    "equals": true,
            |    "disabled": ${this.required.not()}
            |}
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
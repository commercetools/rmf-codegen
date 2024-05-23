package io.vrap.codegen.languages.bruno.model

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplateComponent
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance
import org.eclipse.emf.ecore.EObject

class BrunoUrl (private val resource: Resource, private val method: Method, val renameParam: Function2<Resource, String, String>) {

    fun host(): String {
        return "{{apiUrl}}"
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

    fun query(): String {
        return method.queryParameters.joinToString("\n") { it.queryParam() }
    }

    private fun QueryParameter.queryParam() : String {
        val disabled = if (this.required) "" else "~"
        return "${disabled}${this.name}: ${this.defaultValue()}"
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

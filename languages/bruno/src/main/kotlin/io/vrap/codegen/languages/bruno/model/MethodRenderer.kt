package io.vrap.codegen.languages.bruno.model

import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.apache.commons.text.StringEscapeUtils

class MethodRenderer {
    fun render(method: Method): String {

        val url = BrunoUrl(method.resource(), method) { resource, name -> when (name) {
            "ID" -> resource.resourcePathName.singularize() + "-id"
            "key" -> resource.resourcePathName.singularize() + "-key"
            else -> StringCaseFormat.LOWER_HYPHEN_CASE.apply(name)
        }}
        return """
            |{
            |    meta {
            |      name: "${method.displayName?.value ?: "${method.methodName} ${method.resource().toResourceName()}" }" 
            |      type: http
            |      seq: 1
            |    }
            |    
            |    ${method.methodName} {
            |      url: "${url.raw()}"
            |      body: ${method.bodyType()}
            |      auth: ${authType()} 
            |    }
            |    
            |    <<${auth()}>>
            |    
            |    <<${method.jsonBody()}>>
            |    
            |    query {
            |       <<${url.query()}>>
            |    }
            |    
            |}
        """.trimMargin()
    }

    fun Method.bodyType(): String {
        return if (this.getExample() != null) "json" else "none"
    }

    fun Method.jsonBody(): String {
        val s = this.getExample()
        return if (s != null) {
            "body:json ${s}"
        } else ""
    }

    fun Method.getExample(): String? {
        val s = this.bodies?.
        getOrNull(0)?.
        type?.
        examples?.
        getOrNull(0)?.
        value
        return s?.toJson()
    }
}

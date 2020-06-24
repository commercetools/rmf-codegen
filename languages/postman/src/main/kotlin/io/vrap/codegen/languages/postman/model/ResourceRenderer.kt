package io.vrap.codegen.languages.postman.model

import com.damnhandy.uri.template.Literal
import com.damnhandy.uri.template.UriTemplateComponent
import io.vrap.rmf.raml.model.resources.Resource

class ResourceRenderer {
    fun render(resource: Resource): String {

        if ((resource.relativeUri.template.contains(resource.resourcePathName).not() && resource.relativeUriParameters.size > 0 ) || resource.resources.size == 0) {
            return resource.resources.map { ResourceRenderer().render(it) }.plus(resource.methods.map { MethodRenderer().render(it) }).joinToString(",\n")
        }
        return """
            |{
            |    "name": "${resource.displayName?.value ?: resource.resourcePathName.capitalize()}",
            |    "description": "${resource.description?.description()}",
            |    "item": [
            |        <<${resource.resources.map { ResourceRenderer().render(it) }.plus(resource.methods.map { MethodRenderer().render(it) }).joinToString(",\n") }>>
            |    ]
            |}
        """.trimMargin()

//        |    "event": [
//        |        {
//            |            "listen": "test",
//            |            "script": {
//            |                "type": "text/javascript",
//            |                "exec": [
//            |                    <<${if (resource.items.isNotEmpty()) resource.items.first().testScript("") else ""}>>
//            |                ]
//            |            }
//            |        }
//        |    ]

    }
}

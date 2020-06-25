package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.raml.model.resources.Resource

class ResourceRenderer {
    fun render(resource: Resource): String {

        val items = resource.resources.map { ResourceRenderer().render(it) }
                .plus(resource.methods.map { MethodRenderer().render(it) })
                .plus(ActionRenderer().render(resource))

        if ((resource.relativeUri.template.contains(resource.resourcePathName).not() && resource.relativeUriParameters.size > 0) || resource.resources.size == 0) {
            return items.joinToString(",\n")
        }
        return """
            |{
            |    "name": "${resource.displayName?.value ?: resource.resourcePathName.capitalize()}",
            |    "description": "${resource.description?.description()}",
            |    "item": [
            |        <<${items.joinToString(",\n")}>>
            |    ]
            |}
        """.trimMargin()
    }
}

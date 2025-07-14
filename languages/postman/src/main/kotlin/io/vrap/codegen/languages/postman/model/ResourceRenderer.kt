package io.vrap.codegen.languages.postman.model

import io.vrap.codegen.languages.extensions.deprecated
import io.vrap.codegen.languages.extensions.markDeprecated
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.BooleanInstance

class ResourceRenderer {
    fun render(resource: Resource): String {

        val items = resource.resources.filterNot { res -> res.deprecated() || res.markDeprecated() }.map { ResourceRenderer().render(it) }
                .plus(resource.methods.filterNot { method -> method.deprecated() || method.markDeprecated() }.map { MethodRenderer().render(it) })
                .plus(ActionRenderer().render(resource))

        if ((resource.relativeUri.template.contains(resource.resourcePathName).not() && resource.relativeUriParameters.size > 0) || resource.resources.size == 0) {
            return items.joinToString(",\n")
        }
        return """
            |{
            |    "name": "${resource.displayName?.value ?: resource.resourcePathName.firstUpperCase()}",
            |    "description": "${resource.description?.description()}",
            |    "item": [
            |        <<${items.joinToString(",\n")}>>
            |    ]
            |}
        """.trimMargin()
    }

    private fun Resource.deprecated() : Boolean {
        val anno = this.getAnnotation("deprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    private fun Resource.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

}

package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.BooleanInstance

class ResourceRenderer {
    fun render(resource: Resource): String {

        val maintainedItems = resource.resources.filterNot { res -> res.deprecated() || res.markDeprecated() }
        val renderedItems = maintainedItems.map { ResourceRenderer().render(it) }
        val renderedMethods = resource.methods.map { MethodRenderer().render(it) }
        val renderedActions = ActionRenderer().render(resource)

        val items = renderedItems
                .plus(renderedMethods)
                .plus(renderedActions)

        if ((resource.relativeUri.template.contains(resource.resourcePathName).not() && resource.relativeUriParameters.size > 0) || resource.resources.size == 0) {
            return items.joinToString(",\n")
        }
        return """
            |{
            |    "name": "${resource.displayName?.value ?: resource.resourcePathName.firstUpperCase()}",
            |    "description": "${resource.description?.description()}",
            |    "tools": [
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

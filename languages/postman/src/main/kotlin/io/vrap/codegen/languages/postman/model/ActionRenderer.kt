package io.vrap.codegen.languages.postman.model

import io.vrap.codegen.languages.extensions.resource
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.UnionType
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.util.*

class ActionRenderer {
    fun render(resource: Resource): List<String> {
        if (resource.getAnnotation("updateable") == null) return emptyList()

        val updateMethod = resource.getUpdateMethod()

        val actions = updateMethod?.getActions()?.map { renderAction(resource, updateMethod, it) } ?: return emptyList()

        return listOf("""
            |{
            |    "name": "Update actions",
            |    "item": [
            |        <<${actions.joinToString(",\n") }>>
            |    ]
            |}
        """.trimMargin())
    }

    private fun renderAction(resource: Resource, method: Method, type: ObjectType): String {
        val url = PostmanUrl(method.resource(), method) { name -> name }
        return """
            |{
            |    "name": "${type.discriminatorValue.capitalize()}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${ ""/*item.testScript("") */}>>
            |                ]
            |            }
            |        }
            |    ],
            |    "request": {
            |        "auth":
            |            <<${auth()}>>,
            |        "method": "${method.methodName}",
            |        "body": {
            |            "mode": "raw",
            |            "raw": "${ "" /*actionExample(item).escapeJson().escapeAll()*/}"
            |        },
            |        "header": [
            |            {
            |                "key": "Content-Type",
            |                "value": "application/json"
            |            }
            |        ],
            |        "url": {
            |            "raw": "${url.raw()}",
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${url.path()}">>,
            |                "{{${resource.resourcePathName.singularize()}-id}}"
            |            ],
            |            "query": [
            |                <<${url.query()}>>
            |            ]
            |        },
            |        "description": "${method.description()}"
            |    },
            |    "response": []
            |}
        """.trimMargin()
    }

    private fun Resource.getUpdateMethod(): Method? {
        val byIdResource = this.resources.find { resource -> resource.relativeUri.template == "/{ID}" } ?: return null

        return byIdResource.getMethod(HttpMethod.POST)
    }

    private fun Method.getActions(): List<ObjectType> {
        val body = this.getBody("application/json") ?: return emptyList()

        val actions = (body.type as ObjectType).getProperty("actions") ?: return emptyList()

        val actionsType = actions.type as ArrayType
        val updateActions = if (actionsType.items is UnionType) {
                (actionsType.items as UnionType).oneOf[0].subTypes
            } else {
                actionsType.items.subTypes
            }
        val actionItems = updateActions.map { action -> action as ObjectType }.sortedBy { action -> action.discriminatorValue }
        return actionItems
    }

    fun Method.description(): String {
        val name = StringCaseFormat.UPPER_CAMEL_CASE.apply(Optional.ofNullable(this.resource().displayName).map<String> { it.value }.orElse(this.resource().resourcePathName))
        val description = Optional.ofNullable(this.description).map<String> { it.value }.orElse(this.method.getName() + " " + name)
        return description.escapeJson().escapeAll() ?:""
    }
}

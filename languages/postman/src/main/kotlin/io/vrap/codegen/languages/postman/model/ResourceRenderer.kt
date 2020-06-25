package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.raml.model.resources.Resource

class ResourceRenderer {
    fun render(resource: Resource): String {

        val items = resource.resources.map { ResourceRenderer().render(it) }
                .plus(resource.methods.map { MethodRenderer().render(it) })
                .plus(ActionRenderer().render(resource))

        if ((resource.relativeUri.template.contains(resource.resourcePathName).not() && resource.relativeUriParameters.size > 0 ) || resource.resources.size == 0) {
            return items.joinToString(",\n")
        }
        return """
            |{
            |    "name": "${resource.displayName?.value ?: resource.resourcePathName.capitalize()}",
            |    "description": "${resource.description?.description()}",
            |    "item": [
            |        <<${items.joinToString(",\n") }>>
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

//    fun Resource.getActionItems(method: Method, template: KFunction1<ItemGenModel, String>): List<ActionGenModel> {
//        val actionItems = Lists.newArrayList<ActionGenModel>()
//
//        val body = method.getBody("application/json")
//        if (body != null && body.type is ObjectType) {
//            val actions = (body.type as ObjectType).getProperty("actions")
//            if (actions != null) {
//                val actionsType = actions.type as ArrayType
//                val updateActions: List<AnyType>
//                if (actionsType.items is UnionType) {
//                    updateActions = (actionsType.items as UnionType).oneOf[0].subTypes
//                } else {
//                    updateActions = actionsType.items.subTypes
//                }
//                for (action in updateActions) {
//                    actionItems.add(ActionGenModel(action as ObjectType, this, method))
//                }
//                actionItems.sortBy { actionGenModel -> actionGenModel.discriminatorValue }
//            }
//        }
//
//        return actionItems
//    }
}

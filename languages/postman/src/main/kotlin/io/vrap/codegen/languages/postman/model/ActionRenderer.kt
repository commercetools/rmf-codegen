package io.vrap.codegen.languages.postman.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.io.IOException
import java.util.*

class ActionRenderer {
    fun render(resource: Resource): List<String> {
        if (resource.getAnnotation("updateable") == null) return emptyList()

        val updateMethod = resource.getUpdateMethod()

        return updateMethod?.getActions()?.filterNot { objType -> objType.deprecated() }?.map { renderAction(resource, updateMethod, it) } ?: return emptyList()

    }

    private fun renderAction(resource: Resource, method: Method, type: ObjectType): String {

        val resourceName = method.resource().toResourceName().removePrefix("ByProjectKey")
        val toolName = type.discriminatorValue.firstUpperCase().split(" ") ?.joinToString("") { word ->
            word.replaceFirstChar { it.uppercase() }
        }?.replaceFirstChar { it.lowercaseChar() } ?: resourceName.replaceFirstChar { it.lowercaseChar() }
        val requestMethod = method.methodName.uppercase()

        val description = method.description?.description() ?: method.displayName?.value
        val url = PostmanUrl(method.resource(), method) { resource, name -> when (name) {
            "ID" -> resource.resourcePathName.singularize() + "-id"
            "key" -> resource.resourcePathName.singularize() + "-key"
            else -> StringCaseFormat.LOWER_HYPHEN_CASE.apply(name)
        }}
        val actionType = when (requestMethod) {
            "GET" -> "read"
            "HEAD" -> "read"
            "POST" -> "write"
            "PUT" -> "write"
            "DELETE" -> "delete"
            else -> "read"
        }

        return """
            |{
            |    "name": "${toolName}Tool",
            |    "actionType": "${actionType}",
            |    "tool": tool(
            |        async (args: any) => {
            |            try {
            |                const method: string = "${requestMethod}";
            |                let body = {}
            |                const bodyStr = args.postRequestParams;
            |                if (method === "POST" && bodyStr) {
            |                  try {
            |                    JSON.parse(bodyStr);
            |                  } catch {
            |                    return "Invalid body value: "+ body
            |                  }
            |                  body = { body: bodyStr };
            |                }
            |                let url = [apiUrl, projectKey, "${url.path(2)}"].join("/");
            |                Object.entries(args.pathVariables || {}).forEach(([key, value]: any) => {
            |                           url = url.replace(`{{\$\{key}}}`, value);
            |                         })
            |                const headers = { "Content-Type": "application/json", "Authorization": "Bearer " + accessToken };
            |                const response = await fetch(url, {
            |                  method,
            |                  headers,
            |                  ...body
            |                });
            |                const jsonData = await response.text();
            |                return jsonData;
            |            } catch (error) {
            |                return `Fetch Error:` + (error as Error).message;
            |            }
            |        }, {
            |        name: "${type.discriminatorValue.firstUpperCase()}${if (type.markDeprecated()) " (deprecated)" else ""}",
            |        description: "${description}",
            |        schema: z.object({
            |            "query": z.object({
            |                <<${url.querySchema()}>>
            |            }).optional(),
            |            "pathVariables": z.object({
            |                <<${url.pathVars(2)}>>
            |            }).optional(),
            |            <<${this.bodySchema(method.bodies[0].type as ObjectType)}>>
            |        })
            |    })
            |}
        """.trimIndent()
        return """
            |{
            |    "name": "${type.discriminatorValue.firstUpperCase()}${if (type.markDeprecated()) " (deprecated)" else ""}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${resource.testScript()}>>
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
            |            "raw": "${resource.actionExample(type).escapeJson().escapeAll()}"
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
            |                <<"${url.path()}">>
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

    private fun ObjectType.deprecated() : Boolean {
        val anno = this.getAnnotation("deprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    private fun ObjectType.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }


    private fun Resource.actionExample(type: ObjectType): String {
        val example = getExample(type)
        return """
            |{
            |    "version": {{${this.resourcePathName.singularize()}-version}},
            |    "actions": [
            |        <<${if (example.isNullOrEmpty().not()) example else """
            |        |{
            |        |    "action": "${type.discriminatorValue}"
            |        |}""".trimMargin()}>>
            |    ]
            |}
        """.trimMargin().keepAngleIndent()
    }


    private fun bodySchema(type: ObjectType): String {
        val example = this.getExample(type)?.escapeAll()?:""

        if (example.isEmpty()) {
            return ""
        }

        return  "\"postRequestParams\": z.string().describe(`String value for the request JSON payload. example value (all fields are optional): ${example}`)"
    }
    private fun getExample(type: ObjectType): String? {
        var example: String? = null
        var instance: Instance? = null

        if (type.getAnnotation("postman-example") != null) {
            instance = type.getAnnotation("postman-example").value
        } else if (type.examples.size > 0) {
            instance = type.examples[0].value
        }

        if (instance != null) {
            example = instance.toJson()
            try {
                val mapper = ObjectMapper()
                val nodes = mapper.readTree(example) as ObjectNode
                nodes.put("action", type.discriminatorValue)

                example = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodes)
                        .split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().map { s -> "  $s" }
                        .joinToString("\n")
                        .trim { it <= ' ' }
            } catch (e: IOException) {
            }

        }

        return example
    }

//    private fun getTestScript(type: ObjectType): String? {
//        val t = type.getAnnotation("postman-test-script")
//        if (t != null) return (t.value as StringInstance).value
//
//        return null
//    }

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
        val name = StringCaseFormat.UPPER_CAMEL_CASE.apply(Optional.ofNullable(this.resource().displayName).map { it.value }.orElse(this.resource().resourcePathName))
        val description = Optional.ofNullable(this.description).map { it.value }.orElse(this.method.getName() + " " + name)
        return description.escapeJson().escapeAll()
    }
}

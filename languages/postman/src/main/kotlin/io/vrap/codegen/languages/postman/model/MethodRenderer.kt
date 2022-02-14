package io.vrap.codegen.languages.postman.model

import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.apache.commons.text.StringEscapeUtils

class MethodRenderer {
    fun render(method: Method): String {

        val url = PostmanUrl(method.resource(), method) { resource, name -> when (name) {
            "ID" -> resource.resourcePathName.singularize() + "-id"
            "key" -> resource.resourcePathName.singularize() + "-key"
            else -> StringCaseFormat.LOWER_HYPHEN_CASE.apply(name)
        }}
        return """
            |{
            |    "name": "${method.displayName?.value ?: "${method.methodName} ${method.resource().toResourceName()}" }",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${method.resource().testScript()}>>
            |                ]
            |            }
            |        }
            |    ],
            |    "request": {
            |        "auth":
            |            <<${auth()}>>,
            |        "method": "${method.methodName}",
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
            |        "description": "${method.description?.description()}",
            |        "body": {
            |            "mode": "raw",
            |            "raw": "${method.rawBody()}"
            |        }
            |    },
            |    "response": []
            |}
        """.trimMargin()
    }

    fun Method.getExample(): String? {
        val s = this.bodies?.
        getOrNull(0)?.
        type?.
        examples?.
        getOrNull(0)?.
        value
        return StringEscapeUtils.escapeJson(s?.toJson())
    }

    fun Method.rawBody(): String {
        return this.getExample()?.escapeAll()?:""
    }
}

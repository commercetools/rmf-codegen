package io.vrap.codegen.languages.postman.model

import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import org.apache.commons.lang3.StringEscapeUtils

class MethodRenderer {
    fun render(method: Method): String {

        val url = PostmanUrl(method.resource(), method) { name -> name }
        return """
            |{
            |    "name": "${method.methodName} ${method.resource().toResourceName()}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${this.testScript(method.resource())}>>
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
            |                "raw": "${url.raw()}",
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

    fun testScript(resource: Resource, param: String = ""): String {
        return """
            |tests["Status code " + responseCode.code] = responseCode.code === 200 || responseCode.code === 201;
            |var data = JSON.parse(responseBody);
            |if(data.results && data.results[0] && data.results[0].id && data.results[0].version){
            |    pm.environment.set("${resource.resourcePathName.singularize()}-id", data.results[0].id); 
            |    pm.environment.set("${resource.resourcePathName.singularize()}-version", data.results[0].version);
            |}
            |if(data.results && data.results[0] && data.results[0].key){
            |    pm.environment.set("${resource.resourcePathName.singularize()}-key", data.results[0].key); 
            |}
            |if(data.version){
            |    pm.environment.set("${resource.resourcePathName.singularize()}-version", data.version);
            |}
            |if(data.id){
            |    pm.environment.set("${resource.resourcePathName.singularize()}-id", data.id); 
            |}
            |if(data.key){
            |    pm.environment.set("${resource.resourcePathName.singularize()}-key", data.key);
            |}
            |${if (param.isNotEmpty()) """
            |if(data.${param}){
            |    pm.environment.set("${resource.resourcePathName.singularize()}-${param}", data.${param});
            |}
            """.trimMargin() else ""}
        """.trimMargin().split("\n").map { it.escapeJson().escapeAll() }.joinToString("\",\n\"", "\"", "\"")
    }
}

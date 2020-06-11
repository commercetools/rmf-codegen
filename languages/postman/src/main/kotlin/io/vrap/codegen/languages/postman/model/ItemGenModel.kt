package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.apache.commons.lang3.StringEscapeUtils
import java.util.*
import kotlin.reflect.KFunction1

open class ItemGenModel(private val resource: Resource, private val method: Method, val renameParam: Function2<ItemGenModel, String, String> = { item, name -> name }) {

    val url = PostmanUrl(resource, method) { renameParam(this, it) }

    fun method() = method.methodName.toUpperCase()

    val name: String
        get() =
            StringCaseFormat.UPPER_CAMEL_CASE.apply(Optional.ofNullable(resource.displayName).map<String> { it.value }.orElse(resource.resourcePathName))

    open val description: String
        get() {
            val description = Optional.ofNullable(method.description).map<String> { it.value }.orElse(method.method.getName() + " " + name)
            return StringEscapeUtils.escapeJson(description)
        }

    fun description(): String {
        return description.escapeJson()?.escapeAll()?:""
    }

    fun simpleDescription(): String {
        val simpleDescription = if (description.contains(".")) description.substring(0, description.indexOf(".")) else description
        return simpleDescription.escapeJson()?.escapeAll()?:""
    }


    val resourcePathName: String
        get() {
            val resourcePathName = resource.resourcePathName

            return if (resourcePathName.isEmpty()) {
                resource.displayName.value.toLowerCase()
            } else resourcePathName
        }

    open fun getExample(): String? {
        val s = method.bodies?.
                getOrNull(0)?.
                type?.
                examples?.
                getOrNull(0)?.
                value
        return StringEscapeUtils.escapeJson(s?.toJson())
    }

    fun singularName(): String {
        return resource.resourcePathName.singularize()
    }

    fun rawBody(): String {
        return getExample()?.escapeAll()?:""
    }

    fun testScript(param: String): String {
        return """
            |tests["Status code " + responseCode.code] = responseCode.code === 200 || responseCode.code === 201;
            |var data = JSON.parse(responseBody);
            |if(data.version){
            |    pm.environment.set("${this.resourcePathName.singularize()}-version", data.version);
            |}
            |if(data.id){
            |    pm.environment.set("${this.resourcePathName.singularize()}-id", data.id); 
            |}
            |if(data.key){
            |    pm.environment.set("${this.resourcePathName.singularize()}-key", data.key);
            |}
            |${if (param.isNotEmpty()) """
            |if(data.${param}){
            |    pm.environment.set("${this.resourcePathName.singularize()}-${param}", data.${param});
            |}
            """.trimMargin() else ""}
            |${if (this is ActionGenModel && this.testScript.isNullOrEmpty().not()) this else ""}
        """.trimMargin().split("\n").map { it.escapeJson().escapeAll() }.joinToString("\",\n\"", "\"", "\"")
    }

    fun render(param: String): String {
        return """
            |{
            |    "name": "${this.simpleDescription()}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${this.testScript(param)}>>
            |                ]
            |            }
            |        }
            |    ],
            |    "request": {
            |        "auth":
            |            <<${auth()}>>,
            |        "method": "${this.method()}",
            |        "header": [
            |            {
            |                "key": "Content-Type",
            |                "value": "application/json"
            |            }
            |        ],
            |        "body": {
            |            "mode": "raw",
            |            "raw": "${this.rawBody()}"
            |        },
            |        "url": {
            |                "raw": "${this.url.raw()}",
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${this.url.path()}">>
            |            ],
            |            "query": [
            |                <<${this.url.query()}>>
            |            ]
            |        },
            |        "description": "${this.description()}"
            |    },
            |    "response": []
            |}
        """.trimMargin()
    }
}
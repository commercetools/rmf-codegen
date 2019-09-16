package io.vrap.codegen.languages.postman.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.google.common.collect.Lists
import com.google.inject.Inject
import com.hypertino.inflector.English
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Parameter
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.security.OAuth20Settings
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.apache.commons.lang3.StringEscapeUtils
import org.apache.commons.lang3.StringUtils
import org.eclipse.emf.ecore.EObject
import java.io.IOException
import java.net.URI
import java.util.*
import kotlin.reflect.KFunction1

class PostmanModuleRenderer @Inject constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                template(api),
                collection(api),
                readmeMd()
        )
    }

    private fun readmeMd(): TemplateFile {
        return TemplateFile(relativePath = "README.md",
                content = readme())
    }

    private fun template(api: Api): TemplateFile {
        return TemplateFile(relativePath = "template.json",
                content = """
                    |{
                    |  "id": "5bb74f05-5e78-4aee-b59e-492c947bc160",
                    |  "name": "commercetools platform API (generated).template",
                    |  "values": [
                    |    {
                    |      "enabled": true,
                    |      "key": "host",
                    |      "value": "${api.baseUri.template}",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "auth_url",
                    |      "value": "${api.oauth().uri().host}",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "ctp_client_id",
                    |      "value": "<your-client-id>",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "ctp_client_secret",
                    |      "value": "<your-client-secret>",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "ctp_access_token",
                    |      "value": "<your_access_token>",
                    |      "type": "text"
                    |    }
                    |  ]
                    |}
                """.trimMargin()
        )
    }

    private fun collection(api: Api): TemplateFile {
        return TemplateFile(relativePath = "collection.json",
                content = """
                    |{
                    |    "info": {
                    |        "_postman_id": "f367b534-c9ea-e7c5-1f46-7a27dc6a30ba",
                    |        "name": "commercetools platform API (generated)",
                    |        "description": "${readme().escapeJson().escapeAll()}",
                    |        "schema": "https://schema.getpostman.com/json/collection/v2.0.0/collection.json"
                    |    },
                    |    "auth":
                    |        <<${auth()}>>,
                    |    "item": [
                    |        <<${authorization(api.oauth())}>>,
                    |        <<${api.resources().joinToString(",") { folder(it) }}>>
                    |    ]
                    |}
                """.trimMargin().keepIndentation("<<",">>"))
    }

    private fun folder(resource: ResourceModel): String {
        if (resource.name() == "InStore") {
            return """
                |{
                |    "name": "${resource.name()}",
                |    "description": "${resource.description()?.escapeJson()?.escapeAll()}",
                |    "item": [
                |        <<${resource.resource.resources.joinToString(",\n") { folder(ResourceModel(it, it.items())) }}>>
                |    ]
                |}
            """.trimMargin()
        }
        if (resource.name() == "Me") {
            return """
                |{
                |    "name": "${resource.name()}",
                |    "description": "${resource.description()?.escapeJson()?.escapeAll()}",
                |    "item": [
                |        <<${resource.resource.resources.joinToString(",\n") { folder(ResourceModel(it, it.items())) }}>>${if (resource.items.isNotEmpty()) "," else ""}
                |        <<${resource.items.joinToString(",\n") { it.template(it) } }>>
                |    ]
                |}
            """.trimMargin()
        }
        return """
            |{
            |    "name": "${resource.name()}",
            |    "description": "${resource.description()?.escapeJson()?.escapeAll()}",
            |    "item": [
            |        <<${resource.items.joinToString(",") { it.template(it) } }>>
            |    ],
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${if (resource.items.isNotEmpty()) testScript(resource.items.first(), "") else ""}>>
            |                ]
            |            }
            |        }
            |    ]
            |}
        """.trimMargin()
    }

    private fun auth(): String {
        return """
            |{
            |    "type": "oauth2",
            |    "oauth2": {
            |        "accessToken": "{{ctp_access_token}}",
            |        "addTokenTo": "header",
            |        "tokenType": "Bearer"
            |    }
            |}
            """.trimMargin()
    }

    private fun queryTestScript(item: ItemGenModel, param: String): String {

        return """
            |tests["Status code is 200"] = responseCode.code === 200;
            |var data = JSON.parse(responseBody);
            |if(data.results && data.results[0] && data.results[0].id && data.results[0].version){
            |    pm.environment.set("${item.resourcePathName.singularize()}-id", data.results[0].id); 
            |    pm.environment.set("${item.resourcePathName.singularize()}-version", data.results[0].version);
            |}
            |if(data.results && data.results[0] && data.results[0].key){
            |    pm.environment.set("${item.resourcePathName.singularize()}-key", data.results[0].key); 
            |}
            |${if (item is ActionGenModel && item.testScript.isNullOrEmpty().not()) item.testScript else ""}
        """.trimMargin().split("\n").map { it.escapeJson().escapeAll() }.joinToString("\",\n\"", "\"", "\"")
    }

    private fun testScript(item: ItemGenModel, param: String): String {

        return """
            |tests["Status code " + responseCode.code] = responseCode.code === 200 || responseCode.code === 201;
            |var data = JSON.parse(responseBody);
            |if(data.version){
            |    pm.environment.set("${item.resourcePathName.singularize()}-version", data.version);
            |}
            |if(data.id){
            |    pm.environment.set("${item.resourcePathName.singularize()}-id", data.id); 
            |}
            |if(data.key){
            |    pm.environment.set("${item.resourcePathName.singularize()}-key", data.key);
            |}
            |${if (param.isNotEmpty()) """
            |if(data.${param}){
            |    pm.environment.set("${item.resourcePathName.singularize()}-${param}", data.${param});
            |}
            """.trimMargin() else ""}
            |${if (item is ActionGenModel && item.testScript.isNullOrEmpty().not()) item.testScript else ""}
        """.trimMargin().split("\n").map { it.escapeJson().escapeAll() }.joinToString("\",\n\"", "\"", "\"")
    }

    private fun query(item: ItemGenModel): String {
        return """
            |{
            |    "name": "Query ${item.name}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${queryTestScript(item, "")}>>
            |                ]
            |            }
            |        }
            |    ],
            |    "request": {
            |        "auth":
            |            <<${auth()}>>,
            |        "method": "${item.method.methodName.toUpperCase()}",
            |        "header": [
            |            {
            |                "key": "Content-Type",
            |                "value": "application/json"
            |            }
            |        ],
            |        "body": {
            |            "mode": "raw",
            |            "raw": ""
            |        },
            |        "url": {
            |            "raw": "{{host}}/${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}")}",
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}").split("/").map { it.trim('/') }.joinToString("\",\n\"")}">>
            |            ],
            |            "query": [
            |                <<${if (item.queryParameters.isNotEmpty()) item.queryParameters.joinToString(",\n") { it.queryParam() } else ""}>>
            |            ]
            |        },
            |        "description": "${item.description.escapeJson().escapeAll()}"
            |    },
            |    "response": []
            |}
        """.trimMargin()
    }

    private fun create(item: ItemGenModel): String {
        return """
            |{
            |    "name": "Create ${item.name.singularize()}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${testScript(item, "")}>>
            |                ]
            |            }
            |        }
            |    ],
            |    "request": {
            |        "auth":
            |           <<${auth()}>>,
            |        "method": "${item.method.methodName.toUpperCase()}",
            |        "header": [
            |            {
            |                "key": "Content-Type",
            |                "value": "application/json"
            |            }
            |        ],
            |        "body": {
            |            "mode": "raw",
            |            "raw": "${if (item.getExample().isNullOrEmpty().not()) item.getExample()!!.escapeJson() else ""}"
            |        },
            |        "url": {
            |            "raw": "{{host}}/${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}")}",
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}").split("/").map { it.trim('/') }.joinToString("\",\n\"")}">>
            |            ],
            |            "query": [
            |                <<${if (item.queryParameters.isNotEmpty()) item.queryParameters.joinToString(",\n") { it.queryParam() } else ""}>>
            |            ]
            |        },
            |        "description": "${item.description.escapeJson().escapeAll()}"
            |    },
            |    "response": []
            |}
        """.trimMargin()
    }

    private fun getByID(item: ItemGenModel): String {
        return getByParam(item, "", true)
    }

    private fun getByKey(item: ItemGenModel): String {
        return getByParam(item, "key", true)
    }

    private fun getByParam(item: ItemGenModel, param: String, by: Boolean): String {
        return """
            |{
            |    "name": "Get ${item.name.singularize()}${if (by) " by ${if (param.isNotEmpty()) param else "ID"}" else ""}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${testScript(item, param)}>>
            |                ]
            |            }
            |        }
            |    ],
            |    "request": {
            |        "auth":
            |            <<${auth()}>>,
            |        "method": "${item.method.methodName.toUpperCase()}",
            |        "header": [
            |            {
            |                "key": "Content-Type",
            |                "value": "application/json"
            |            }
            |        ],
            |        "body": {
            |            "mode": "raw",
            |            "raw": ""
            |        },
            |        "url": {
            |            ${if (param.isNotEmpty()) """
                            "raw": "{{host}}/${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}")}/${param}={{${item.resource.resourcePathName.singularize()}-${param}}}",
                            """.trimIndent() else """
                            "raw": "{{host}}/${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}")}/{{${item.resource.resourcePathName.singularize()}-id}}",
                            """.trimIndent()}
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}").split("/").map { it.trim('/') }.joinToString("\",\n\"")}">>,
            |                "${if (param.isNotEmpty()) "${param}={{${item.resource.resourcePathName.singularize()}-${param}}}" else "{{${item.resource.resourcePathName.singularize()}-id}}"}"
            |            ],
            |            "query": [
            |                <<${if (item.queryParameters.isNotEmpty()) item.queryParameters.joinToString(",\n") { it.queryParam() } else ""}>>
            |            ]
            |        },
            |        "description": "${item.description.escapeJson().escapeAll()}"
            |    },
            |    "response": []
            |}
        """.trimMargin()
    }

    private fun updateByID(item: ItemGenModel): String {
        return updateByParam(item, "", true)
    }

    private fun updateByKey(item: ItemGenModel): String {
        return updateByParam(item, "key", true)
    }

    private fun updateByParam(item: ItemGenModel, param: String, by: Boolean): String {
        return """
            |{
            |    "name": "Update ${item.name.singularize()}${if (by) " by ${if (param.isNotEmpty()) param else "ID"}" else ""}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${testScript(item, param)}>>
            |                ]
            |            }
            |        }
            |    ],
            |    "request": {
            |        "auth":
            |            <<${auth()}>>,
            |        "method": "${item.method.methodName.toUpperCase()}",
            |        "header": [
            |            {
            |                "key": "Content-Type",
            |                "value": "application/json"
            |            }
            |        ],
            |        "body": {
            |            "mode": "raw",
            |            "raw": "${if (item.getExample().isNullOrEmpty().not()) item.getExample()?.escapeAll() else """
                            |{
                            |    "version": {{${item.resource.resourcePathName.singularize()}-version}},
                            |    "actions": []
                            |}
                            """.trimMargin().escapeJson().escapeAll()}"
            |        },
            |        "url": {
            |            ${if (param.isNotEmpty()) """
                            "raw": "{{host}}/${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}")}/${param}={{${item.resource.resourcePathName.singularize()}-${param}}}",
                            """.trimIndent() else """
                            "raw": "{{host}}/${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}")}/{{${item.resource.resourcePathName.singularize()}-id}}",
                            """.trimIndent()}
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}").split("/").map { it.trim('/') }.joinToString("\",\n\"")}">>,
            |                "${if (param.isNotEmpty()) "${param}={{${item.resource.resourcePathName.singularize()}-${param}}}" else "{{${item.resource.resourcePathName.singularize()}-id}}"}"
            |            ],
            |            "query": [
            |                <<${if (item.queryParameters.isNotEmpty()) item.queryParameters.joinToString(",\n") { it.queryParam() } else ""}>>
            |            ]
            |        },
            |        "description": "${item.description.escapeJson().escapeAll()}"
            |    },
            |    "response": []
            |}
        """.trimMargin()
    }

    private fun deleteByID(item: ItemGenModel): String {
        return deleteByParam(item, "", true)
    }

    private fun deleteByKey(item: ItemGenModel): String {
        return deleteByParam(item, "key", true)
    }

    private fun deleteByParam(item: ItemGenModel, param: String, by: Boolean): String {
        return """
            |{
            |    "name": "Delete ${item.name.singularize()}${if (by) " by ${if (param.isNotEmpty()) param else "ID"}" else ""}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${testScript(item, param)}>>
            |                ]
            |            }
            |        }
            |    ],
            |    "request": {
            |        "auth":
            |            <<${auth()}>>,
            |        "method": "${item.method.methodName.toUpperCase()}",
            |        "header": [
            |            {
            |                "key": "Content-Type",
            |                "value": "application/json"
            |            }
            |        ],
            |        "body": {
            |            "mode": "raw",
            |            "raw": "${if (item.getExample().isNullOrEmpty().not()) item.getExample()!!.escapeJson() else ""}"
            |        },
            |        "url": {
            |            ${if (param.isNotEmpty()) """
                            "raw": "{{host}}/${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}")}/${param}={{${item.resource.resourcePathName.singularize()}-${param}}}",
                            """.trimIndent() else """
                            "raw": "{{host}}/${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}")}/{{${item.resource.resourcePathName.singularize()}-id}}",
                            """.trimIndent()}
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}").split("/").map { it.trim('/') }.joinToString("\",\n\"")}">>,
            |                "${if (param.isNotEmpty()) "${param}={{${item.resource.resourcePathName.singularize()}-${param}}}" else "{{${item.resource.resourcePathName.singularize()}-id}}"}"
            |            ],
            |            "query": [
            |                <<${if (item.queryParameters.isNotEmpty()) item.queryParameters.joinToString(",\n") { it.queryParam() } else ""}>>
            |            ]
            |        },
            |        "description": "${item.description.escapeJson().escapeAll()}"
            |    },
            |    "response": []
            |}
        """.trimMargin()
    }

    private fun actionExample(item: ActionGenModel): String {
        return """
            |{
            |    "version": {{${item.resource.resourcePathName.singularize()}-version}},
            |    "actions": [
            |        <<${if (item.getExample().isNullOrEmpty().not()) item.getExample() else """
            |        |{
            |        |    "action": "${item.type.discriminatorValue}"
            |        |}""".trimMargin()}>>
            |    ]
            |}
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun updateProjectActionExample(item: ItemGenModel): String {
        if (item.getExample().isNullOrEmpty())
            return """
                |{
                |    "version": {{project-version}},
                |    "actions": []
                |}
            """.trimMargin()
        return item.getExample()!!
    }

    private fun projectActionExample(item: ActionGenModel): String {
        return """
            |{
            |    "version": {{project-version}},
            |    "actions": [
            |        <<${if (item.getExample().isNullOrEmpty().not()) item.getExample() else """
            |        |{
            |        |    "action": "${item.type.discriminatorValue}"
            |        |}""".trimMargin()}>>
            |    ]
            |}
        """.trimMargin().keepIndentation("<<", ">>")
    }


    private fun action(item: ItemGenModel): String {
        if (item is ActionGenModel)
            return """
                |{
                |    "name": "${item.type.discriminatorValue.capitalize()}",
                |    "event": [
                |        {
                |            "listen": "test",
                |            "script": {
                |                "type": "text/javascript",
                |                "exec": [
                |                    <<${testScript(item, "")}>>
                |                ]
                |            }
                |        }
                |    ],
                |    "request": {
                |        "auth":
                |            <<${auth()}>>,
                |        "method": "${item.method.methodName.toUpperCase()}",
                |        "body": {
                |            "mode": "raw",
                |            "raw": "${actionExample(item).escapeJson().escapeAll()}"
                |        },
                |        "header": [
                |            {
                |                "key": "Content-Type",
                |                "value": "application/json"
                |            }
                |        ],
                |        "url": {
                |            "raw": "{{host}}/${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}")}/{{${item.resource.resourcePathName.singularize()}-id}}",
                |            "host": [
                |                "{{host}}"
                |            ],
                |            "path": [
                |                <<"${item.resource.fullUri.template.replace("{", "{{").replace("}", "}}").split("/").map { it.trim('/') }.joinToString("\",\n\"")}">>,
                |                "{{${item.resource.resourcePathName.singularize()}-id}}"
                |            ],
                |            "query": [
                |                <<${if (item.queryParameters.isNotEmpty()) item.queryParameters.joinToString(",\n") { it.queryParam() } else ""}>>
                |            ]
                |        },
                |        "description": "${item.description.escapeJson().escapeAll()}"
                |    },
                |    "response": []
                |}
            """.trimMargin()
        return ""
    }

    private fun getProject(item: ItemGenModel): String {
        return """
            |{
            |    "name": "Get ${item.name.singularize()}",
            |    "event": [
            |        {
            |            "listen": "test",
            |            "script": {
            |                "type": "text/javascript",
            |                "exec": [
            |                    <<${testScript(item, "")}>>
            |                ]
            |            }
            |        }
            |    ],
            |    "request": {
            |        "auth":
            |            <<${auth()}>>,
            |        "method": "${item.method.methodName.toUpperCase()}",
            |        "header": [
            |            {
            |                "key": "Content-Type",
            |                "value": "application/json"
            |            }
            |        ],
            |        "body": {
            |            "mode": "raw",
            |            "raw": ""
            |        },
            |        "url": {
            |            "raw": "{{host}}/{{projectKey}}",
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                "{{projectKey}}"
            |            ],
            |            "query": [
            |                <<${if (item.queryParameters.isNotEmpty()) item.queryParameters.joinToString(",\n") { it.queryParam() } else ""}>>
            |            ]
            |        },
            |        "description": "${item.description.escapeJson()}"
            |    },
            |    "response": []
            |}
        """.trimMargin()
    }

    private fun updateProject(item: ItemGenModel): String {
        return """
                |{
                |    "name": "Update ${item.name.singularize()}",
                |    "event": [
                |        {
                |            "listen": "test",
                |            "script": {
                |                "type": "text/javascript",
                |                "exec": [
                |                    <<${testScript(item, "")}>>
                |                ]
                |            }
                |        }
                |    ],
                |    "request": {
                |        "auth":
                |            <<${auth()}>>,
                |        "method": "${item.method.methodName.toUpperCase()}",
                |        "header": [
                |            {
                |                "key": "Content-Type",
                |                "value": "application/json"
                |            }
                |        ],
                |        "body": {
                |            "mode": "raw",
                |            "raw": "${updateProjectActionExample(item).escapeJson().escapeAll()}"
                |        },
                |        "url": {
                |            "raw": "{{host}}/{{projectKey}}",
                |            "host": [
                |                "{{host}}"
                |            ],
                |            "path": [
                |                "{{projectKey}}"
                |            ],
                |            "query": [
                |                <<${if (item.queryParameters.isNotEmpty()) item.queryParameters.joinToString(",\n") { it.queryParam() } else ""}>>
                |            ]
                |        },
                |        "description": "${item.description.escapeJson().escapeAll()}"
                |    },
                |    "response": []
                |}
            """.trimMargin()
    }

    private fun projectAction(item: ItemGenModel): String {
        if (item is ActionGenModel)
            return """
                |{
                |    "name": "${item.type.discriminatorValue.capitalize()}",
                |    "event": [
                |        {
                |            "listen": "test",
                |            "script": {
                |                "type": "text/javascript",
                |                "exec": [
                |                    <<${testScript(item, "")}>>
                |                ]
                |            }
                |        }
                |    ],
                |    "request": {
                |        "auth":
                |            <<${auth()}>>,
                |        "method": "${item.method.methodName.toUpperCase()}",
                |        "body": {
                |            "mode": "raw",
                |            "raw": "${projectActionExample(item).escapeJson().escapeAll()}"
                |        },
                |        "header": [
                |            {
                |                "key": "Content-Type",
                |                "value": "application/json"
                |            }
                |        ],
                |        "url": {
                |            "raw": "{{host}}/{{projectKey}}",
                |            "host": [
                |                "{{host}}"
                |            ],
                |            "path": [
                |                "{{projectKey}}"
                |            ],
                |            "query": [
                |                <<${if (item.queryParameters.isNotEmpty()) item.queryParameters.joinToString(",\n") { it.queryParam() } else ""}>>
                |            ]
                |        },
                |        "description": "${item.description.escapeJson().escapeAll()}"
                |    },
                |    "response": []
                |}
            """.trimMargin()
        return ""
    }

    private fun authorization(oauth: OAuth20Settings): String {
        return """
            |{
            |    "name": "Authorization",
            |    "description": "Authorization",
            |    "item": [
            |        {
            |            "name": "Obtain access token",
            |            "event": [
            |                {
            |                    "listen": "test",
            |                    "script": {
            |                        "type": "text/javascript",
            |                        "exec": [
            |                            "tests[\"Status code is 200\"] = responseCode.code === 200;",
            |                            "var data = JSON.parse(responseBody);",
            |                            "if(data.access_token){",
            |                            "    pm.environment.set(\"ctp_access_token\", data.access_token);",
            |                            "}",
            |                            "if (data.scope) {",
            |                            "    parts = data.scope.split(\" \");",
            |                            "    if (parts.length > 0) {",
            |                            "        scopeParts = parts[0].split(\":\");",
            |                            "        if (scopeParts.length >= 2) {",
            |                            "            pm.environment.set(\"projectKey\", scopeParts[1]);",
            |                            "        }",
            |                            "    }",
            |                            "}"
            |                        ]
            |                    }
            |                }
            |            ],
            |            "request": {
            |                "auth": {
            |                    "type": "basic",
            |                    "basic": {
            |                        "username": "{{ctp_client_id}}",
            |                        "password": "{{ctp_client_secret}}"
            |                    }
            |                },
            |                "method": "POST",
            |                "header": [],
            |                "body": {
            |                    "mode": "raw",
            |                    "raw": ""
            |                },
            |                "url": {
            |                    "raw": "https://{{auth_url}}${oauth.uri().path}?grant_type=client_credentials",
            |                    "protocol": "https",
            |                    "host": [
            |                        "{{auth_url}}"
            |                    ],
            |                    "path": [
            |                        "oauth",
            |                        "token"
            |                    ],
            |                    "query": [
            |                        {
            |                            "key": "grant_type",
            |                            "value": "client_credentials",
            |                            "equals": true
            |                        }
            |                    ]
            |                },
            |                "description": "Use this request to obtain an access token for your commercetools platform project via Client Credentials Flow. As a prerequisite you must have filled out environment variables in Postman for projectKey, client_id and client_secret to use this."
            |            },
            |            "response": []
            |        },
            |        {
            |            "name": "Obtain access token through password flow",
            |            "event": [
            |                {
            |                    "listen": "test",
            |                    "script": {
            |                        "type": "text/javascript",
            |                        "exec": [
            |                            "tests[\"Status code is 200\"] = responseCode.code === 200;"
            |                        ]
            |                    }
            |                }
            |            ],
            |            "request": {
            |                "auth": {
            |                    "type": "basic",
            |                    "basic": {
            |                        "username": "{{ctp_client_id}}",
            |                        "password": "{{ctp_client_secret}}"
            |                    }
            |                },
            |                "method": "POST",
            |                "header": [
            |                    {
            |                        "key": "",
            |                        "value": "",
            |                        "disabled": true
            |                    }
            |                ],
            |                "body": {
            |                    "mode": "raw",
            |                    "raw": ""
            |                },
            |                "url": {
            |                    "raw": "https://{{auth_url}}/oauth/{{projectKey}}/customers/token?grant_type=password&username={{user_email}}&password={{user_password}}",
            |                    "protocol": "https",
            |                    "host": [
            |                        "{{auth_url}}"
            |                    ],
            |                    "path": [
            |                        "oauth",
            |                        "{{projectKey}}",
            |                        "customers",
            |                        "token"
            |                    ],
            |                    "query": [
            |                        {
            |                            "key": "grant_type",
            |                            "value": "password",
            |                            "equals": true
            |                        },
            |                        {
            |                            "key": "username",
            |                            "value": "",
            |                            "equals": true
            |                        },
            |                        {
            |                            "key": "password",
            |                            "value": "",
            |                            "equals": true
            |                        },
            |                        {
            |                            "key": "scope",
            |                            "value": "manage_project:{{projectKey}}",
            |                            "equals": true
            |                        }
            |                    ]
            |                },
            |                "description": "Use this request to obtain an access token for your commercetools platform project via Password Flow. As a prerequisite you must have filled out environment variables in Postman for projectKey, client_id, client_secret, user_email and user_password to use this."
            |            },
            |            "response": []
            |        },
            |        {
            |            "name": "Token for Anonymous Sessions",
            |            "event": [
            |                {
            |                    "listen": "test",
            |                    "script": {
            |                        "type": "text/javascript",
            |                        "exec": [
            |                            "tests[\"Status code is 200\"] = responseCode.code === 200;"
            |                        ]
            |                    }
            |                }
            |            ],
            |            "request": {
            |                "auth": {
            |                    "type": "basic",
            |                    "basic": {
            |                        "username": "{{ctp_client_id}}",
            |                        "password": "{{ctp_client_secret}}"
            |                    }
            |                },
            |                "method": "POST",
            |                "header": [],
            |                "body": {
            |                    "mode": "raw",
            |                    "raw": ""
            |                },
            |                "url": {
            |                    "raw": "https://{{auth_url}}/oauth/{{projectKey}}/anonymous/token?grant_type=client_credentials&scope=manage_my_profile:{{projectKey}}",
            |                    "protocol": "https",
            |                    "host": [
            |                        "{{auth_url}}"
            |                    ],
            |                    "path": [
            |                        "oauth",
            |                        "{{projectKey}}",
            |                        "anonymous",
            |                        "token"
            |                    ],
            |                    "query": [
            |                        {
            |                            "key": "grant_type",
            |                            "value": "client_credentials",
            |                            "equals": true
            |                        },
            |                        {
            |                            "key": "scope",
            |                            "value": "manage_my_profile:{{projectKey}}",
            |                            "equals": true
            |                        }
            |                    ]
            |                },
            |                "description": "Use this request to obtain an access token for a anonymous session. As a prerequisite you must have filled out environment variables in Postman for projectKey, client_id and client_secret to use this."
            |            },
            |            "response": []
            |        },
            |        {
            |            "name": "Token Introspection",
            |            "event": [
            |                {
            |                    "listen": "test",
            |                    "script": {
            |                        "type": "text/javascript",
            |                        "exec": [
            |                            "tests[\"Status code is 200\"] = responseCode.code === 200;"
            |                        ]
            |                    }
            |                }
            |            ],
            |            "request": {
            |                "auth": {
            |                    "type": "basic",
            |                    "basic": {
            |                        "username": "{{ctp_client_id}}",
            |                        "password": "{{ctp_client_secret}}"
            |                    }
            |                },
            |                "method": "POST",
            |                "header": [
            |                    {
            |                        "key": "Content-Type",
            |                        "value": "application/json"
            |                    }
            |                ],
            |                "body": {
            |                    "mode": "raw",
            |                    "raw": ""
            |                },
            |                "url": {
            |                    "raw": "https://{{auth_url}}/oauth/introspect?token={{ctp_access_token}}",
            |                    "protocol": "https",
            |                    "host": [
            |                        "{{auth_url}}"
            |                    ],
            |                    "path": [
            |                        "oauth",
            |                        "introspect"
            |                    ],
            |                    "query": [
            |                        {
            |                            "key": "token",
            |                            "value": "{{ctp_access_token}}",
            |                            "equals": true
            |                        }
            |                    ]
            |                },
            |                "description": "Token introspection allows to determine the active state of an OAuth 2.0 access token and to determine meta-information about this accces token, such as the `scope`."
            |            },
            |            "response": []
            |        }
            |    ]
            |}
        """.trimMargin().escapeAll()
    }

    private fun readme(): String {
        return """
            # commercetools API Postman collection

            This Postman collection contains examples of requests and responses for most endpoints and commands of the commercetools platform API. For every command the smallest possible payload is given. Please find optional fields in the related official documentation. Additionally the collection provides example requests and responses for specific tasks and more complex data models.

            ## Disclaimer

            This is not the official commercetools platform API documentation. Please see [here](http://docs.commercetools.com/) for a complete and approved documentation of the commercetools platform API.

            To automate frequent tasks the collection automatically manages commonly required values and parameters such as resource ids, keys and versions in Postman environment variables for you.

            Please see http://docs.commercetools.com/ for further information about the commercetools Plattform.
        """.trimIndent()
    }

    class ResourceModel(val resource: Resource, val items: List<ItemGenModel>)

    fun Api.resources(): List<ResourceModel> {
        val resources = Lists.newArrayList<ResourceModel>()
        resources.add(ResourceModel(this.resources[0], this.resources[0].projectItems()))
        resources.addAll(this.resources[0].resources.map { ResourceModel(it, it.items()) })
        return resources
    }

    fun Resource.projectItems(): List<ItemGenModel> {
        val items = Lists.newArrayList<ItemGenModel>()
        val resource = this
        if (resource.getMethod(HttpMethod.GET) != null) {
            items.add(ItemGenModel(resource, ::getProject, resource.getMethod(HttpMethod.GET)))
        }
        if (resource.getMethod(HttpMethod.POST) != null) {
            items.add(ItemGenModel(resource, ::updateProject, resource.getMethod(HttpMethod.POST)))
        }
        if (resource.getMethod(HttpMethod.POST) != null) {
            items.addAll(getActionItems(resource.getMethod(HttpMethod.POST), ::projectAction))
        }
        return items
    }

    fun Resource.items(): List<ItemGenModel> {
        val items = Lists.newArrayList<ItemGenModel>()

        if (this.getMethod(HttpMethod.GET) != null) {
            items.add(ItemGenModel(this, ::query, this.getMethod(HttpMethod.GET)))
        }
        if (this.getMethod(HttpMethod.POST) != null) {
            items.add(ItemGenModel(this, ::create, this.getMethod(HttpMethod.POST)))
        }
        val byId = this.resources.stream().filter { resource1 -> resource1.getUriParameter("ID") != null }.findFirst().orElse(null)
        val byKey = this.resources.stream().filter { resource1 -> resource1.getUriParameter("key") != null }.findFirst().orElse(null)
        if (byId?.getMethod(HttpMethod.GET) != null) {
            items.add(ItemGenModel(this, ::getByID, byId.getMethod(HttpMethod.GET)))
        }
        if (byKey?.getMethod(HttpMethod.GET) != null) {
            items.add(ItemGenModel(this, ::getByKey, byKey.getMethod(HttpMethod.GET)))
        }
        if (byId?.getMethod(HttpMethod.POST) != null) {
            items.add(ItemGenModel(this, ::updateByID, byId.getMethod(HttpMethod.POST)))
        }
        if (byKey?.getMethod(HttpMethod.POST) != null) {
            items.add(ItemGenModel(this, ::updateByKey, byKey.getMethod(HttpMethod.POST)))
        }
        if (byId?.getMethod(HttpMethod.DELETE) != null) {
            items.add(ItemGenModel(this, ::deleteByID, byId.getMethod(HttpMethod.DELETE)))
        }
        if (byKey?.getMethod(HttpMethod.DELETE) != null) {
            items.add(ItemGenModel(this, ::deleteByKey, byKey.getMethod(HttpMethod.DELETE)))
        }
        if (byId?.getMethod(HttpMethod.POST) != null) {
            items.addAll(getActionItems(byId.getMethod(HttpMethod.POST)))
        }
        return items
    }

    fun Resource.getActionItems(method: Method): List<ActionGenModel> {
        return this.getActionItems(method, ::action)
    }

    fun Resource.getActionItems(method: Method, template: KFunction1<ItemGenModel, String>): List<ActionGenModel> {
        val actionItems = Lists.newArrayList<ActionGenModel>()

        val body = method.getBody("application/json")
        if (body != null && body.type is ObjectType) {
            val actions = (body.type as ObjectType).getProperty("actions")
            if (actions != null) {
                val actionsType = actions.type as ArrayType
                val updateActions: List<AnyType>
                if (actionsType.items is UnionType) {
                    updateActions = (actionsType.items as UnionType).oneOf[0].subTypes
                } else {
                    updateActions = actionsType.items.subTypes
                }
                for (action in updateActions) {
                    actionItems.add(ActionGenModel(action as ObjectType, this, template, method))
                }
                actionItems.sortBy { actionGenModel -> actionGenModel.discriminatorValue }
            }
        }

        return actionItems
    }

    fun ResourceModel.description(): String? {
        return this.resource.description?.value
    }

    fun String.singularize(): String {
        return English.singular(this)
    }

    fun String.escapeJson(): String {
        return StringEscapeUtils.escapeJson(this)
    }
    class ActionGenModel(val type: ObjectType, resource: Resource, template: KFunction1<ItemGenModel, String>, method: Method) : ItemGenModel(resource, template, method) {
        val testScript: String?
        private val example: String?
        val discriminatorValue: String
            get() = type.discriminatorValue

        override val description: String
            get() {
                val description = Optional.ofNullable(type.description).map<String> { it.value }.orElse(type.name)
                return StringEscapeUtils.escapeJson(description)
            }

        init {
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

            this.example = example
            val t = type.getAnnotation("postman-test-script")
            if (t != null) {
                this.testScript = (t.value as StringInstance).value
            } else {
                this.testScript = null
            }
        }


        override fun getExample(): String? {
            return example
        }
    }

    open class ItemGenModel(val resource: Resource, val template: KFunction1<ItemGenModel, String>, val method: Method) {

        val name: String
            get() =
                StringCaseFormat.UPPER_CAMEL_CASE.apply(Optional.ofNullable(resource.displayName).map<String> { it.value }.orElse(resource.resourcePathName))

        open val description: String
            get() {
                val description = Optional.ofNullable(method.description).map<String> { it.value }.orElse(method.method.getName() + " " + name)
                return StringEscapeUtils.escapeJson(description)
            }

        val resourcePathName: String
            get() {
                val resourcePathName = resource.resourcePathName

                return if (resourcePathName.isEmpty()) {
                    resource.displayName.value.toLowerCase()
                } else resourcePathName
            }

        val queryParameters: List<QueryParameter>
            get() =
                method.queryParameters

        open fun getExample(): String? {
            val s = method.bodies?.
                    getOrNull(0)?.
                    type?.
                    examples?.
                    getOrNull(0)?.
                    value
            return StringEscapeUtils.escapeJson(s?.toJson())
        }
    }

    fun ResourceModel.name(): String {
        return StringCaseFormat.UPPER_CAMEL_CASE.apply(Optional.ofNullable(this.resource.displayName).map<String> { it.value }.orElse(this.resource.resourcePathName))
    }

    private fun URI.pathElements(): List<String> {
        return this.path.split("/").filter { it.isNotEmpty() }
    }
    private fun OAuth20Settings.uri(): URI {
        return URI.create(this.accessTokenUri)
    }

    private fun QueryParameter.queryParam() : String {
        return """
            |{
            |    "key": "${this.name}",
            |    "value": "${this.defaultValue()}",
            |    "equals": true,
            |    "disabled": ${this.required.not()}
            |}
        """.trimMargin()
    }

    fun Api.oauth(): OAuth20Settings {
        return this.securitySchemes.stream()
                .filter { securityScheme -> securityScheme.settings is OAuth20Settings }
                .map { securityScheme -> securityScheme.settings as OAuth20Settings }
                .findFirst().orElse(null)
    }

    fun QueryParameter.defaultValue(): String {
        if (this.name == "version") {
            return "{{" + this.getParent(Resource::class.java)?.resourcePathName?.singularize() + "-version}}"
        }
        val defaultValue = this.getAnnotation("postman-default-value")
        if (defaultValue != null && defaultValue.value is StringInstance) {
            val value = (defaultValue.value.value as String).replace("{{", "").replace("}}", "")

            return "{{" + this.getParent(Resource::class.java)?.resourcePathName?.singularize() + "-" + value + "}}"
        }

        return ""
    }

    fun <T> EObject.getParent(parentClass: Class<T>): T? {
        if (this.eContainer() == null) {
            return null
        }
        return if (parentClass.isInstance(this.eContainer())) {
            this.eContainer() as T
        } else this.eContainer().getParent(parentClass)
    }
}

fun Instance.toJson(): String {
    var example = ""
    val mapper = ObjectMapper()

    val module = SimpleModule()
    module.addSerializer(ObjectInstance::class.java, ObjectInstanceSerializer())
    module.addSerializer<Instance>(ArrayInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(IntegerInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(BooleanInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(StringInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(NumberInstance::class.java, InstanceSerializer())
    mapper.registerModule(module)

    if (this is StringInstance) {
        example = this.value
    } else if (this is ObjectInstance) {
        try {
            example = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
        } catch (e: JsonProcessingException) {
        }

    }

    return example
}

class InstanceSerializer @JvmOverloads constructor(t: Class<Instance>? = null) : StdSerializer<Instance>(t) {

    @Throws(IOException::class)
    override fun serialize(value: Instance, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeObject(value.value)
    }
}

class ObjectInstanceSerializer @JvmOverloads constructor(t: Class<ObjectInstance>? = null) : StdSerializer<ObjectInstance>(t) {

    @Throws(IOException::class)
    override fun serialize(value: ObjectInstance, gen: JsonGenerator, provider: SerializerProvider) {
        val properties = value.value
        gen.writeStartObject()
        for (v in properties) {
            gen.writeObjectField(v.name, v.value)
        }
        gen.writeEndObject()
    }
}

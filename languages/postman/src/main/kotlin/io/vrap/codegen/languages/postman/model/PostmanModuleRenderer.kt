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
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.apache.commons.lang3.StringEscapeUtils
import org.eclipse.emf.ecore.EObject
import java.io.IOException
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
                content = readme(api))
    }

    private fun template(api: Api): TemplateFile {
        return TemplateFile(relativePath = "template.json",
                content = """
                    |{
                    |  "id": "5bb74f05-5e78-4aee-b59e-492c947bc160",
                    |  "name": "${api.title}.template",
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
                    |      "value": "https://${api.oAuth2().uri().host}",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "client_id",
                    |      "value": "<your-client-id>",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "client_secret",
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
                    |        "name": "${api.title}",
                    |        "description": "${readme(api).escapeJson().escapeAll()}",
                    |        "schema": "https://schema.getpostman.com/json/collection/v2.0.0/collection.json"
                    |    },
                    |    "auth":
                    |        <<${auth()}>>,
                    |    "item": [
                    |        <<${authorization(api.oAuth2())}>>,
                    |        <<${api.resources().joinToString(",") { folder(it) }}>>
                    |    ]
                    |}
                """.trimMargin().keepAngleIndent())
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
            |            "raw": "{{host}}${item.toPostmanPath()}",
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.toPostmanPath().trim('/').split("/").joinToString("\",\n\"")}">>
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
            |            "raw": "{{host}}${item.toPostmanPath()}",
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.toPostmanPath().trim('/').split("/").joinToString("\",\n\"")}">>
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
                            "raw": "{{host}}${item.toPostmanPath()}/${param}={{${item.singularName()}-${param}}}",
                            """.trimIndent() else """
                            "raw": "{{host}}${item.toPostmanPath()}/{{${item.singularName()}-id}}",
                            """.trimIndent()}
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.toPostmanPath().trim('/').split("/").joinToString("\",\n\"")}">>,
            |                "${if (param.isNotEmpty()) "${param}={{${item.singularName()}-${param}}}" else "{{${item.singularName()}-id}}"}"
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
                            |    "version": {{${item.singularName()}-version}},
                            |    "actions": []
                            |}
                            """.trimMargin().escapeJson().escapeAll()}"
            |        },
            |        "url": {
            |            ${if (param.isNotEmpty()) """
                            "raw": "{{host}}${item.toPostmanPath()}/${param}={{${item.singularName()}-${param}}}",
                            """.trimIndent() else """
                            "raw": "{{host}}${item.toPostmanPath()}/{{${item.singularName()}-id}}",
                            """.trimIndent()}
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.toPostmanPath().trim('/').split("/").joinToString("\",\n\"")}">>,
            |                "${if (param.isNotEmpty()) "${param}={{${item.singularName()}-${param}}}" else "{{${item.singularName()}-id}}"}"
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
                            "raw": "{{host}}${item.toPostmanPath()}/${param}={{${item.singularName()}-${param}}}",
                            """.trimIndent() else """
                            "raw": "{{host}}${item.toPostmanPath()}/{{${item.singularName()}-id}}",
                            """.trimIndent()}
            |            "host": [
            |                "{{host}}"
            |            ],
            |            "path": [
            |                <<"${item.toPostmanPath().trim('/').split("/").joinToString("\",\n\"")}">>,
            |                "${if (param.isNotEmpty()) "${param}={{${item.singularName()}-${param}}}" else "{{${item.singularName()}-id}}"}"
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
            |    "version": {{${item.singularName()}-version}},
            |    "actions": [
            |        <<${if (item.getExample().isNullOrEmpty().not()) item.getExample() else """
            |        |{
            |        |    "action": "${item.type.discriminatorValue}"
            |        |}""".trimMargin()}>>
            |    ]
            |}
        """.trimMargin().keepAngleIndent()
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
        """.trimMargin().keepAngleIndent()
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
                |            "raw": "{{host}}${item.toPostmanPath()}/{{${item.singularName()}-id}}",
                |            "host": [
                |                "{{host}}"
                |            ],
                |            "path": [
                |                <<"${item.toPostmanPath().trim('/').split("/").joinToString("\",\n\"")}">>,
                |                "{{${item.singularName()}-id}}"
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


    private fun readme(api: Api): String {
        return """
            # commercetools API Postman collection

            This Postman collection contains examples of requests and responses for most endpoints and commands of the
            ${api.title}. For every command the smallest possible payload is given. Please find optional
            fields in the related official documentation. Additionally the collection provides example requests and
            responses for specific tasks and more complex data models.

            ## Disclaimer

            This is not the official ${api.title} documentation. Please see [here](http://docs.commercetools.com/)
            for a complete and approved documentation of the ${api.title}.

            ## How to use
            
            **:warning: Be aware that postman automatically synchronizes environment variables (including your API client credentials) to your workspace if logged in.
            Use this collection only for development purposes and non-production projects.**
            
            To use this collection in Postman please perform the following steps:

            1. Download and install the Postman Client
            2. Import the [collection.json](https://github.com/commercetools/commercetools-postman-api-examples/raw/master/collection.json) and [template.json](https://github.com/commercetools/commercetools-postman-api-examples/raw/master/template.json) in your postman application
            3. In the Merchant Center, create a new API Client and fill in the client credentials in your environment
            4. Obtain an access token by sending the "Authorization/Obtain access token" request at the bottom of the request list. Now you can use all other endpoints
    
            Feel free to clone and modify this collection to your needs.

            To automate frequent tasks the collection automatically manages commonly required values and parameters such
            as resource ids, keys and versions in Postman environment variables for you.

            Please see http://docs.commercetools.com/ for further information about the commercetools Plattform.
        """.trimIndent()
    }

    class ResourceModel(val resource: Resource, val items: List<ItemGenModel>)

    fun Api.resources(): List<ResourceModel> {
        val resources = Lists.newArrayList<ResourceModel>()
        val rootItems = this.resources[0].projectItems()
        if (rootItems.size > 0) {
            resources.add(ResourceModel(this.resources[0], this.resources[0].projectItems()))
        }
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
        if (byKey?.getMethod(HttpMethod.PUT) != null) {
            items.add(ItemGenModel(this, ::updateByKey, byKey.getMethod(HttpMethod.PUT)))
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

        fun toPostmanPath(): String {
            return resource.fullUri.template.replace("{", "{{").replace("}", "}}")
        }
        
        fun singularName(): String {
            return resource.resourcePathName.singularize()
        }
    }

    fun ResourceModel.name(): String {
        return StringCaseFormat.UPPER_CAMEL_CASE.apply(Optional.ofNullable(this.resource.displayName).map<String> { it.value }.orElse(this.resource.resourcePathName))
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

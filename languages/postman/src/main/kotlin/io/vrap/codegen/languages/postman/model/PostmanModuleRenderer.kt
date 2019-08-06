package io.vrap.codegen.languages.postman.model

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
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.security.OAuth20Settings
import io.vrap.rmf.raml.model.types.*
import org.apache.commons.lang3.StringEscapeUtils
import org.eclipse.emf.ecore.EObject
import java.net.URI
import java.util.ArrayList

class PostmanModuleRenderer @Inject constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                template(api),
                collection(api)
        )
    }

    private fun template(api: Api): TemplateFile {
        return TemplateFile(relativePath = "template.json",
                content = """
                    |{
                    |  "id": "<id>",
                    |  "name": "commercetools platform API (generated).template",
                    |  "values": [
                    |    {
                    |      "enabled": true,
                    |      "key": "host",
                    |      "value": "${api.baseUri}",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "auth_url",
                    |      "value": "<api.OAuth.uri.host>",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "ctp_client_id",
                    |      "value": "\<your-client-id\>",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "ctp_client_secret",
                    |      "value": "\<your-client-secret\>",
                    |      "type": "text"
                    |    },
                    |    {
                    |      "enabled": true,
                    |      "key": "ctp_access_token",
                    |      "value": "\<your_access_token\>",
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
                    |        "_postman_id": "<id>",
                    |        "name": "commercetools platform API (generated)",
                    |        "description": "${StringEscapeUtils.escapeJson(readme())}",
                    |        "schema": "https://schema.getpostman.com/json/collection/v2.0.0/collection.json"
                    |    },
                    |    "auth":
                    |        <<${auth(api.oauth())}>>,
                    |    "item": [
                    |        <<"authorization(${api.oauth()})">>,
                    |        <<"api.resources: {r|<folder(${api.oauth()}, r)>}; separator=">>
                    |    ]
                    |}
                """.trimMargin().keepIndentation("<<",">>"))
    }

    private fun auth(oauth: OAuth20Settings): String {
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
            |                        "${oauth.uri().pathElements().joinToString("\", \"")}"
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

    private fun URI.pathElements(): List<String> {
        return this.path.split("/")
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
        """.trimIndent()
    }

    fun Api.oauth(): OAuth20Settings {
        return api.securitySchemes.stream()
                .filter { securityScheme -> securityScheme.settings is OAuth20Settings }
                .map { securityScheme -> securityScheme.settings as OAuth20Settings }
                .findFirst().orElse(null)
    }

    fun QueryParameter.defaultValue(): String {
        if (this.name == "version") {
            return "{{" + English.singular(this.getParent(Resource::class.java)?.resourcePathName) + "-version}}"
        }
        val defaultValue = this.getAnnotation("postman-default-value")
        if (defaultValue != null && defaultValue.getValue() is StringInstance) {
            val value = (defaultValue.getValue().getValue() as String).replace("{{", "").replace("}}", "")

            return "{{" + English.singular(this.getParent(Resource::class.java)?.resourcePathName) + "-" + value + "}}"
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

    /**
folder(oauth, resource) ::=<<

{
    "name": "<resource.name>",
    "description": "<resource.description>",
    "item": [
        <resource.items: {item |<(item.template)(oauth, item)>}; separator=",">
    ],
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    <test(first(resource.items), false)>
                ]
            }
        }
    ]
}
>>

query(oauth, item) ::=<<

{
    "name": "Query <item.name>",
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    "tests[\"Status code is 200\"] = responseCode.code === 200;",
                    "var data = JSON.parse(responseBody);",
                    "if(data.results && data.results[0] && data.results[0].id && data.results[0].version){",
                    "    pm.environment.set(\"<item.resource.resourcePathName; format="singularize">-id\", data.results[0].id); ",
                    "    pm.environment.set(\"<item.resource.resourcePathName; format="singularize">-version\", data.results[0].version);",
                    "}",
                    "if(data.results && data.results[0] && data.results[0].key){",
                    "    pm.environment.set(\"<item.resource.resourcePathName; format="singularize">-key\", data.results[0].key); ",
                    "}"
                ]
            }
        }
    ],
    "request": {
        "auth":
            <auth(oauth)>,
        "method": "<item.method.methodName; format="uppercase">",
        "header": [
            {
                "key": "Content-Type",
                "value": "application/json"
            }
        ],
        "body": {
            "mode": "raw",
            "raw": ""
        },
        "url": {
            "raw": "{{host}}/{{projectKey}}<item.resource.relativeUri.template>",
            "host": [
                "{{host}}"
            ],
            "path": [
                "{{projectKey}}",
                "<item.resource.resourcePathName>"
            <if (item.queryParameters)>
            ],
            "query": [
                <item.queryParameters: {param |<queryParam(param)>}; separator=",">
            <endif>
            ]
        },
        "description": "<item.description>"
    },
    "response": []
}
>>

create(oauth, item) ::=<<

{
    "name": "Create <item.name; format="singularize">",
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    <test(item, false)>
                ]
            }
        }
    ],
    "request": {
        "auth": <auth(oauth)>,
        "method": "<item.method.methodName; format="uppercase">",
        "header": [
            {
                "key": "Content-Type",
                "value": "application/json"
            }
        ],
        "body": {
            "mode": "raw",
            "raw": "<if (item.example)><item.example><endif>"
        },
        "url": {
            "raw": "{{host}}/{{projectKey}}<item.resource.relativeUri.template>",
            "host": [
                "{{host}}"
            ],
            "path": [
                "{{projectKey}}",
                "<item.resource.resourcePathName>"
            <if (item.queryParameters)>
            ],
            "query": [
                <item.queryParameters: {param |<queryParam(param)>}; separator=",">
            <endif>
            ]
        },
        "description": "<item.description>"
    },
    "response": []
}
>>

get(oauth, item) ::=<<
<getByParam(oauth, item, false, false)>
>>


getByID(oauth, item) ::=<<
<getByParam(oauth, item, false, true)>
>>

getByKey(oauth, item) ::=<<
<getByParam(oauth, item, "key", true)>
>>

getByParam(oauth, item, param, by) ::=<<
{
    "name": "Get <item.name; format="singularize"><if(by)> by <if(param)><param><else>ID<endif><endif>",
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    <test(item, param)>
                ]
            }
        }
    ],
    "request": {
        "auth":
            <auth(oauth)>,
        "method": "<item.method.methodName; format="uppercase">",
        "header": [
            {
                "key": "Content-Type",
                "value": "application/json"
            }
        ],
        "body": {
            "mode": "raw",
            "raw": ""
        },
        "url": {
            <if (param)>
            "raw": "{{host}}/{{projectKey}}<item.resource.relativeUri.template>/<param>={{<item.resource.resourcePathName; format="singularize">-<param>}}",
            <else>
            "raw": "{{host}}/{{projectKey}}<item.resource.relativeUri.template>/{{<item.resource.resourcePathName; format="singularize">-id}}",
            <endif>
            "host": [
                "{{host}}"
            ],
            "path": [
                "{{projectKey}}",
                "<item.resource.resourcePathName>",
                <if (param)>
                "<param>={{<item.resource.resourcePathName; format="singularize">-<param>}}"
                <else>
                "{{<item.resource.resourcePathName; format="singularize">-id}}"
                <endif>
            <if (item.queryParameters)>
            ],
            "query": [
                <item.queryParameters: {param |<queryParam(param)>}; separator=",">
            <endif>
            ]
        },
        "description": "<item.description>"
    },
    "response": []
}
>>

getProject(oauth, item) ::=<<
{
    "name": "Get <item.name; format="singularize">",
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    <test(item, false)>
                ]
            }
        }
    ],
    "request": {
        "auth":
            <auth(oauth)>,
        "method": "<item.method.methodName; format="uppercase">",
        "header": [
            {
                "key": "Content-Type",
                "value": "application/json"
            }
        ],
        "body": {
            "mode": "raw",
            "raw": ""
        },
        "url": {
            "raw": "{{host}}/{{projectKey}}",
            "host": [
                "{{host}}"
            ],
            "path": [
                "{{projectKey}}"
            <if (item.queryParameters)>
            ],
            "query": [
                <item.queryParameters: {param |<queryParam(param)>}; separator=",">
            <endif>
            ]
        },
        "description": "<item.description>"
    },
    "response": []
}
>>

updateByID(oauth, item) ::=<<
<updateByParam(oauth, item, false)>
>>

updateByKey(oauth, item) ::=<<
<updateByParam(oauth, item, "key")>
>>

updateProject(oauth, item) ::=<<
{
    "name": "Update <item.name; format="singularize">",
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    <test(item, false)>
                ]
            }
        }
    ],
    "request": {
        "auth":
            <auth(oauth)>,
        "method": "<item.method.methodName; format="uppercase">",
        "header": [
            {
                "key": "Content-Type",
                "value": "application/json"
            }
        ],
        "body": {
            "mode": "raw",
            "raw": "<if (item.example)><item.example><else>{\n  \"version\": {{project-version}},\n  \"actions\": [\n  ]\n}<endif>"
        },
        "url": {
            "raw": "{{host}}/{{projectKey}}",
            "host": [
                "{{host}}"
            ],
            "path": [
                "{{projectKey}}"
            <if (item.queryParameters)>
            ],
            "query": [
                <item.queryParameters: {param |<queryParam(param)>}; separator=",">
            <endif>
            ]
        },
        "description": "<item.description>"
    },
    "response": []
}
>>

updateByParam(oauth, item, param) ::=<<
{
    "name": "Update <item.name; format="singularize"> by <if(param)><param><else>ID<endif>",
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    <test(item, param)>
                ]
            }
        }
    ],
    "request": {
        "auth":
            <auth(oauth)>,
        "method": "<item.method.methodName; format="uppercase">",
        "header": [
            {
                "key": "Content-Type",
                "value": "application/json"
            }
        ],
        "body": {
            "mode": "raw",
            "raw": "<if (item.example)><item.example><else>{\n  \"version\": {{<item.resource.resourcePathName; format="singularize">-version}},\n  \"actions\": [\n  ]\n}<endif>"
        },
        "url": {
            <if (param)>
            "raw": "{{host}}/{{projectKey}}<item.resource.relativeUri.template>/<param>={{<item.resource.resourcePathName; format="singularize">-<param>}}",
            <else>
            "raw": "{{host}}/{{projectKey}}<item.resource.relativeUri.template>/{{<item.resource.resourcePathName; format="singularize">-id}}",
            <endif>
            "host": [
                "{{host}}"
            ],
            "path": [
                "{{projectKey}}",
                "<item.resource.resourcePathName>",
                <if (param)>
                "<param>={{<item.resource.resourcePathName; format="singularize">-<param>}}"
                <else>
                "{{<item.resource.resourcePathName; format="singularize">-id}}"
                <endif>
            <if (item.queryParameters)>
            ],
            "query": [
                <item.queryParameters: {param |<queryParam(param)>}; separator=",">
            <endif>
            ]
        },
        "description": "<item.description>"
    },
    "response": []
}
>>

deleteByID(oauth, item) ::=<<
<deleteByParam(oauth, item, false)>
>>

deleteByKey(oauth, item) ::=<<
<deleteByParam(oauth, item, "key")>
>>

deleteByParam(oauth, item, param) ::=<<
{
    "name": "Delete <item.name; format="singularize"> by <if(param)><param><else>ID<endif>",
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    <test(item, param)>
                ]
            }
        }
    ],
    "request": {
        "auth":
            <auth(oauth)>,
        "method": "<item.method.methodName; format="uppercase">",
        "header": [
            {
                "key": "Content-Type",
                "value": "application/json"
            }
        ],
        "body": {
            "mode": "raw",
            "raw": ""
        },
        "url": {
            <if (param)>
            "raw": "{{host}}/{{projectKey}}<item.resource.relativeUri.template>/<param>={{<item.resource.resourcePathName; format="singularize">-<param>}}?version={{<item.resource.resourcePathName; format="singularize">-version}}",
            <else>
            "raw": "{{host}}/{{projectKey}}<item.resource.relativeUri.template>/{{<item.resource.resourcePathName; format="singularize">-id}}?version={{<item.resource.resourcePathName; format="singularize">-version}}",
            <endif>
            "host": [
                "{{host}}"
            ],
            "path": [
                "{{projectKey}}",
                "<item.resource.resourcePathName>",
                <if (param)>
                "<param>={{<item.resource.resourcePathName; format="singularize">-<param>}}"
                <else>
                "{{<item.resource.resourcePathName; format="singularize">-id}}"
                <endif>
            <if (item.queryParameters)>
            ],
            "query": [
                <item.queryParameters: {param |<queryParam(param)>}; separator=",">
            <endif>
            ]
        },
        "description": "<item.description>"
    },
    "response": []
}
>>

action(oauth, action) ::=<<
{
    "name": "<action.type.discriminatorValue; format="capitalize">",
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    <test(item, false)>
                ]
            }
        }
    ],
    "request": {
        "auth":
            <auth(oauth)>,
        "method": "<item.method.methodName; format="uppercase">",
        "body": {
            "mode": "raw",
            "raw": "{\n  \"version\": {{<item.resource.resourcePathName; format="singularize">-version}},\n  \"actions\": [<if (item.example)><item.example><else>{\n    \"action\": \"<action.type.discriminatorValue>\"\n  }<endif>]\n}"
        },
        "header": [
            {
                "key": "Content-Type",
                "value": "application/json"
            }
        ],
        "url": {
            "raw": "{{host}}/{{projectKey}}/<item.resource.relativeUri.template>/{{<item.resource.resourcePathName; format="singularize">-id}}",
            "host": [
                "{{host}}"
            ],
            "path": [
                "{{projectKey}}",
                "<item.resource.resourcePathName>",
                "{{<item.resource.resourcePathName; format="singularize">-id}}"
            <if (item.queryParameters)>
            ],
            "query": [
                <item.queryParameters: {param |<queryParam(param)>}; separator=",">
            <endif>
            ]
        },
        "description": "<item.description>"
    },
    "response": []
}
>>

projectAction(oauth, action) ::=<<
{
    "name": "<action.type.discriminatorValue; format="capitalize">",
    "event": [
        {
            "listen": "test",
            "script": {
                "type": "text/javascript",
                "exec": [
                    <test(item, false)>
                ]
            }
        }
    ],
    "request": {
        "auth":
            <auth(oauth)>,
        "method": "<item.method.methodName; format="uppercase">",
        "body": {
            "mode": "raw",
            "raw": "{\n  \"version\": {{project-version}},\n  \"actions\": [<if (item.example)><item.example><else>{\n    \"action\": \"<action.type.discriminatorValue>\"\n  }<endif>]\n}"
        },
        "header": [
            {
                "key": "Content-Type",
                "value": "application/json"
            }
        ],
        "url": {
            "raw": "{{host}}/{{projectKey}}",
            "host": [
                "{{host}}"
            ],
            "path": [
                "{{projectKey}}"
            <if (item.queryParameters)>
            ],
            "query": [
                <item.queryParameters: {param |<queryParam(param)>}; separator=",">
            <endif>
            ]
        },
        "description": "<item.description>"
    },
    "response": []
}
>>

test(item, param) ::=<<
"tests[\"Status code \" + responseCode.code] = responseCode.code === 200 || responseCode.code === 201;",
"var data = JSON.parse(responseBody);",
"if(data.version){",
"    pm.environment.set(\"<item.resourcePathName; format="singularize">-version\", data.version);",
"}",
"if(data.id){",
"    pm.environment.set(\"<item.resourcePathName; format="singularize">-id\", data.id); ",
"}",
"if(data.key){",
"    pm.environment.set(\"<item.resourcePathName; format="singularize">-key\", data.key); ",
"}",
<if (param)>
"if(data.<param>){",
"    pm.environment.set(\"<item.resourcePathName; format="singularize">-<param>\", data.<param>); ",
"}",
<endif>
<if (item.testScript)>
<item.testScript: {t |"<t; format="jsonescape">"}; separator=",\n">,
<endif>
""
>>

auth(oauth) ::=<<

{
    "type": "oauth2",
    "oauth2": {
        "accessToken": "{{ctp_access_token}}",
        "addTokenTo": "header",
        "tokenType": "Bearer"
    }
}
>>

authorization(oauth) ::=<<

{
    "name": "Authorization",
    "description": "Authorization",
    "item": [
        {
            "name": "Obtain access token",
            "event": [
                {
                    "listen": "test",
                    "script": {
                        "type": "text/javascript",
                        "exec": [
                            "tests[\"Status code is 200\"] = responseCode.code === 200;",
                            "var data = JSON.parse(responseBody);",
                            "if(data.access_token){",
                            "    pm.environment.set(\"ctp_access_token\", data.access_token);",
                            "}",
                            "if (data.scope) {",
                            "    parts = data.scope.split(\" \");",
                            "    if (parts.length > 0) {",
                            "        scopeParts = parts[0].split(\":\");",
                            "        if (scopeParts.length >= 2) {",
                            "            pm.environment.set(\"projectKey\", scopeParts[1]);",
                            "        }",
                            "    }",
                            "}"
                        ]
                    }
                }
            ],
            "request": {
                "auth": {
                    "type": "basic",
                    "basic": {
                        "username": "{{ctp_client_id}}",
                        "password": "{{ctp_client_secret}}"
                    }
                },
                "method": "POST",
                "header": [],
                "body": {
                    "mode": "raw",
                    "raw": ""
                },
                "url": {
                    "raw": "https://{{auth_url}}<oauth.uri.path>?grant_type=client_credentials",
                    "protocol": "https",
                    "host": [
                        "{{auth_url}}"
                    ],
                    "path": [
                        "<oauth.uri.pathElements; separator="\",\"">"
                    ],
                    "query": [
                        {
                            "key": "grant_type",
                            "value": "client_credentials",
                            "equals": true
                        }
                    ]
                },
                "description": "Use this request to obtain an access token for your commercetools platform project via Client Credentials Flow. As a prerequisite you must have filled out environment variables in Postman for projectKey, client_id and client_secret to use this."
            },
            "response": []
        },
        {
            "name": "Obtain access token through password flow",
            "event": [
                {
                    "listen": "test",
                    "script": {
                        "type": "text/javascript",
                        "exec": [
                            "tests[\"Status code is 200\"] = responseCode.code === 200;"
                        ]
                    }
                }
            ],
            "request": {
                "auth": {
                    "type": "basic",
                    "basic": {
                        "username": "{{ctp_client_id}}",
                        "password": "{{ctp_client_secret}}"
                    }
                },
                "method": "POST",
                "header": [
                    {
                        "key": "",
                        "value": "",
                        "disabled": true
                    }
                ],
                "body": {
                    "mode": "raw",
                    "raw": ""
                },
                "url": {
                    "raw": "https://{{auth_url}}/oauth/{{projectKey}}/customers/token?grant_type=password&username={{user_email}}&password={{user_password}}",
                    "protocol": "https",
                    "host": [
                        "{{auth_url}}"
                    ],
                    "path": [
                        "oauth",
                        "{{projectKey}}",
                        "customers",
                        "token"
                    ],
                    "query": [
                        {
                            "key": "grant_type",
                            "value": "password",
                            "equals": true
                        },
                        {
                            "key": "username",
                            "value": "",
                            "equals": true
                        },
                        {
                            "key": "password",
                            "value": "",
                            "equals": true
                        },
                        {
                            "key": "scope",
                            "value": "manage_project:{{projectKey}}",
                            "equals": true
                        }
                    ]
                },
                "description": "Use this request to obtain an access token for your commercetools platform project via Password Flow. As a prerequisite you must have filled out environment variables in Postman for projectKey, client_id, client_secret, user_email and user_password to use this."
            },
            "response": []
        },
        {
            "name": "Token for Anonymous Sessions",
            "event": [
                {
                    "listen": "test",
                    "script": {
                        "type": "text/javascript",
                        "exec": [
                            "tests[\"Status code is 200\"] = responseCode.code === 200;"
                        ]
                    }
                }
            ],
            "request": {
                "auth": {
                    "type": "basic",
                    "basic": {
                        "username": "{{ctp_client_id}}",
                        "password": "{{ctp_client_secret}}"
                    }
                },
                "method": "POST",
                "header": [],
                "body": {
                    "mode": "raw",
                    "raw": ""
                },
                "url": {
                    "raw": "https://{{auth_url}}/oauth/{{projectKey}}/anonymous/token?grant_type=client_credentials&scope=manage_my_profile:{{projectKey}}",
                    "protocol": "https",
                    "host": [
                        "{{auth_url}}"
                    ],
                    "path": [
                        "oauth",
                        "{{projectKey}}",
                        "anonymous",
                        "token"
                    ],
                    "query": [
                        {
                            "key": "grant_type",
                            "value": "client_credentials",
                            "equals": true
                        },
                        {
                            "key": "scope",
                            "value": "manage_my_profile:{{projectKey}}",
                            "equals": true
                        }
                    ]
                },
                "description": "Use this request to obtain an access token for a anonymous session. As a prerequisite you must have filled out environment variables in Postman for projectKey, client_id and client_secret to use this."
            },
            "response": []
        },
        {
            "name": "Token Introspection",
            "event": [
                {
                    "listen": "test",
                    "script": {
                        "type": "text/javascript",
                        "exec": [
                            "tests[\"Status code is 200\"] = responseCode.code === 200;"
                        ]
                    }
                }
            ],
            "request": {
                "auth": {
                    "type": "basic",
                    "basic": {
                        "username": "{{ctp_client_id}}",
                        "password": "{{ctp_client_secret}}"
                    }
                },
                "method": "POST",
                "header": [
                    {
                        "key": "Content-Type",
                        "value": "application/json"
                    }
                ],
                "body": {
                    "mode": "raw",
                    "raw": ""
                },
                "url": {
                    "raw": "https://{{auth_url}}/oauth/introspect?token={{ctp_access_token}}",
                    "protocol": "https",
                    "host": [
                        "{{auth_url}}"
                    ],
                    "path": [
                        "oauth",
                        "introspect"
                    ],
                    "query": [
                        {
                            "key": "token",
                            "value": "{{ctp_access_token}}",
                            "equals": true
                        }
                    ]
                },
                "description": "Token introspection allows to determine the active state of an OAuth 2.0 access token and to determine meta-information about this accces token, such as the `scope`."
            },
            "response": []
        }
    ]
}
>>

     */
}

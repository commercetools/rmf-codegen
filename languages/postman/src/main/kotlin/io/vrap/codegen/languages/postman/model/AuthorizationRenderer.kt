package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.raml.model.security.OAuth20Settings

fun authorization(oauth: OAuth20Settings): String {
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
            |                            <<${testAuthScript().jScript()}>>
            |                        ]
            |                    }
            |                }
            |            ],
            |            "request": {
            |                "auth": {
            |                    "type": "basic",
            |                    "basic": {
            |                        "username": "{{client_id}}",
            |                        "password": "{{client_secret}}"
            |                    }
            |                },
            |                "method": "POST",
            |                "header": [],
            |                "body": {
            |                    "mode": "raw",
            |                    "raw": ""
            |                },
            |                "url": {
            |                    "raw": "{{auth_url}}${oauth.uri().path}?grant_type=client_credentials",
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
            |                            <<${testAuthScript().jScript()}>>
            |                        ]
            |                    }
            |                }
            |            ],
            |            "request": {
            |                "auth": {
            |                    "type": "basic",
            |                    "basic": {
            |                        "username": "{{client_id}}",
            |                        "password": "{{client_secret}}"
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
            |                    "raw": "{{auth_url}}/oauth/{{projectKey}}/customers/token?grant_type=password&username={{user_email}}&password={{user_password}}",
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
            |                            <<${testAuthScript().jScript()}>>
            |                        ]
            |                    }
            |                }
            |            ],
            |            "request": {
            |                "auth": {
            |                    "type": "basic",
            |                    "basic": {
            |                        "username": "{{client_id}}",
            |                        "password": "{{client_secret}}"
            |                    }
            |                },
            |                "method": "POST",
            |                "header": [],
            |                "body": {
            |                    "mode": "raw",
            |                    "raw": ""
            |                },
            |                "url": {
            |                    "raw": "{{auth_url}}/oauth/{{projectKey}}/anonymous/token?grant_type=client_credentials",
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
            |                            <<${ "tests[\"Status code is 200\"] = responseCode.code === 200;".jScript() }>>
            |                        ]
            |                    }
            |                }
            |            ],
            |            "request": {
            |                "auth": {
            |                    "type": "basic",
            |                    "basic": {
            |                        "username": "{{client_id}}",
            |                        "password": "{{client_secret}}"
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
            |                    "raw": "{{auth_url}}/oauth/introspect?token={{ctp_access_token}}",
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
        """.trimMargin().keepAngleIndent().escapeAll()
}

private fun testAuthScript(): String {
    return """
                |tests["Status code is 200"] = responseCode.code === 200;
                |var data = JSON.parse(responseBody);
                |if(data.access_token){
                |    pm.environment.set("ctp_access_token", data.access_token);
                |}
                |if (data.scope) {
                |    parts = data.scope.split(" ");
                |    parts = parts.filter(scope => scope.includes(":")).map(scope => scope.split(":"))
                |    if (parts.length > 0) {
                |        scopeParts = parts[0];
                |        pm.environment.set("projectKey", scopeParts[1]);
                |        parts = parts.filter(scope => scope.length >= 3)
                |        if (parts.length > 0) {
                |            scopeParts = parts[0];
                |            pm.environment.set("storeKey", scopeParts[2]);
                |        }
                |    }
                |}
            """.trimMargin()
}

fun auth(): String {
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

private fun String.jScript(): String {
    return this.split("\n").map { it.escapeJson().escapeAll() }.joinToString("\",\n\"", "\"", "\"");
}

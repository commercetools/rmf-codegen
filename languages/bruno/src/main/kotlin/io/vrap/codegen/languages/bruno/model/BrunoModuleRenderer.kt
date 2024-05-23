package io.vrap.codegen.languages.bruno.model

import com.damnhandy.uri.template.UriTemplate
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*

class BrunoModuleRenderer constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                brunoJson(api),
                collectionBru(api),
                clientCredentialsBru(api),
                exampleEnvironment(api),
                dotEnvEnvironment(api),
                dotEnvSample(api),
                gitIgnore()
        )
    }

    private fun exampleEnvironment(api: Api): TemplateFile {
        val baseUri = when (val sdkBaseUri = api.getAnnotation("sdkBaseUri")?.value) {
            is StringInstance -> sdkBaseUri.value
            else -> api.baseUri.template
        }.trimEnd('/')
        return TemplateFile(relativePath = "environments/Example.bru",
                content = """
                    |vars {
                    |  authUrl: ${api.oAuth2().uri().toString().trimEnd('/')}
                    |  apiUrl: $baseUri
                    |  projectKey:
                    |}
                    |vars:secret [
                    |  ctp_client_id,
                    |  ctp_client_secret,
                    |  ctp_access_token
                    |]
                """.trimMargin()
        )
    }

    private fun gitIgnore(): TemplateFile {
        return TemplateFile(relativePath = ".gitignore",
                content = """
                    |.env
                """.trimMargin()
        )
    }
    private fun dotEnvSample(api: Api): TemplateFile {
        return TemplateFile(relativePath = ".env.sample",
                content = """
                    |CTP_CLIENT_ID=
                    |CTP_CLIENT_SECRET=
                """.trimMargin()
        )
    }

    private fun dotEnvEnvironment(api: Api): TemplateFile {
        val baseUri = when (val sdkBaseUri = api.getAnnotation("sdkBaseUri")?.value) {
            is StringInstance -> sdkBaseUri.value
            else -> api.baseUri.template
        }.trimEnd('/')
        return TemplateFile(relativePath = "environments/DotEnv.bru",
                content = """
                    |vars {
                    |  authUrl: ${api.oAuth2().uri().toString().trimEnd('/')}
                    |  apiUrl: $baseUri
                    |  projectKey:
                    |  ctp_client_id: {{process.env.CTP_CLIENT_ID}}
                    |  ctp_client_secret: {{process.env.CTP_CLIENT_SECRET}}
                    |}
                    |vars:secret [
                    |  ctp_access_token
                    |]
                """.trimMargin().keepAngleIndent()
        )
    }

    private fun brunoJson(api: Api): TemplateFile {
        return TemplateFile(relativePath = "bruno.json",
                content = """
                    |{
                    |  "version": "1",
                    |  "name": "${api.title}",
                    |  "type": "collection",
                    |  "ignore": [
                    |    "node_modules",
                    |    ".git"
                    |  ]
                    |}
                """.trimMargin()
        )
    }

    private fun collectionBru(api: Api): TemplateFile {
        return TemplateFile(relativePath = "collection.bru",
                content = """
                    |auth {
                    |  mode: bearer
                    |}
                    |
                    |auth:bearer {
                    |  token: {{ctp_access_token}}
                    |}
                """.trimMargin()
        )
    }

    private fun clientCredentialsBru(api: Api): TemplateFile {
        return TemplateFile(relativePath = "auth/clientCredentials.bru",
                content = """
                    |meta {
                    |  name: Client Credentials
                    |  type: http
                    |  seq: 1
                    |}
                    |
                    |post {
                    |  url: {{authUrl}}
                    |  body: formUrlEncoded
                    |  auth: basic
                    |}
                    |
                    |body:form-urlencoded {
                    |  grant_type: client_credentials
                    |}
                    |
                    |auth:basic {
                    |  username: {{ctp_client_id}}
                    |  password: {{ctp_client_secret}}
                    |}
                    |
                    |script:post-response {
                    |  if(res.status == 200) {
                    |    var data = res.body;
                    |    if(data.access_token){
                    |      bru.setEnvVar("ctp_access_token", data.access_token, true);
                    |    }
                    |  
                    |    if (data.scope) {
                    |      parts = data.scope.split(" ");
                    |      parts = parts.filter(scope => scope.includes(":")).map(scope => scope.split(":"))
                    |      if (parts.length > 0) {
                    |          scopeParts = parts[0];
                    |          bru.setEnvVar("projectKey", scopeParts[1]);
                    |          parts = parts.filter(scope => scope.length >= 3)
                    |          if (parts.length > 0) {
                    |              scopeParts = parts[0];
                    |              bru.setEnvVar("storeKey", scopeParts[2]);
                    |          }
                    |      }
                    |    }
                    |  }
                    |}
                    |
                    |assert {
                    |  res.status: eq 200
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
                    |        <<${api.resources.joinToString(",") { ResourceRenderer().render(it) }}>>
                    |    ]
                    |}
                """.trimMargin().keepAngleIndent())
    }

    private fun readme(api: Api): String {
        return """
            # commercetools API Postman Collection

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
            2. Import the [collection.json](collection.json) and [template.json](template.json) in your postman application
            3. In the Merchant Center, create a new API Client and fill in the client credentials in your environment
            4. Obtain an access token by sending the "Authorization/Obtain access token" request at the bottom of the request list. Now you can use all other endpoints
    
            Feel free to clone and modify this collection to your needs.

            To automate frequent tasks the collection automatically manages commonly required values and parameters such
            as resource ids, keys and versions in Postman environment variables for you.

            Please see http://docs.commercetools.com/ for further information about the commercetools Plattform.
        """.trimIndent()
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

fun Resource.testScript(param: String = ""): String {
    return """
            |tests["Status code " + responseCode.code] = responseCode.code === 200 || responseCode.code === 201;
            |var data = JSON.parse(responseBody);
            |if(data.results && data.results[0] && data.results[0].id && data.results[0].version){
            |    pm.environment.set("${this.resourcePathName.singularize()}-id", data.results[0].id); 
            |    pm.environment.set("${this.resourcePathName.singularize()}-version", data.results[0].version);
            |}
            |if(data.results && data.results[0] && data.results[0].key){
            |    pm.environment.set("${this.resourcePathName.singularize()}-key", data.results[0].key); 
            |}
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
        """.trimMargin().split("\n").map { it.escapeJson().escapeAll() }.joinToString("\",\n\"", "\"", "\"")
}

package io.vrap.codegen.languages.bruno.model

import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*

class BrunoModuleRenderer(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                brunoJson(api),
                collectionBru(),
                clientCredentialsBru(),
                exampleEnvironment(api),
                dotEnvEnvironment(api),
                dotEnvSample(),
                gitIgnore()
        )
    }

    private fun readme(api: Api): String {
        return """
            # commercetools API Bruno Collection

            This collection contains examples of requests and responses for most endpoints and commands of the
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

            1. Download and install the Bruno Client
            1. Fork the repository
            1. Open the collection
            1. In the Merchant Center, create a new API Client
            1. Select the environment and configure the client credentials in the variable `ctp_client_id` and `ctp_client_secret`
               or create an `.env` file and put it in the collection folder. An sample file is part of the collection.
            1. Obtain an access token by sending the "Auth/Client credentials" request. Now you can use all other endpoints
    
            Feel free to clone and modify this collection to your needs. This collection gets automatically
            updated and you can pull the latest changes.

            To automate frequent tasks the collection automatically manages commonly required values and parameters such
            as resource ids, keys and versions in environment variables for you.

            Please see http://docs.commercetools.com/ for further information about the commercetools Plattform.
        """.trimIndent()
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
                    |  project-key:
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
    private fun dotEnvSample(): TemplateFile {
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
                    |  project-key:
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

    private fun collectionBru(): TemplateFile {
        return TemplateFile(relativePath = "collection.bru",
                content = """
                    |auth {
                    |  mode: bearer
                    |}
                    |
                    |auth:bearer {
                    |  token: {{ctp_access_token}}
                    |}
                    |
                    |docs {
                    |  <<${readme(api)}>>
                    |}
                """.trimMargin().keepAngleIndent()
        )
    }

    private fun clientCredentialsBru(): TemplateFile {
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
                    |          bru.setEnvVar("project-key", scopeParts[1]);
                    |          parts = parts.filter(scope => scope.length >= 3)
                    |          if (parts.length > 0) {
                    |              scopeParts = parts[0];
                    |              bru.setEnvVar("store-key", scopeParts[2]);
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
}

fun Resource.testScript(param: String = ""): String {
    return """
            |var data = res.body;
            |if(res.status == 200 || res.status == 201) {
            |    if(data.results && data.results[0] && data.results[0].id && data.results[0].version){
            |        bru.setEnvVar("${this.resourcePathName.singularize()}-id", data.results[0].id); 
            |        bru.setEnvVar("${this.resourcePathName.singularize()}-version", data.results[0].version);
            |    }
            |    if(data.results && data.results[0] && data.results[0].key){
            |        bru.setEnvVar("${this.resourcePathName.singularize()}-key", data.results[0].key); 
            |    }
            |    if(data.version){
            |        bru.setEnvVar("${this.resourcePathName.singularize()}-version", data.version);
            |    }
            |    if(data.id){
            |        bru.setEnvVar("${this.resourcePathName.singularize()}-id", data.id); 
            |    }
            |    if(data.key){
            |        bru.setEnvVar("${this.resourcePathName.singularize()}-key", data.key);
            |    }
            |   ${if (param.isNotEmpty()) """
            |   if(data.${param}){
            |       bru.setEnvVar("${this.resourcePathName.singularize()}-${param}", data.${param});
            |   }
            |""".trimMargin() else ""}
            |}
        """.trimMargin()
}

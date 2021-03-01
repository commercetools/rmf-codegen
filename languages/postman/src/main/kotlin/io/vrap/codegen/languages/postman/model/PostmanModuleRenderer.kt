package io.vrap.codegen.languages.postman.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*

class PostmanModuleRenderer constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {

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
                    |      "value": "${api.baseUri.template.trimEnd('/')}",
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
                    |        <<${api.resources.joinToString(",") { ResourceRenderer().render(it) }}>>
                    |    ]
                    |}
                """.trimMargin().keepAngleIndent())
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

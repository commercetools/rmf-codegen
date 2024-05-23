package io.vrap.codegen.languages.bruno.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.google.common.collect.Lists
import io.vrap.codegen.languages.extensions.*
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.MethodRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject

class BrunoMethodRenderer constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {

    val offset = 1

    fun allResourceMethods(): List<Method> = api.allContainedResources.flatMap { it.methods }

    override fun produceFiles(): List<TemplateFile> {
        return methods(api)
    }

    fun methods(api: Api): List<TemplateFile> {
        return allResourceMethods().mapIndexed { index, method -> render(index, method) }
    }

    fun render(index: Int, type: Method): TemplateFile {

        val content = renderStr(index, type).trimMargin().keepAngleIndent()

        val relativePath = methodResourcePath(type) + "/" + type.toRequestName() + ".bru"

        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    fun renderStr(index: Int, method: Method): String {
        val url = BrunoUrl(method.resource(), method) { resource, name -> when (name) {
            "ID" -> resource.resourcePathName.singularize() + "-id"
            "key" -> resource.resourcePathName.singularize() + "-key"
            else -> StringCaseFormat.LOWER_HYPHEN_CASE.apply(name)
        }}
        return """
            |meta {
            |  name: ${method.displayName?.value ?: "${method.methodName} ${method.resource().toResourceName()}" }
            |  type: http
            |  seq: ${index + offset}
            |}
            | 
            |${method.methodName} {
            |  url: ${url.raw()}
            |  body: ${method.bodyType()}
            |  auth: inherit
            |}
            | 
            |<<${method.jsonBody()}>>
            | 
            |query {
            |  <<${url.query()}>>
            |}
            |
            |script:post-response {
            |  <<${method.resource().testScript()}>>
            |}
            |
            |assert {
            |  res.status: in [200, 201]
            |}
        """.trimMargin()
    }

    fun Method.bodyType(): String {
        return if (this.getExample() != null) "json" else "none"
    }

    fun Method.jsonBody(): String {
        val s = this.getExample()
        return if (s != null) {
            """|body:json {
               |  <<${s}>>
               |}
            """.trimMargin()
        } else ""
    }

    fun Method.getExample(): String? {
        val s = this.bodies?.
        getOrNull(0)?.
        type?.
        examples?.
        getOrNull(0)?.
        value
        return s?.toJson()
    }

    private fun methodResourcePath(method: Method): String {
        var resourcePathes = resourcePathes(method.resource())

        var directories = resourcePathes.map { it.displayName?.value ?: it.resourcePathName.firstUpperCase() }
        return directories.joinToString("/")
    }

    private fun resourcePathes(resource: Resource): List<Resource> {
        if (resource.parent is Resource) {
            if (resource.resourcePathName == resource.parent.resourcePathName) {
                return resourcePathes(resource.parent)
            }
            return resourcePathes(resource.parent).plus(resource)
        }
        return listOf(resource)
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
}



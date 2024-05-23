package io.vrap.codegen.languages.bruno.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ObjectNode
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
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject
import java.io.IOException

class BrunoActionRenderer constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {

    val offset = 1 + allResourceMethods().count()

    fun allResourceMethods(): List<Method> = api.allContainedResources.flatMap { it.methods }
    fun allResources(): List<Resource> = api.allContainedResources

    override fun produceFiles(): List<TemplateFile> {
        return updateActions(api)
    }

    fun updateActions(api: Api): List<TemplateFile> {
        val updatableResources = api.allContainedResources.filter { it.getAnnotation("updateable") != null }

        return updatableResources.flatMap { resourceUpdates(it) }
    }

    fun resourceUpdates(resource: Resource): List<TemplateFile> {
        val updateMethod = resource.getUpdateMethod()
        return updateMethod?.getActions()?.filterNot { objType -> objType.deprecated() }?.mapIndexed { index, objectType ->  renderAction(resource, updateMethod, objectType, index) } ?: return emptyList()
    }

    private fun renderAction(resource: Resource, method: Method, type: ObjectType, index: Int): TemplateFile {
        val url = BrunoUrl(method.resource(), method) { resource, name -> when (name) {
            "ID" -> resource.resourcePathName.singularize() + "-id"
            "key" -> resource.resourcePathName.singularize() + "-key"
            else -> StringCaseFormat.LOWER_HYPHEN_CASE.apply(name)
        }}
        val actionBody = resource.actionExample(type)
        val name = "${type.discriminatorValue.firstUpperCase()}${if (type.markDeprecated()) " (deprecated)" else ""}"
        val content = BrunoRequestRenderer.renderRequest(name, method, url, actionBody, index + offset)

        val relativePath = methodResourcePath(method) + "/Update actions/" + type.discriminatorValue.firstUpperCase() + ".bru"

        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun Resource.actionExample(type: ObjectType): String {
        val example = getExample(type)
        return """
            |{
            |    "version": {{${this.resourcePathName.singularize()}-version}},
            |    "actions": [
            |        <<${if (example.isNullOrEmpty().not()) example else """
            |        |{
            |        |    "action": "${type.discriminatorValue}"
            |        |}""".trimMargin()}>>
            |    ]
            |}
        """.trimMargin().keepAngleIndent()
    }

    private fun getExample(type: ObjectType): String? {
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

        return example
    }

    private fun ObjectType.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    private fun Resource.getUpdateMethod(): Method? {
        val byIdResource = this.resources.find { resource -> resource.relativeUri.template == "/{ID}" } ?: return null

        return byIdResource.getMethod(HttpMethod.POST)
    }

    private fun Method.getActions(): List<ObjectType> {
        val body = this.getBody("application/json") ?: return emptyList()

        val actions = (body.type as ObjectType).getProperty("actions") ?: return emptyList()

        val actionsType = actions.type as ArrayType
        val updateActions = if (actionsType.items is UnionType) {
            (actionsType.items as UnionType).oneOf[0].subTypes
        } else {
            actionsType.items.subTypes
        }
        val actionItems = updateActions.map { action -> action as ObjectType }.sortedBy { action -> action.discriminatorValue }
        return actionItems
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
}



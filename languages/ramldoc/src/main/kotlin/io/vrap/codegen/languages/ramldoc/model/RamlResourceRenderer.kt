package io.vrap.codegen.languages.ramldoc.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.isSuccessfull
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.ramldoc.extensions.InstanceSerializer
import io.vrap.codegen.languages.ramldoc.extensions.ObjectInstanceSerializer
import io.vrap.codegen.languages.ramldoc.extensions.renderType
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.UriParameter
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.responses.Response
import io.vrap.rmf.raml.model.security.SecuredBy
import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.ecore.EObject

class RamlResourceRenderer @Inject constructor(val api: Api, val vrapTypeProvider: VrapTypeProvider) : ResourceRenderer {
    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val content = """
            |#${type.toResourceName()}
            |(resourcePathUri): ${type.fullUri.template}
            |${if (type.fullUriParameters.size > 0) """
            |uriParameters:
            |  <<${type.fullUriParameters.joinToString("\n") { renderUriParameter(it) }}>>""" else ""}
            |${type.methods.joinToString("\n") { renderMethod(it) }}
        """.trimMargin().keepIndentation("<<", ">>")
        val relativePath = "resources/" + type.toResourceName()+ ".raml"
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun renderMethod(method: Method): String {
        return """
            |${method.methodName}:${if (method.securedBy.isNotEmpty()) """
            |  securedBy:
            |  <<${method.securedBy.joinToString("\n") { renderScheme(it)}}>>""" else ""}${if (method.description != null) """
            |  description: |-
            |    <<${method.description.value.trim()}>>""" else ""}${if (method.queryParameters.isNotEmpty()) """
            |  queryParameters:
            |    <<${method.queryParameters.joinToString("\n") { renderQueryParameter(it) }}>>""" else ""}${if (method.bodies.any { it.type != null }) """
            |  body:
            |    <<${method.bodies.filter { it.type != null }.joinToString("\n") { renderBody(it) } }>>""" else ""}${if (method.responses.any { response -> response.isSuccessfull() }) """
            |  responses:
            |    <<${method.responses.filter { response -> response.isSuccessfull() }.joinToString("\n") { renderResponse(it) }}>>""" else ""}
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderScheme(securedBy: SecuredBy): String {
        return """
            |- ${securedBy.scheme.name}:
            |    <<${securedBy.parameters.toYaml()}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderResponse(response: Response): String {
        return """
            |${response.statusCode}:
            |  body:
            |    <<${response.bodies.joinToString("\n") { renderBody(it) } }>>
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderBody(body: Body): String {
        return """
            |${body.contentType}:
            |  <<${body.type.renderType()}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }
    private fun renderUriParameter(uriParameter: UriParameter): String {
        return """
            |${uriParameter.name}:
            |  <<${uriParameter.type.renderType(false)}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderQueryParameter(queryParameter: QueryParameter): String {
        return """
            |${queryParameter.name}:${if (queryParameter.type.default != null) """
            |  default: ${queryParameter.type.default.value}""" else ""}
            |  required: ${queryParameter.required}
            |  <<${queryParameter.type.renderType(false)}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }
}

fun Instance.toYaml(): String {
    var example = ""
    val mapper = YAMLMapper()
    mapper.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)

    val module = SimpleModule()
    module.addSerializer(ObjectInstance::class.java, ObjectInstanceSerializer())
    module.addSerializer<Instance>(ArrayInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(IntegerInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(BooleanInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(StringInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(NumberInstance::class.java, InstanceSerializer())
    mapper.registerModule(module)

    if (this is StringInstance) {
        example = mapper.writeValueAsString(this.value)
    } else if (this is ObjectInstance) {
        try {
            example = mapper.writeValueAsString(this)
        } catch (e: JsonProcessingException) {
        }
    }

    return example.trim()
}

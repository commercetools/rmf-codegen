package io.vrap.codegen.languages.javalang.client.builder.requests

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toParamName
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.JavaEObjectTypeExtensions
import io.vrap.codegen.languages.java.base.extensions.simpleName
import io.vrap.codegen.languages.java.base.extensions.toJavaVType
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer

class JavaRequestBuilderResourceRenderer @Inject constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : ResourceRenderer, JavaEObjectTypeExtensions {

    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType
        val resourceName : String = type.toResourceName()
        val className : String = "${resourceName}RequestBuilder"


        val content : String = """
            |package ${vrapType.`package`};
            |import io.vrap.rmf.base.client.ApiHttpClient;
            |
            |<${JavaSubTemplates.generatedAnnotation}>
            |public class $className {
            |   
            |   <${type.fields()}>
            |   
            |   <${type.constructor()}>
            |   
            |   <${type.methods()}>
            |   
            |   <${type.subResources()}>
            |}
            |
        """.trimMargin().keepIndentation()

        return TemplateFile(
                relativePath = "${vrapType.`package`}.$className".replace(".", "/") + ".java",
                content = content
        )
    }

    private fun Resource.allFields(): List<NamedField> {

        return listOf(NamedField(apiHttpClient,"apiHttpClient"))
                .plus(
                        this.pathArguments().map {
                            NamedField(VrapScalarType("String"), it)
                        }
                )

    }

    private fun Resource.fields() : String {

        return this.allFields()
                .map {
                    "private final ${it.type.toJavaVType().simpleName()} ${it.name}"
                }
                .joinToString(separator = ";\n",postfix = ";\n")

    }

    private fun Resource.constructor() : String {
        val resourceName : String = this.toResourceName()
        val className : String = "${resourceName}RequestBuilder"

        val constructorArguments : String = this.allFields().map { "final ${it.type.toJavaVType().simpleName()} ${it.name}" }.joinToString(separator = ",")
        val constructorAssignment : String = this.allFields().map { "this.${it.name} = ${it.name};" }.joinToString(separator = "\n")
        return """
            |public $className ($constructorArguments) {
            |   <$constructorAssignment>
            |}
        """.trimMargin()

    }

    private fun Resource.toResourceName(): String {
        return this.fullUri.toParamName("By")
    }

    private fun Resource.methods() : String {
        return this.methods.map { it.method() }.joinToString(separator = "\n\n")
    }

    private fun Method.method() : String {
        return """
            |public ${this.toRequestName()} ${this.method.name.toLowerCase()}(${this.constructorArguments()}) {
            |   return new ${this.toRequestName()}(${this.requestArguments()});
            |}
        """.trimMargin()
    }

    private fun Method.constructorArguments(): String? {
        return if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType()
            if(methodBodyVrapType is VrapObjectType) {
                val methodBodyArgument = "${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
                methodBodyArgument
            }else {
                "com.fasterxml.jackson.databind.JsonNode jsonNode"
            }
        }else {
            ""
        }
    }

    private fun Method.requestArguments() : String {
        val requestArguments = mutableListOf("apiHttpClient")
        this.pathArguments().forEach { requestArguments.add(it) }

        if(this.bodies != null && this.bodies.isNotEmpty()){
            val vrapType = this.bodies[0].type.toVrapType()
            if(vrapType is VrapObjectType) {
                requestArguments.add(vrapType.simpleClassName.decapitalize())
            }else {
                requestArguments.add("jsonNode")
            }
        }
        return requestArguments.joinToString(separator = ", ")
    }




    private fun ResourceContainer.subResources() : String {
        return this.resources.map {
            val args = if (it.relativeUri.variables.isNullOrEmpty()){
                ""
            }else {
                it.relativeUri.variables.map { "String $it" }.joinToString(separator = " ,")
            }
            val subResourceArgs : String = listOf("apiHttpClient")
                    .plus(
                            it.pathArguments()
                    )
                    .joinToString(separator = ", ")
            """
            |public ${it.toResourceName()}RequestBuilder ${it.getMethodName()}($args) {
            |   return new ${it.toResourceName()}RequestBuilder($subResourceArgs);
            |}
        """.trimMargin()
        }.joinToString(separator = "\n")
    }

    private fun Method.pathArguments() : List<String> = this.resource().pathArguments()

    private fun Resource.pathArguments(): List<String> = this.fullUri.variables.toList()
}
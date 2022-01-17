package io.vrap.codegen.languages.csharp.requests

import com.google.common.net.MediaType
import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.extensions.*
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.firstLowerCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.resources.impl.ResourceImpl
import io.vrap.rmf.raml.model.types.BooleanInstance
import java.util.*

class CsharpRequestBuilderResourceRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, private val basePackagePrefix: String) : ResourceRenderer, CsharpEObjectTypeExtensions {

    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toCsharpVType() as VrapObjectType
        val resourceName: String = type.toResourceName()
        val className: String = "${resourceName}RequestBuilder"
        val entityFolder = type.GetNameAsPlural()
        val cPackage = vrapType.requestBuildersPackage(entityFolder)

        val content: String = """
            |using System;
            |using System.Collections.Generic;
            |using System.IO;
            |using System.Text.Json;
            |using commercetools.Base.Client;
            |using commercetools.Base.Serialization;
            |<${type.subResourcesUsings()}>
            |
            |namespace $cPackage
            |{
            |   ${if (type.markDeprecated()) "[Obsolete(\"usage of this endpoint has been deprecated.\", false)]" else ""}
            |   public class $className {
            |
            |       <${type.properties()}>
            |   
            |       <${type.constructor()}>
            |   
            |       <${type.methods()}>
            |       
            |       <${type.subResources()}>
            |   }
            |}
        """.trimMargin().keepIndentation()

        val relativePath = cPackage.replace(basePackagePrefix, "").replace(".", "/").trimStart('/')

        return TemplateFile(
                relativePath = "${relativePath}/${className}.cs",
                content = content
        )
    }

    private fun Resource.toResourceName(): String {
        return this.fullUri.toParamName("By")
    }


    private fun Resource.properties(): String {

        var props = """
            |private IClient ApiHttpClient { get; }
            |
            |private ISerializerService SerializerService { get; }
        """.trimMargin().keepIndentation()

        return props + "\n\n" + this.pathArguments().map { "private string ${it.firstUpperCase()} { get; }" }.joinToString(separator = "\n\n")


    }

    private fun Resource.constructor(): String {
        val resourceName: String = this.toResourceName()
        val className: String = "${resourceName}RequestBuilder"

        val constructorArguments = mutableListOf("IClient apiHttpClient", "ISerializerService serializerService")
        val constructorAssignments = mutableListOf("this.ApiHttpClient = apiHttpClient;", "this.SerializerService = serializerService;")

        this.pathArguments().map { "string ${it.lowerCamelCase()}" }.forEach { constructorArguments.add(it) }
        this.pathArguments().map { "this.${it.firstUpperCase()} = ${it.lowerCamelCase()};" }.forEach { constructorAssignments.add(it) }

        return """
            |public $className (${constructorArguments.joinToString(separator = ", ")}) {
            |    <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation()

    }

    private fun Resource.methods(): String {
        return this.methods.map { it.method() }.joinToString(separator = "\n\n")
    }

    private fun Method.method(): String {
        return """
            |public ${this.toRequestName()} ${this.method.name.upperCamelCase()}(${this.constructorArguments()}) {
            |    return new ${this.toRequestName()}(${this.requestArguments()});
            |}
        """.trimMargin()
    }

    private fun Method.constructorArguments(): String? {
        return if (this.bodies != null && this.bodies.isNotEmpty()) {
            val methodBodyVrapType = this.bodies[0].type.toVrapType()
            if (methodBodyVrapType is VrapObjectType) {
                val methodBodyArgument: String
                if (methodBodyVrapType.`package` == "")
                    methodBodyArgument =
                        "${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.firstLowerCase()}"
                else
                    methodBodyArgument =
                        "${methodBodyVrapType.`package`}.I${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.firstLowerCase()}"
                methodBodyArgument
            } else if (this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA)) {
               "List<KeyValuePair<string, string>> formParams = null".escapeAll()
            } else {
                "JsonElement? jsonNode"
            }
        } else {
            ""
        }
    }

    private fun Method.requestArguments(): String {
        val requestArguments = mutableListOf("ApiHttpClient")
        if (this.methodName.lowercase(Locale.getDefault()) == "post") {
            requestArguments.add("SerializerService")
        }
        this.pathArguments().forEach { requestArguments.add(it.firstUpperCase()) }

        if (this.bodies != null && this.bodies.isNotEmpty()) {
            val vrapType = this.bodies[0].type.toVrapType()
            if (vrapType is VrapObjectType) {
                requestArguments.add(vrapType.simpleClassName.firstLowerCase())
            } else if (this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA)) {
                requestArguments.add("formParams")
            } else {
                requestArguments.add("jsonNode")
            }
        }
        return requestArguments.joinToString(separator = ", ")
    }

    private fun ResourceContainer.subResources() : String {
        return this.resources.filterNot { it.deprecated() }.map {
            val args = if (it.relativeUri.variables.isNullOrEmpty()){
                ""
            }else {
                it.relativeUri.variables.map { "string $it" }.joinToString(separator = " ,")
            }
            val subResourceArgs : String = listOf("ApiHttpClient", "SerializerService")
                    .plus(
                            it.pathArguments().map
                            {
                                if(args.contains(it, true)) it else it.firstUpperCase()
                            }
                    )
                    .joinToString(separator = ", ")
            """
            |${if (it.markDeprecated()) "[Obsolete(\"usage of this endpoint has been deprecated.\", false)]" else ""}
            |public ${it.toResourceName()}RequestBuilder ${it.getMethodName().firstUpperCase()}($args) {
            |    return new ${it.toResourceName()}RequestBuilder($subResourceArgs);
            |}
        """.trimMargin()
        }.joinToString(separator = "\n")
    }

    private fun Resource.deprecated() : Boolean {
        val anno = this.getAnnotation("deprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    private fun Resource.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    private fun Method.pathArguments() : List<String> = this.resource().pathArguments()

    private fun Resource.pathArguments(): List<String> = this.fullUri.variables.toList()

    private fun ResourceContainer.subResourcesUsings() : String {
        var listOfUsings = mutableListOf<String>()
        this.resources.map {
            var r = it as ResourceImpl
            var using = "using ${r.toVrapType().requestBuildersPackage(r.GetNameAsPlural())};"
            if(!listOfUsings.contains(using))
                listOfUsings.add(using)
        }
        return listOfUsings.joinToString(separator = "\n")
    }

}

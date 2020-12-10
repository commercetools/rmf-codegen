package io.vrap.codegen.languages.csharp.requests

import com.google.inject.Inject
import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.resources.impl.ResourceImpl

class CsharpRequestBuilderResourceRenderer @Inject constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : ResourceRenderer, CsharpEObjectTypeExtensions {

    @Inject
    @BasePackageName
    lateinit var basePackagePrefix:String

    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toCsharpVType() as VrapObjectType
        val resourceName: String = type.toResourceName()
        val className: String = "${resourceName}RequestBuilder"
        val entityFolder = type.GetNameAsPlurar()
        val cPackage = vrapType.requestBuildersPackage(entityFolder)

        val content: String = """
            |using System.IO;
            |using System.Text.Json;
            |using commercetools.Base.Client;
            |using commercetools.Base.Serialization;
            |<${type.subResourcesUsings()}>
            |
            |namespace $cPackage
            |{
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

        val relativePath = vrapType.requestBuildersPackage(entityFolder).replace(basePackagePrefix, "").replace(".", "/").trimStart('/')

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

        return props + "\n\n" + this.pathArguments().map { "private string ${it.capitalize()} { get; }" }.joinToString(separator = "\n\n")


    }

    private fun Resource.constructor(): String {
        val resourceName: String = this.toResourceName()
        val className: String = "${resourceName}RequestBuilder"

        val constructorArguments = mutableListOf("IClient apiHttpClient", "ISerializerService serializerService")
        val constructorAssignments = mutableListOf("this.ApiHttpClient = apiHttpClient;", "this.SerializerService = serializerService;")

        this.pathArguments().map { "string ${it.lowerCamelCase()}" }.forEach { constructorArguments.add(it) }
        this.pathArguments().map { "this.${it.capitalize()} = ${it.lowerCamelCase()};" }.forEach { constructorAssignments.add(it) }

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
                var methodBodyArgument = ""
                if(methodBodyVrapType.`package`=="")
                    methodBodyArgument = "${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
                else
                    methodBodyArgument = "${methodBodyVrapType.`package`}.I${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
                methodBodyArgument
            } else {
                "JsonElement jsonNode"
            }
        } else {
            ""
        }
    }

    private fun Method.requestArguments(): String {
        val requestArguments = mutableListOf("ApiHttpClient")
        if(this.methodName.toLowerCase() == "post")
        {
            requestArguments.add("SerializerService")
        }
        this.pathArguments().forEach { requestArguments.add(it.capitalize()) }

        if (this.bodies != null && this.bodies.isNotEmpty()) {
            val vrapType = this.bodies[0].type.toVrapType()
            if (vrapType is VrapObjectType) {
                requestArguments.add(vrapType.simpleClassName.decapitalize())
            } else {
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
                it.relativeUri.variables.map { "string $it" }.joinToString(separator = " ,")
            }
            val subResourceArgs : String = listOf("ApiHttpClient", "SerializerService")
                    .plus(
                            it.pathArguments().map
                            {
                                if(args.contains(it, true)) it else it.capitalize()
                            }
                    )
                    .joinToString(separator = ", ")
            """
            |public ${it.toResourceName()}RequestBuilder ${it.getMethodName().capitalize()}($args) {
            |    return new ${it.toResourceName()}RequestBuilder($subResourceArgs);
            |}
        """.trimMargin()
        }.joinToString(separator = "\n")
    }

    private fun Method.pathArguments() : List<String> = this.resource().pathArguments()

    private fun Resource.pathArguments(): List<String> = this.fullUri.variables.toList()

    private fun ResourceContainer.subResourcesUsings() : String {
        var listOfUsings = mutableListOf<String>()
        this.resources.map {
            var r = it as ResourceImpl
            var using = "using ${r.toVrapType().requestBuildersPackage(r.GetNameAsPlurar())};"
            if(!listOfUsings.contains(using))
                listOfUsings.add(using)
        }
        return listOfUsings.joinToString(separator = "\n")
    }

}

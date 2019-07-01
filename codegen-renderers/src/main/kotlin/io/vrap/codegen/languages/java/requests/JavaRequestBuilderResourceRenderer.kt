package io.vrap.codegen.languages.java.requests

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.java.extensions.resource
import io.vrap.codegen.languages.java.extensions.toRequestName
import io.vrap.codegen.languages.php.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.php.extensions.toParamName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer

class JavaRequestBuilderResourceRenderer @Inject constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : ResourceRenderer, EObjectTypeExtensions {

    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType
        val resourceName : String = type.toResourceName()
        val className : String = "${resourceName}RequestBuilder"
        
        val content : String = """
            |package ${vrapType.`package`};
            |
            |public class $className {
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
    
    private fun Resource.toResourceName(): String {
        return this.fullUri.toParamName("By")
    }
    
    private fun Resource.methods() : String {
        return this.methods.map { it.method() }.joinToString(separator = "\n")
    }
    
    private fun Method.method() : String {
        return """
            |public ${this.toRequestName()} ${this.method.name.toLowerCase()}(${this.constructorArguments()}) {
            |   return new ${this.toRequestName()}(${this.requestArguments()});
            |}
        """.trimMargin()
    }

    private fun Method.constructorArguments(): String? {
        val constructorArguments = mutableListOf<String>()
        this.pathArguments().map { "String $it" }.forEach { constructorArguments.add(it) }
        
        if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            val methodBodyArgument = "${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
            constructorArguments.add(methodBodyArgument) 
        }

        return constructorArguments.joinToString(separator = ", ")
    }
    
    private fun Method.requestArguments() : String {
        val requestArguments = mutableListOf<String>()
        this.pathArguments().forEach { requestArguments.add(it) }

        if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            requestArguments.add(methodBodyVrapType.simpleClassName.decapitalize())
        }

        return requestArguments.joinToString(separator = ", ")
    }

    private fun Method.pathArguments() : List<String> {
        val urlPathParts = this.resource().fullUri.template.split("/").filter { it.isNotEmpty() }
        return urlPathParts.filter { it.startsWith("{") && it.endsWith("}") }.map { it.replace("{", "").replace("}", "") }
    }
    
    private fun ResourceContainer.subResources() : String {
        return this.resources.map { """
            |public ${it.toResourceName()}RequestBuilder ${it.getMethodName()}() {
            |   return new ${it.toResourceName()}RequestBuilder();
            |}
        """.trimMargin() 
        }.joinToString(separator = "\n")
    }
}
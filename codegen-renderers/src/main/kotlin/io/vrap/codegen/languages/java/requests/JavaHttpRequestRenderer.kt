package io.vrap.codegen.languages.java.requests

import com.google.inject.Inject
import io.vrap.codegen.languages.java.extensions.resource
import io.vrap.codegen.languages.java.extensions.returnType
import io.vrap.codegen.languages.java.extensions.toRequestName
import io.vrap.codegen.languages.php.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.php.extensions.ObjectTypeExtensions
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import org.eclipse.emf.ecore.EObject


class JavaHttpRequestRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, ObjectTypeExtensions, EObjectTypeExtensions {
    
    override fun render(type: Method): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val content = """
            |package ${vrapType.`package`};
            |
            |import client.HttpRequest;
            |import client.HttpMethod;
            |
            |public class ${type.toRequestName()} extends HttpRequest {
            |   
            |   <${type.fields()}>
            |   
            |   <${type.constructor()}>
            |}
        """.trimMargin().keepIndentation()
        
        return TemplateFile(
                relativePath = "${vrapType.`package`}.${type.toRequestName()}".replace(".", "/") + ".java",
                content = content
        )
    }
    
    
    private fun Method.constructor(): String? {
        val constructorArguments = mutableListOf<String>()
        val constructorAssignments = mutableListOf<String>()

        this.pathArguments().map { "String $it" }.forEach { constructorArguments.add(it) }
        this.pathArguments().map { "this.$it = $it;" }.forEach { constructorAssignments.add(it) }

        if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            val methodBodyArgument = "${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
            constructorArguments.add(methodBodyArgument)
            val methodBodyAssignment = "this.${methodBodyVrapType.simpleClassName.decapitalize()} = ${methodBodyVrapType.simpleClassName.decapitalize()};"
            constructorAssignments.add(methodBodyAssignment)
        }

        val path = this.resource().fullUri.template
        val parts = path.split("/").filter { it.isNotEmpty() }
        val pathArguments = parts.filter { it.startsWith("{") && it.endsWith("}")}
        var stringFormat = path

        pathArguments.forEach { stringFormat = stringFormat.replace(it, "%s") }

        val stringFormatArgs = pathArguments
                .map { it.replace("{", "").replace("}", "") }
                .map { "this.$it" }
                .joinToString(separator = ",")
        
        val httpBodyAssignment : String = if(this.bodyObjectName() != null) { "setBody(${this.bodyObjectName()}.toJson());" } else { "" }
        
        return """
            |public ${this.toRequestName()}(${constructorArguments.joinToString(separator = ", ")}){
            |   <${constructorAssignments.joinToString(separator = "\n")}>
            |   setMethod(HttpMethod.${this.method.name});
            |   setPath(String.format("$stringFormat", $stringFormatArgs));
            |   $httpBodyAssignment
            |}
        """.trimMargin().keepIndentation()
    }
    
    private fun Method.fields(): String? {
        val commandClassFields = mutableListOf<String>()
        this.pathArguments().map { "private String $it;" }.forEach { commandClassFields.add(it) }
        if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            commandClassFields.add("private ${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()};")
        }

        return commandClassFields.joinToString(separator = "\n\n")
    }

    private fun Method.pathArguments() : List<String> {
        val urlPathParts = this.resource().fullUri.template.split("/").filter { it.isNotEmpty() }
        return urlPathParts.filter { it.startsWith("{") && it.endsWith("}") }.map { it.replace("{", "").replace("}", "") }
    }

    private fun Method.bodyObjectName() : String? {
        if(this.bodies != null && this.bodies.isNotEmpty()) {
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            return methodBodyVrapType.simpleClassName.decapitalize()
        }else{
            return null
        }
    }

    private fun Method.jacksonJavaTypeMethod() : String {
        val commandReturnType = vrapTypeProvider.doSwitch(this.returnType()) as VrapObjectType

        return """
            |private JavaType jacksonJavaType() {
            |   return SphereJsonUtils.convertToJavaType(${commandReturnType.`package`}.${commandReturnType.simpleClassName}.typeReference());
            |}
        """.trimMargin()
    }
    
    private fun Method.javaReturnType() : String {
        val commandReturnType = vrapTypeProvider.doSwitch(this.returnType()) as VrapObjectType
        return "${commandReturnType.`package`}.${commandReturnType.simpleClassName}"
    }
}
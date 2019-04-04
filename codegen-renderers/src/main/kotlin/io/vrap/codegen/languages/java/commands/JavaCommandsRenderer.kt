package io.vrap.codegen.languages.java.commands

import io.vrap.codegen.languages.java.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.ObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.resource
import io.vrap.codegen.languages.java.extensions.toRequestName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import org.eclipse.emf.ecore.EObject
import javax.inject.Inject

class JavaCommandsRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, ObjectTypeExtensions, EObjectTypeExtensions {

    override fun render(type: Method): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType
        
        val content = """
                |package ${vrapType.`package`};
                |
                |import com.commercetools.importer.commands.SphereRequest;
                |import io.sphere.sdk.http.HttpResponse;
                |import io.sphere.sdk.client.HttpRequestIntent;
                |import io.sphere.sdk.http.HttpMethod;
                |import io.sphere.sdk.json.SphereJsonUtils;
                |
                |final class ${type.toRequestName()} implements SphereRequest {
                |   
                |   <${type.fields()}>
                |   
                |   <${type.constructor()}>
                |   
                |   <${type.ofMethod()}>
                |   
                |   @Override
                |   public Object deserialize(HttpResponse httpResponse) {
                |      return null;
                |   }
                |
                |   @Override
                |   public HttpRequestIntent httpRequestIntent() {
                |      ${type.httpRequestIntentBody()}
                |   }
                |
                |   @Override
                |   public boolean canDeserialize(HttpResponse httpResponse) {
                |      return false;
                |   }
                |}
                |
                """.trimMargin().keepIndentation()

        return TemplateFile(
            relativePath = "${vrapType.`package`}.${type.toRequestName()}".replace(".", "/") + ".java",
            content = content
        )
    }

    private fun Method.httpRequestIntentBody() : String? {
        val path = this.resource().fullUri.template
        val parts = path.split("/").filter { it.isNotEmpty() }
        val pathArguments = parts.filter { it.startsWith("{") && it.endsWith("}")}
        var stringFormat = path
        
        pathArguments.forEach { stringFormat = stringFormat.replace(it, "%s") }
        
        val stringFormatArgs = pathArguments
                .map { it.replace("{", "").replace("}", "") }
                .map { "this.$it" }
                .joinToString(separator = ",")

        val httpRequestIntentArguments = mutableListOf<String>()
        httpRequestIntentArguments.add("HttpMethod.${this.method.name}")
        
        if(stringFormat.isNotEmpty() && stringFormatArgs.isNotEmpty()){
            httpRequestIntentArguments.add("String.format(\"$stringFormat\", $stringFormatArgs)")
        }
        
        if(this.bodyObjectName() != null){
            httpRequestIntentArguments.add("SphereJsonUtils.toJsonString(${this.bodyObjectName()})")
        }
        
        return "return HttpRequestIntent.of(${httpRequestIntentArguments.joinToString(separator = ", ")});"
    }
    
    private fun Method.ofMethod(): String? {
        val ofMethodArguments = mutableListOf<String>()
        val constructorArguments = mutableListOf<String>()
        
        this.pathArguments().map { "final String $it" }.forEach { ofMethodArguments.add(it) }
        constructorArguments.addAll(this.pathArguments())
        
        if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            val methodBodyArgument = "final ${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
            ofMethodArguments.add(methodBodyArgument)
            constructorArguments.add(methodBodyVrapType.simpleClassName.decapitalize())
        }
        
        return """
            |public static ${this.toRequestName()} of(${ofMethodArguments.joinToString(separator = ", ")}) {
            |   <return new ${this.toRequestName()}(${constructorArguments.joinToString(separator = ", ")});>
            |}
        """.trimMargin().keepIndentation()
    }
    
    private fun Method.constructor(): String? {
        val constructorArguments = mutableListOf<String>()
        val constructorAssignments = mutableListOf<String>()
        
        this.pathArguments().map { "final String $it" }.forEach { constructorArguments.add(it) }
        this.pathArguments().map { "this.$it = $it;" }.forEach { constructorAssignments.add(it) }
        
        if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            val methodBodyArgument = "final ${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
            constructorArguments.add(methodBodyArgument)
            val methodBodyAssignment = "this.${methodBodyVrapType.simpleClassName.decapitalize()} = ${methodBodyVrapType.simpleClassName.decapitalize()};"
            constructorAssignments.add(methodBodyAssignment)
        }
        
        return """
            |private ${this.toRequestName()}(${constructorArguments.joinToString(separator = ", ")}){
            |   <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation()
    }
    
    
    
    private fun Method.fields(): String? {
        val commandClassFields = mutableListOf<String>()
        this.pathArguments().map { "private final String $it;" }.forEach { commandClassFields.add(it) }
        if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            commandClassFields.add("private final ${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()};")
        }
        
        return commandClassFields.joinToString(separator = "\n")
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
}

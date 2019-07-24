package io.vrap.codegen.languages.javalang.client.builder.requests

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import org.eclipse.emf.ecore.EObject

class JavaHttpRequestRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, JavaObjectTypeExtensions, EObjectTypeExtensions {
    
    override fun render(type: Method): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType
        
        val content = """
            |package ${vrapType.`package`};
            |
            |import client.HttpRequest;
            |import client.HttpMethod;
            |import client.HttpHeaders;
            |import client.HttpResponse;
            |import json.CommercetoolsJsonUtils;
            |import client.CommercetoolsClient;
            |
            |import java.util.ArrayList;
            |import java.util.List;
            |
            |public class ${type.toRequestName()} {
            |   
            |   <${type.fields()}>
            |   
            |   <${type.constructor()}>
            |   
            |   <${type.createRequestMethod()}>
            |   
            |   <${type.executeBlockingMethod()}>
            |   
            |   <${type.pathArgumentsGetters()}>
            |   
            |   <${type.queryParamsGetters()}>
            |   
            |   <${type.pathArgumentsSetters()}>
            |   
            |   <${type.queryParamsSetters()}>
            |   
            |   <${type.helperMethods()}>
            |
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
        
        return """
            |public ${this.toRequestName()}(${constructorArguments.joinToString(separator = ", ")}){
            |   <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation()
    }
    
    private fun Method.fields(): String? {
        val commandClassFields = mutableListOf<String>()
        commandClassFields.add("private HttpHeaders headers = new HttpHeaders();")
        commandClassFields.add("private List<String> additionalQueryParams = new ArrayList<>();")
        val parameterFields : String = 
                this.queryParameters.map { "private List<${it.type.toVrapType().simpleName()}> ${it.name} = new ArrayList<>();" }
                        .joinToString(separator = "\n\n")
        commandClassFields.add(parameterFields)
        
        this.pathArguments().map { "private String $it;" }.forEach { commandClassFields.add(it) }
        if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            commandClassFields.add("private ${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()};")
        }

        return commandClassFields.map { it.escapeAll() }.joinToString(separator = "\n\n")
    }

    private fun Method.pathArguments() : List<String> {
        return this.resource().fullUri.variables.toList()
    }
    
    private fun Method.createRequestMethod() : String {
        
        val pathArguments = this.pathArguments().map { "{$it}" }
        var stringFormat = this.resource().fullUri.template
        pathArguments.forEach { stringFormat = stringFormat.replace(it, "%s") }
        val stringFormatArgs = pathArguments
                .map { it.replace("{", "").replace("}", "") }
                .map { "this.$it" }
                .joinToString(separator = ", ")

        val bodyName : String? = if(this.bodies != null && this.bodies.isNotEmpty()){
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            methodBodyVrapType.simpleClassName.decapitalize()
        }else {
            null
        }
        
        return """
            |public HttpRequest createHttpRequest() {
            |   HttpRequest httpRequest = new HttpRequest();
            |   httpRequest.setPath(String.format("$stringFormat", $stringFormatArgs)); 
            |   httpRequest.setMethod(HttpMethod.${this.method.name});
            |   httpRequest.setHeaders(headers);
            |   ${if(bodyName != null) "try{httpRequest.setBody(CommercetoolsJsonUtils.toJsonString($bodyName));}catch(Exception e){e.printStackTrace();}" else "" }
            |   return httpRequest;
            |}
        """.trimMargin().keepIndentation()
    }   
    
    private fun Method.executeBlockingMethod() : String {
        
        val responseErrorsDeserialization : String = 
                this.responses
                        .filter { !it.statusCode.startsWith("2") }
                        .map { responseErrorsDeserialization(it.statusCode, it.bodies[0].type.toVrapType()) }
                        .joinToString(separator = "\n\n")
        
        return """
            |public ${this.javaReturnType(vrapTypeProvider)} executeBlocking() {
            |   HttpResponse response = CommercetoolsClient.getClient().execute(this.createHttpRequest());
            |
            |   $responseErrorsDeserialization
            |   
            |   try{
            |       return CommercetoolsJsonUtils.fromJsonString(response.getBody(), ${this.javaReturnType(vrapTypeProvider)}.class);
            |   }catch(Exception e){
            |       e.printStackTrace();
            |   }
            |   return null;
            |}
        """.trimMargin().keepIndentation()
    }

    private fun responseErrorsDeserialization(statusCode : String, bodyType: VrapType) : String {
        return """
            |if(response.getStatusCode() == $statusCode){
            |   try{
            |       ${bodyType.fullClassName()} ${bodyType.simpleName().lowerCamelCase()} = CommercetoolsJsonUtils.fromJsonString(response.getBody(), ${bodyType.fullClassName()}.class);
            |       throw new RuntimeException(${bodyType.simpleName().lowerCamelCase()}.getMessage());
            |   }catch(Exception e){
            |       e.printStackTrace();
            |       throw new RuntimeException(e.getMessage());
            |   }
            |}
        """.trimMargin().keepIndentation()
    }
    
    private fun Method.pathArgumentsGetters() : String = this.pathArguments()
            .map { "public String get${it.capitalize()}() {return this.$it;}" }
            .joinToString(separator = "\n")
    
    private fun Method.pathArgumentsSetters() : String = this.pathArguments()
            .map { "public void set${it.capitalize()}(final String $it) {this.$it = $it;}" }
            .joinToString(separator = "\n\n")
    
    private fun Method.queryParamsGetters() : String = this.queryParameters
            .map { """
                |public List<${it.type.toVrapType().simpleName()}> get${it.name.capitalize()}() {
                |   return this.${it.name};
                |}
                """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")
    
    private fun Method.queryParamsSetters() : String = this.queryParameters
            .map { """
                |public void add${it.name.capitalize()}(final ${it.type.toVrapType().simpleName()} ${it.name}){
                |   this.${it.name}.add(${it.name});
                |}
                |
                |public void with${it.name.capitalize()}(final List<${it.type.toVrapType().simpleName()}> ${it.name}){
                |   this.${it.name} = this.${it.name};
                |}
            """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")
    
    private fun Method.helperMethods() : String {
        return """
            |public ${this.toRequestName()} addHeader(final String key, final String value) {
            |   this.headers.addHeader(key, value);
            |   return this;
            |}
            |
            |public ${this.toRequestName()} withHeaders(final HttpHeaders headers) {
            |   this.headers = headers;
            |   return this;
            |}
            |
            |public String getHeaderValue(final String key) {
            |   return this.headers.getHeaderValue(key);
            |}
            |
            |public HttpHeaders getHeaders() {
            |   return this.headers;
            |}
            |
            |public ${this.toRequestName()} addAdditionalQueryParam(final String additionalQueryParam) {
            |   this.additionalQueryParams.add(additionalQueryParam);
            |   return this;
            |}
            |
            |public ${this.toRequestName()} setAdditionalQueryParams(final List<String> additionalQueryParams) {
            |   this.additionalQueryParams = additionalQueryParams;
            |   return this;
            |}
            |
            |public List<String> getAdditionalQueryParams() {
            |   return this.additionalQueryParams;
            |}
        """.trimMargin().escapeAll()
    }
}
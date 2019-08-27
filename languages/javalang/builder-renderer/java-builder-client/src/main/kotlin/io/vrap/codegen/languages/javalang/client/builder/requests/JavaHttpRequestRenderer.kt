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
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject

const val PLACEHOLDER_PARAM_ANNOTATION = "placeholderParam"

class JavaHttpRequestRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, JavaObjectTypeExtensions, EObjectTypeExtensions {
    
    override fun render(type: Method): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType
        
        val content = """
            |package ${vrapType.`package`};
            |
            |import client.ApiHttpRequest;
            |import client.ApiHttpMethod;
            |import client.ApiHttpHeaders;
            |import client.ApiHttpResponse;
            |import json.CommercetoolsJsonUtils;
            |import client.CommercetoolsClient;
            |
            |import java.io.IOException;
            |
            |import java.util.ArrayList;
            |import java.util.List;
            |import java.util.Map;
            |import java.util.HashMap;
            |import java.util.stream.Collectors;
            |
            |import java.io.UnsupportedEncodingException;
            |import java.net.URLEncoder;
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
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                val methodBodyArgument = "${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
                constructorArguments.add(methodBodyArgument)
                val methodBodyAssignment = "this.${methodBodyVrapType.simpleClassName.decapitalize()} = ${methodBodyVrapType.simpleClassName.decapitalize()};"
                constructorAssignments.add(methodBodyAssignment)
            }else {
                constructorArguments.add("com.fasterxml.jackson.databind.JsonNode jsonNode")
                constructorAssignments.add("this.jsonNode = jsonNode;")
            }
        }
        
        return """
            |public ${this.toRequestName()}(${constructorArguments.joinToString(separator = ", ")}){
            |   <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation()
    }
    
    private fun Method.fields(): String? {
        val commandClassFields = mutableListOf<String>()
        commandClassFields.add("private ApiHttpHeaders headers = new ApiHttpHeaders();")
        commandClassFields.add("private Map<String, String> additionalQueryParams = new HashMap<>();")
        val parameterFields : String =
                this.queryParameters
                        .filter { it.annotations.find { it.type.name.equals(PLACEHOLDER_PARAM_ANNOTATION) } == null}
                        .map { "private List<${it.type.toVrapType().simpleName()}> ${it.fieldName()} = new ArrayList<>();" }
                        .joinToString(separator = "\n\n")
        commandClassFields.add(parameterFields)

        this.pathArguments().map { "private String $it;" }.forEach { commandClassFields.add(it) }
        if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType){
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                commandClassFields.add("private ${methodBodyVrapType.`package`}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()};")
            }else {
                commandClassFields.add("private com.fasterxml.jackson.databind.JsonNode jsonNode;");
            }
        }

        return commandClassFields.map { it.escapeAll() }.joinToString(separator = "\n\n")
    }

    private fun QueryParameter.fieldName() : String {
        return StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
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
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                methodBodyVrapType.simpleClassName.decapitalize()
            }else {
                "jsonNode"
            }
        }else {
            null
        }
        
        val addingQueryParams : String = this.queryParameters
                .filter { it.annotations.find { it.type.name.equals("placeholderParam") } == null}
                .map { "params.add(this.${it.fieldName()}.stream().map(s -> \"${it.name}=\" + ${if(it.type.name.equals("string")) "urlEncode(s)" else "s"}).collect(Collectors.joining(\"&\")));" }
                .joinToString(separator = "\n")
        
        val addingAdditionalQueryParams : String = "params.add(additionalQueryParams.entrySet().stream().map(entry -> entry.getKey() + \"=\" + entry.getValue()).collect(Collectors.joining(\"&\")));"
        
        val requestPathGeneration : String = """
            |List<String> params = new ArrayList<>();
            |$addingQueryParams
            |$addingAdditionalQueryParams
            |params.removeIf(String::isEmpty);
            |String httpRequestPath = String.format("$stringFormat", $stringFormatArgs);
            |if(!params.isEmpty()){
            |   httpRequestPath += "?" + String.join("&", params);
            |}
        """.trimMargin().escapeAll()
        
        return """
            |public ApiHttpRequest createHttpRequest() {
            |   ApiHttpRequest httpRequest = new ApiHttpRequest();
            |   $requestPathGeneration
            |   httpRequest.setPath(httpRequestPath); 
            |   httpRequest.setMethod(ApiHttpMethod.${this.method.name});
            |   httpRequest.setHeaders(headers);
            |   ${if(bodyName != null) "try{httpRequest.setBody(CommercetoolsJsonUtils.toJsonString($bodyName));}catch(Exception e){e.printStackTrace();}" else "" }
            |   return httpRequest;
            |}
        """.trimMargin()
    }   
    
    private fun Method.executeBlockingMethod() : String {
        val responseErrorsDeserialization : String = 
                this.responses
                        .filter { !it.statusCode.startsWith("2") }
                        .map { responseErrorsDeserialization(it.statusCode, if(it.bodies.isNotEmpty())it.bodies[0].type.toVrapType() else null) }
                        .joinToString(separator = "\n\n")
        
        return """
            |public ${this.javaReturnType(vrapTypeProvider)} executeBlocking() {
            |   ApiHttpResponse response;
            |   try{
            |       response = CommercetoolsClient.getClient().execute(this.createHttpRequest());
            |   }catch(IOException e){
            |       throw new RuntimeException(e.getMessage());
            |   }
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

    private fun responseErrorsDeserialization(statusCode : String, bodyType: VrapType?) : String {
        return if(bodyType == null){
            """
                |if(response.getStatusCode() == $statusCode){
                |   throw new RuntimeException("Response status code : " + $statusCode);
                |}
            """.trimMargin().keepIndentation()
        }else{
            """
            |if(response.getStatusCode() == $statusCode){
            |   try{
            |       ${bodyType?.fullClassName()} ${bodyType?.simpleName()?.lowerCamelCase()} = CommercetoolsJsonUtils.fromJsonString(response.getBody(), ${bodyType?.fullClassName()}.class);
            |       throw new RuntimeException(${bodyType?.simpleName()?.lowerCamelCase()}.getMessage());
            |   }catch(Exception e){
            |       e.printStackTrace();
            |       throw new RuntimeException(e.getMessage());
            |   }
            |}
        """.trimMargin().keepIndentation()
        }
    }
    
    private fun Method.pathArgumentsGetters() : String = this.pathArguments()
            .map { "public String get${it.capitalize()}() {return this.$it;}" }
            .joinToString(separator = "\n")
    
    private fun Method.pathArgumentsSetters() : String = this.pathArguments()
            .map { "public void set${it.capitalize()}(final String $it) {this.$it = $it;}" }
            .joinToString(separator = "\n\n")
    
    private fun Method.queryParamsGetters() : String = this.queryParameters
            .filter { it.annotations.find { it.type.name.equals("placeholderParam") } == null}
            .map { """
                |public List<${it.type.toVrapType().simpleName()}> get${it.fieldName().capitalize()}() {
                |   return this.${it.fieldName()};
                |}
                """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")
    
    private fun Method.queryParamsSetters() : String = this.queryParameters
            .filter { it.annotations.find { it.type.name.equals("placeholderParam") } == null}
            .map { """
                |public ${this.toRequestName()} add${it.fieldName().capitalize()}(final ${it.type.toVrapType().simpleName()} ${it.fieldName()}){
                |   this.${it.fieldName()}.add(${it.fieldName()});
                |   return this;
                |}
                |
                |public ${this.toRequestName()} with${it.fieldName().capitalize()}(final List<${it.type.toVrapType().simpleName()}> ${it.fieldName()}){
                |   this.${it.fieldName()} = ${it.fieldName()};
                |   return this;
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
            |public ${this.toRequestName()} withHeaders(final ApiHttpHeaders headers) {
            |   this.headers = headers;
            |   return this;
            |}
            |
            |public String getHeaderValue(final String key) {
            |   return this.headers.getHeaderValue(key);
            |}
            |
            |public ApiHttpHeaders getHeaders() {
            |   return this.headers;
            |}
            |
            |public ${this.toRequestName()} addAdditionalQueryParam(final String additionalQueryParamKey, final String additionalQueryParamValue) {
            |   this.additionalQueryParams.put(additionalQueryParamKey, additionalQueryParamValue);
            |   return this;
            |}
            |
            |public ${this.toRequestName()} setAdditionalQueryParams(final Map<String, String> additionalQueryParams) {
            |   this.additionalQueryParams = additionalQueryParams;
            |   return this;
            |}
            |
            |public Map<String, String> getAdditionalQueryParams() {
            |   return this.additionalQueryParams;
            |}
            |
            |private String urlEncode(final String s){
            |   try{
            |        return URLEncoder.encode(s, "UTF-8");
            |    }catch (UnsupportedEncodingException e) {
            |        e.printStackTrace();
            |        return null;
            |    }
            |}
        """.trimMargin().escapeAll()
    }
}
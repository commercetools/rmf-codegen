package io.vrap.codegen.languages.javalang.client.builder.requests

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.java.base.extensions.javaReturnType
import io.vrap.codegen.languages.php.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.php.extensions.ObjectTypeExtensions
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
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
            |import client.HttpHeaders;
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
            |   <${type.getters()}>
            |   
            |   <${type.setters()}>
            |   
            |   <${type.helperMethods()}>
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
        
        val httpBodyAssignment : String = if(this.bodyObjectName() != null) { "setBody(${this.bodyObjectName()}.toJson());" } else { "" }
        
        return """
            |public ${this.toRequestName()}(${constructorArguments.joinToString(separator = ", ")}){
            |   <${constructorAssignments.joinToString(separator = "\n")}>
            |   $httpBodyAssignment
            |}
        """.trimMargin().keepIndentation()
    }
    
    private fun Method.fields(): String? {
        val commandClassFields = mutableListOf<String>()
        commandClassFields.add("private HttpHeaders headers = new HttpHeaders();")
        commandClassFields.add("private List<String> expansionPath = new ArrayList<>();")
        commandClassFields.add("private List<String> predicate = new ArrayList<>();")
        commandClassFields.add("private List<String> sort = new ArrayList<>();")
        commandClassFields.add("private List<String> additionalQueryParams = new ArrayList<>();")
        commandClassFields.add("private Long offset;")
        commandClassFields.add("private Long limit;")
        
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

    private fun Method.bodyObjectName() : String? {
        if(this.bodies != null && this.bodies.isNotEmpty()) {
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            return methodBodyVrapType.simpleClassName.decapitalize()
        }else{
            return null
        }
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
            |   ${if(bodyName != null) "httpRequest.setBody($bodyName)" else "" }
            |   return httpRequest;
            |}
        """.trimMargin().keepIndentation()
    }   
    
    private fun Method.executeBlockingMethod() : String {
        return """
            |public ${this.javaReturnType(vrapTypeProvider)} executeBlocking() {
            |   try{
            |       return CommercetoolsJsonUtils.fromJsonString(CommercetoolsClient.getClient().execute(this.createHttpRequest()).getBody(), ${this.javaReturnType(vrapTypeProvider)}.class);
            |   }catch(Exception e){
            |       e.printStackTrace();
            |   }
            |   return null;
            |}
        """.trimMargin().keepIndentation()
    }

    private fun Method.getters() = this.pathArguments()
            .map { "public String get${it.capitalize()}() {return this.$it;}" }
            .joinToString(separator = "\n")
    
    private fun Method.setters() = this.pathArguments()
            .map { "public void set${it.capitalize()}(final String $it) {this.$it = $it;}" }
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
            |public ${this.toRequestName()} addExpansionPath(final String expansionPath) {
            |   this.expansionPath.add(expansionPath);
            |   return this;
            |}
            |
            |public ${this.toRequestName()} withExpansionPath(final List<String> expansionPath) {
            |   this.expansionPath = expansionPath;
            |   return this;
            |}
            |
            |public List<String> getExpansionPath() {
            |   return this.expansionPath;
            |}
            |
            |public ${this.toRequestName()} addPredicate(final String predicate) {
            |   this.predicate.add(predicate);
            |   return this;
            |}
            |
            |public ${this.toRequestName()} withPredicate(List<String> predicate) {
            |   this.predicate = predicate;
            |   return this;
            |}
            |
            |public List<String> getPredicate() {
            |   return this.predicate;
            |}
            |
            |public ${this.toRequestName()} addSort(final String sort) {
            |   this.sort.add(sort);
            |   return this;
            |}
            |
            |public ${this.toRequestName()} withSort(final List<String> sort) {
            |   this.sort = sort;
            |   return this;
            |}
            |
            |public List<String> getSort() {
            |   return this.sort;
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
            |
            |public ${this.toRequestName()} withOffset(final Long offset){
            |   this.offset = offset;
            |   return this;
            |}
            |
            |public ${this.toRequestName()} withLimit(final Long limit){
            |   this.limit = limit;
            |   return this;
            |}
        """.trimMargin().escapeAll()
    }
}
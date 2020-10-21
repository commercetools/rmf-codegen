package io.vrap.codegen.languages.javalang.client.builder.requests

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject

/**
 * Query parameters with this annotation should be ignored by JVM sdk.
 */
const val PLACEHOLDER_PARAM_ANNOTATION = "placeholderParam"

class JavaHttpRequestRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, JavaObjectTypeExtensions, JavaEObjectTypeExtensions {

    override fun render(type: Method): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val content = """
            |package ${vrapType.`package`.toJavaPackage()};
            |
            |import io.vrap.rmf.base.client.utils.Utils;
            |import io.vrap.rmf.base.client.utils.json.VrapJsonUtils;
            |
            |import java.io.InputStream;
            |import java.io.IOException;
            |
            |import java.nio.file.Files;
            |
            |import java.time.Duration;
            |import java.util.ArrayList;
            |import java.util.List;
            |import java.util.Map;
            |import java.util.HashMap;
            |import java.util.stream.Collectors;
            |import java.util.concurrent.CompletableFuture;
            |import io.vrap.rmf.base.client.utils.Generated;
            |
            |import java.io.UnsupportedEncodingException;
            |import java.net.URLEncoder;
            |import io.vrap.rmf.base.client.*;
            |${type.imports()}
            |
            |import static io.vrap.rmf.base.client.utils.ClientUtils.blockingWait;
            |
            |<${type.toComment().escapeAll()}>
            |<${JavaSubTemplates.generatedAnnotation}>
            |public class ${type.toRequestName()} extends ApiMethod\<${type.toRequestName()}\> {
            |
            |    <${type.fields()}>
            |
            |    <${type.constructor()}>
            |
            |    <${type.copyConstructor()}>
            |
            |    <${type.createRequestMethod()}>
            |
            |    <${type.executeBlockingMethod()}>
            |
            |    <${type.executeMethod()}>
            |
            |    <${type.pathArgumentsGetters()}>
            |
            |    <${type.queryParamsGetters()}>
            |
            |    <${type.pathArgumentsSetters()}>
            |
            |    <${type.queryParamsSetters()}>
            |}
        """.trimMargin()
                .keepIndentation()

        return TemplateFile(
                relativePath = "${vrapType.`package`}.${type.toRequestName()}".replace(".", "/") + ".java",
                content = content
        )
    }

    private fun Method.constructor(): String? {
        val constructorArguments = mutableListOf("final ApiHttpClient apiHttpClient")
        val constructorAssignments = mutableListOf("super(apiHttpClient);")

        this.pathArguments().map { "String $it" }.forEach { constructorArguments.add(it) }
        this.pathArguments().map { "this.$it = $it;" }.forEach { constructorAssignments.add(it) }

        if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                val methodBodyArgument = "${methodBodyVrapType.`package`.toJavaPackage()}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
                constructorArguments.add(methodBodyArgument)
                val methodBodyAssignment = "this.${methodBodyVrapType.simpleClassName.decapitalize()} = ${methodBodyVrapType.simpleClassName.decapitalize()};"
                constructorAssignments.add(methodBodyAssignment)
            }else {
                constructorArguments.add("com.fasterxml.jackson.databind.JsonNode jsonNode")
                constructorAssignments.add("this.jsonNode = jsonNode;")
            }
        }

        return """
            |public ${this.toRequestName()}(${constructorArguments.joinToString(separator = ", ")}) {
            |    <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation()
    }

    private fun Method.copyConstructor(): String? {
        val constructorAssignments = mutableListOf("super(t);")

        this.pathArguments().map { "this.$it = t.$it;" }.forEach { constructorAssignments.add(it) }

        if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                val methodBodyAssignment = "this.${methodBodyVrapType.simpleClassName.decapitalize()} = t.${methodBodyVrapType.simpleClassName.decapitalize()};"
                constructorAssignments.add(methodBodyAssignment)
            } else {
                constructorAssignments.add("this.jsonNode = t.jsonNode;")
            }
        }

        return """
            |public ${this.toRequestName()}(${this.toRequestName()} t) {
            |    <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation()
    }

    private fun Method.fields(): String? {

        val pathArgs = this.pathArguments().map { "private String $it;" }.joinToString(separator = "\n")

        val body: String = if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType){
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                "private ${methodBodyVrapType.`package`.toJavaPackage()}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()};"
            }else {
                "private com.fasterxml.jackson.databind.JsonNode jsonNode;"
            }
        }else{
            ""
        }

        return """|
            |<$pathArgs>
            |
            |<$body>
        """.trimMargin()
    }

    private fun QueryParameter.fieldName(): String {
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

        val requestPathGeneration : String = """
            |List<String> params = new ArrayList<>(getQueryParamUriStrings());
            |String httpRequestPath = String.format("$stringFormat", $stringFormatArgs);
            |if(!params.isEmpty()){
            |    httpRequestPath += "?" + String.join("&", params);
            |}
        """.trimMargin().escapeAll()
        val bodySetter: String = if(bodyName != null){
            if(this.bodies[0].type.isFile())
                "try{httpRequest.setBody(Files.readAllBytes(file.toPath()));}catch(Exception e){e.printStackTrace();}"
            else "try{httpRequest.setBody(VrapJsonUtils.toJsonByteArray($bodyName));}catch(Exception e){e.printStackTrace();}"
        } else ""

        return """
            |public ApiHttpRequest createHttpRequest() {
            |    ApiHttpRequest httpRequest = new ApiHttpRequest();
            |    <$requestPathGeneration>
            |    httpRequest.setUri(httpRequestPath); 
            |    httpRequest.setMethod(ApiHttpMethod.${this.method.name});
            |    httpRequest.setHeaders(getHeaders());
            |    $bodySetter
            |    return httpRequest;
            |}
        """.trimMargin()
    }

    private fun Method.executeBlockingMethod() : String {
        return """
            |public ApiHttpResponse\<${this.javaReturnType(vrapTypeProvider)}\> executeBlocking(){
            |    return executeBlocking(Duration.ofSeconds(60));
            |}
            |
            |public ApiHttpResponse\<${this.javaReturnType(vrapTypeProvider)}\> executeBlocking(Duration timeout){
            |    return blockingWait(execute(), timeout);
            |}
        """.trimMargin()
    }

    private fun Method.executeMethod() : String {
        return """
            |public CompletableFuture\<ApiHttpResponse\<${this.javaReturnType(vrapTypeProvider)}\>\> execute(){
            |    return apiHttpClient().execute(this.createHttpRequest())
            |            .thenApply(response -\> Utils.convertResponse(response,${this.javaReturnType(vrapTypeProvider)}.class));
            |}
        """.trimMargin()
    }

    private fun Method.pathArgumentsGetters() : String = this.pathArguments()
            .map { "public String get${it.capitalize()}() {return this.$it;}" }
            .joinToString(separator = "\n")

    private fun Method.pathArgumentsSetters() : String = this.pathArguments()
            .map { "public void set${it.capitalize()}(final String $it) { this.$it = $it; }" }
            .joinToString(separator = "\n\n")

    private fun Method.queryParamsGetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |public List<String> get${it.fieldName().capitalize()}() {
                |    return this.getQueryParam("${it.fieldName()}");
                |}
                """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun Method.queryParamsSetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |public ${this.toRequestName()} with${it.fieldName().capitalize()}(final ${it.type.toVrapType().simpleName()} ${it.fieldName()}){
                |    return new ${this.toRequestName()}(this).addQueryParam("${it.fieldName()}", ${it.fieldName()});
                |}
            """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun Method.imports(): String {
        return this.queryParameters
                .map {
                    it.type.toVrapType()
                }
                .filter { it !is VrapScalarType }
                .map {
                    getImportsForType(it)
                }
                .filter { !it.isNullOrBlank() }
                .map { "import ${it};" }
                .joinToString(separator = "\n")

    }
}

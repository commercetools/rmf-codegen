package io.vrap.codegen.kt.languages.java.client

import com.google.inject.Inject
import io.vrap.codegen.kt.languages.java.JavaSubTemplates
import io.vrap.codegen.kt.languages.java.extensions.EObjectTypeExtensions
import io.vrap.codegen.kt.languages.java.extensions.fullClassName
import io.vrap.codegen.kt.languages.java.extensions.simpleName
import io.vrap.codegen.kt.languages.java.extensions.toComment
import io.vrap.rmf.codegen.common.generator.core.ResourceCollection
import io.vrap.rmf.codegen.kt.io.TemplateFile
import io.vrap.rmf.codegen.kt.rendring.ResourceCollectionRenderer
import io.vrap.rmf.codegen.kt.rendring.utils.escapeAll
import io.vrap.rmf.codegen.kt.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.kt.types.PackageProvider
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.Response
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.impl.TypesFactoryImpl

class SpringClientRenderer @Inject constructor(val packageProvider: PackageProvider, override val vrapTypeProvider: VrapTypeProvider) : ResourceCollectionRenderer, EObjectTypeExtensions {


    override fun render(type: ResourceCollection): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type.sample) as VrapObjectType

        val content = """
            |package ${vrapType.`package`};
            |
            |import java.util.HashMap;
            |import java.util.List;
            |import java.util.Map;
            |import java.net.ConnectException;
            |import javax.annotation.Generated;
            |
            |import org.springframework.beans.factory.annotation.Value;
            |import org.springframework.core.ParameterizedTypeReference;
            |import org.springframework.http.HttpEntity;
            |import org.springframework.http.HttpHeaders;
            |import org.springframework.http.HttpMethod;
            |import org.springframework.http.MediaType;
            |import org.springframework.retry.annotation.Backoff;
            |import org.springframework.retry.annotation.Retryable;
            |import org.springframework.stereotype.Component;
            |import org.springframework.web.client.RestTemplate;
            |
            |${type.sample.toComment().escapeAll()}
            |${JavaSubTemplates.generatedAnnotation.escapeAll()}
            |@Component
            |public class ${vrapType.simpleClassName} {
            |
            |    private final String baseUri;
            |
            |    private final RestTemplate restTemplate;
            |
            |    public ${vrapType.simpleClassName}(@Value("${'$'}{sdk.baseUri}") final String baseUri, final RestTemplate restTemplate) {
            |        this.baseUri = baseUri;
            |        this.restTemplate = restTemplate;
            |    }
            |
            |    <${type.methods()}>
            |
            |}
        """.trimMargin().keepIndentation()
        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".java",
                content = content
        )
    }


    fun ResourceCollection.methods(): String {
        return this.resources
                .flatMap { resource -> resource.methods.map { javaBody(resource, it) } }
                .joinToString(separator = "\n\n")
    }


    fun javaBody(resource: Resource, method: Method): String {
        val methodReturnType = vrapTypeProvider.doSwitch(method.retyurnType())
        val body = """
            |${method.toComment().escapeAll()}
            |@Retryable(
            |          value = { ConnectException.class },
            |          maxAttemptsExpression = "#{${'$'}{retry.${method.method.name.toLowerCase()}.maxAttempts}}",
            |          backoff = @Backoff(delayExpression = "#{1}", maxDelayExpression = "#{5}", multiplierExpression = "#{2}"))
            |public ${methodReturnType.fullClassName().escapeAll()} ${method.method.name.toLowerCase()}(${methodParameters(resource, method)}) {
            |
            |    final Map\<String, Object\> parameters = new HashMap\<\>();
            |
            |    <${resource.allUriParameters.map { "parameters.put(\"${it.name}\",${it.name});" }.joinToString(separator = "\n")}>
            |    <${method.queryParameters.map { "parameters.put(\"${it.name}\",${it.name});" }.joinToString(separator = "\n")}>
            |
            |    <${method.mediaType().escapeAll()}>
            |
            |    final ParameterizedTypeReference\<${method.retyurnType().toVrapType().fullClassName().escapeAll()}\> type = new ParameterizedTypeReference\<${method.retyurnType().toVrapType().fullClassName().escapeAll()}\>() {};
            |    final String fullUri = baseUri + "${relativeUrl(resource,method)}";
            |
            |    return restTemplate.exchange(fullUri, HttpMethod.${method.method.name.toUpperCase()}, entity, type, parameters).getBody();
            |
            |}
                """.trimMargin()
        return body
    }

    fun relativeUrl(resource: Resource, method: Method) : String{
        val urlBuilder = StringBuilder(resource.fullUri.template)
        method.queryParameters
                .map { it.name }
                .map { "$it={$it}" }
                .joinToString( separator = "&")
                .let {
                    if(!it.isNullOrEmpty()){
                        urlBuilder.append("?$it")
                    }
                }

        return urlBuilder.toString()
    }



    fun methodParameters(resource: Resource, method: Method): String {

        val parameters : MutableList<String> = resource.allUriParameters
                .map { "final ${it.type.toVrapType().fullClassName()} ${it.name}" }
                .toMutableList()

        val queryParameters : MutableList<String>  = method.queryParameters
                .map { "final ${it.type.toVrapType().fullClassName()} ${it.name}" }
                .toMutableList()

        val paramsList = mutableListOf<String>()
        paramsList.addAll(parameters)
        paramsList.addAll(queryParameters)

        if (method.bodies?.isNotEmpty() ?: false) {
            paramsList.add("${method.bodies[0].type.toVrapType().fullClassName()} body")
        }
        return paramsList.joinToString(separator = ", ")
    }

    fun Method.mediaType(): String {
        if (this.bodies?.isNotEmpty() ?: false) {
            val result = """
                |final HttpHeaders headers = new HttpHeaders();
                |headers.setContentType(MediaType.parseMediaType(("${this.bodies[0].contentType}")));
                |final HttpEntity<${this.bodies[0].type.toVrapType().fullClassName()}> entity = new HttpEntity<>(body, headers);
        """.trimMargin()
            return result

        }
        return "final HttpEntity<?> entity = null;"

    }

    fun Method.retyurnType(): AnyType {
        return this.responses
                .filter { it.isSuccessfull() }
                .filter { it.bodies?.isNotEmpty() ?: false }
                .firstOrNull()
                ?.let { it.bodies[0].type }
                ?: TypesFactoryImpl.eINSTANCE.createNilType()
    }


    fun Response.isSuccessfull(): Boolean = this.statusCode.toInt() in (200..299)

}
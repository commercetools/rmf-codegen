package io.vrap.codegen.languages.php.model

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.java.extensions.isSuccessfull
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.responses.Response
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.FileType
import io.vrap.rmf.raml.model.types.impl.TypesFactoryImpl
import org.eclipse.emf.ecore.EObject

class PhpMethodBuilderRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ResourceRenderer, EObjectTypeExtensions {

    @Inject
    @Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME)
    lateinit var packagePrefix:String

    private val resourcePackage = "Resource";

    override fun render(type: Resource): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.`package`.toNamespaceName().escapeAll()}\\$resourcePackage;
            |
            |use ${vrapType.`package`.toNamespaceName().escapeAll()}\\ApiResource;
            |use Psr\\Http\\Message\\UploadedFileInterface;
            |
            |/** @psalm-suppress PropertyNotSetInConstructor */
            |class ${type.resourceBuilderName()} extends ApiResource
            |{
            |   <<${type.subResources()}>>
            |   <<${type.methods()}>>
            |}
        """.trimMargin().keepIndentation("<<", ">>").forcedLiteralEscape()
        val relativeTypeNamespace = vrapType.`package`.toNamespaceName().replace(packagePrefix.toNamespaceName() + "\\", "").replace("\\", "/") + "/$resourcePackage"
        val relativePath = "src/" + relativeTypeNamespace + "/" + type.resourceBuilderName() + ".php"
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun Resource.methods(): String {
        return this.methods.map {
            """
                |/**
                | * @psalm-param ${if (it.firstBody()?.type is FileType) "?UploadedFileInterface " else "?object"} $!body
                | * @psalm-param array<string, scalar|scalar[]> $!headers
                | */
                |public function ${it.methodName}(${if (it.firstBody()?.type is FileType) "?UploadedFileInterface " else ""}$!body = null, array $!headers = []): ${it.toRequestName()} {
                |   $!args = $!this->getArgs();
                |   return new ${it.toRequestName()}(${it.allParams()?.map { "$!args['${it}'], " }?.joinToString("")}$!body, $!headers);
                |}
                |
            """.trimMargin()
        }.joinToString(separator = "")
    }

    private fun Resource.subResources(): String {
        return this.resources.map {
            """
                |/**
                | <<${it.uriParameters?.asSequence()?.map { "* @psalm-param scalar $${it.name}" }?.joinToString(separator = "\n") ?: "*"}>>
                | */
                |public function ${it.getMethodName()}(${it.uriParameters?.asSequence()?.map { "$${it.name} = null"  }?.joinToString(", ") ?: ""}): ${it.resourceBuilderName()} {
                |   $!args = $!this->getArgs();
                |   ${it.uriParameters?.asSequence()?.map { "if (!is_null($${it.name})) { $!args['${it.name}'] = $${it.name}; }" }?.joinToString("\n")}
                |   return new ${it.resourceBuilderName()}($!this->getUri() . '${it.relativeUri.template}', $!args);
                |}
                |
            """.trimMargin()
        }.joinToString(separator = "")
    }





//    fun Method.getAllParamNames(): List<String>? {
//        val params = this.getAbsoluteUri().getComponents().stream()
//                .filter({ uriTemplatePart -> uriTemplatePart is ExpressioResourceByProjectKeyProductsByIDImagesn })
//                .flatMap({ uriTemplatePart -> (uriTemplatePart as Expression).varSpecs.stream().map<String>(Function<VarSpec, String> { it.getVariableName() }) })
//                .collect(Collectors.toList<String>())
//        return if (params.size > 0) {
//            params
//        } else null
//    }
//
//    fun getQueryParameters(): List<QueryParameter> {
//        return method.getQueryParameters().stream().filter({ parameter ->
//            val placeholderParam = parameter.getAnnotation("placeholderParam") == null
//            placeholderParam
//        }).collect(Collectors.toList<QueryParameter>())
//    }

//    fun ResourceCollection.methods(): String {
//        return this.resources
//                .flatMap { resource -> resource.methods.map { javaBody(resource, it) } }
//                .joinToString(separator = "\n\n")
//    }

//    fun javaBody(resource: Resource, method: Method): String {
//        val methodReturnType = vrapTypeProvider.doSwitch(method.retyurnType())
//        val body = """
//            |${method.toComment().escapeAll()}
//            |@Retryable(
//            |          value = { ConnectException.class },
//            |          maxAttemptsExpression = "#{${'$'}{retry.${method.method.name}.maxAttempts}}",
//            |          backoff = @Backoff(delayExpression = "#{1}", maxDelayExpression = "#{5}", multiplierExpression = "#{2}"))
//            |public ${methodReturnType.fullClassName().escapeAll()} ${method.method.name.toLowerCase()}(${methodParameters(resource, method)}) {
//            |
//            |    final Map\<String, Object\> parameters = new HashMap\<\>();
//            |
//            |    <${resource.allUriParameters.map { "parameters.put(\"${it.name}\",${it.name});" }.joinToString(separator = "\n")}>
//            |
//            |    <${method.mediaType().escapeAll()}>
//            |
//            |    final ParameterizedTypeReference\<${method.retyurnType().toVrapType().fullClassName().escapeAll()}\> type = new ParameterizedTypeReference\<${method.retyurnType().toVrapType().fullClassName().escapeAll()}\>() {};
//            |    final String fullUri = baseUri + "${resource.fullUri.template}";
//            |
//            |    return restTemplate.exchange(fullUri, HttpMethod.${method.method.name.toUpperCase()}, entity, type, parameters).getBody();
//            |
//            |}
//                """.trimMargin()
//        return body
//    }
//
//    fun methodParameters(resource: Resource, method: Method): String {
//
//        val paramList = resource.allUriParameters
//                .map { "final ${it.type.toVrapType().simpleName()} ${it.name}" }
//                .toMutableList()
//
//        if (method.bodies?.isNotEmpty() ?: false) {
//            paramList.add(method.bodies[0].type.toVrapType().fullClassName())
//        }
//        return paramList.joinToString(separator = ", ")
//    }
//
//    fun Method.mediaType(): String {
//        if (this.bodies?.isNotEmpty() ?: false) {
//            val result = """
//                |final HttpHeaders headers = new HttpHeaders();
//                |headers.setContentType(MediaType.parseMediaType(("${this.bodies[0].contentType}")));
//                |final HttpEntity<${this.bodies[0].type.toVrapType().fullClassName()}> entity = new HttpEntity<>(body, headers);
//        """.trimMargin()
//            return result
//
//        }
//        return "final HttpEntity<?> entity = null;"
//
//    }
//
//    fun Method.retyurnType(): AnyType {
//        return this.responses
//                .filter { it.isSuccessfull() }
//                .filter { it.bodies?.isNotEmpty() ?: false }
//                .first()
//                .let { it.bodies[0].type }
//                ?: TypesFactoryImpl.eINSTANCE.createNilType()
//    }


    fun Response.isSuccessfull(): Boolean = this.statusCode.toInt() in (200..299)

    fun Method.returnType(): AnyType {
        return this.responses
                .filter { it.isSuccessfull() }
                .filter { it.bodies?.isNotEmpty() ?: false }
                .firstOrNull()
                ?.let { it.bodies[0].type }
                ?: TypesFactoryImpl.eINSTANCE.createNilType()
    }

    fun Method.returnTypeClass(): String {
        val vrapType = this.returnType().toVrapType()
        return when (vrapType) {
            is VrapObjectType -> vrapType.simpleName()
            else -> "JsonObject"
        }
    }

    fun Method.returnTypeFullClass(): String {
        val vrapType = this.returnType().toVrapType()
        return when (vrapType) {
            is VrapObjectType -> vrapType.fullClassName()
            else -> "${packagePrefix.toNamespaceName()}\\Base\\JsonObject"
        }
    }

    fun Resource.resourceBuilderName():String = "Resource${this.toResourceName()}"
}

package io.vrap.codegen.languages.php.model

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.responses.Response
import io.vrap.rmf.raml.model.types.FileType
import org.eclipse.emf.ecore.EObject

class PhpMethodRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, EObjectTypeExtensions {

    @Inject
    @Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME)
    lateinit var packagePrefix:String

    override fun render(type: Method): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        // final Body firstBodyType = method.getBodies().stream().findFirst().orElse(null);
        //                         case "serialize":
        //                            if (firstBodyType != null) {
        //                                if (firstBodyType.getType() instanceof FileType) {
        //                                    return "!is_null($body) ? $body->getStream() : null";
        //                                }
        //                            }
        //                            return "!is_null($body) ? json_encode($body) : null";
        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.`package`.toNamespaceName().escapeAll()};
            |
            |use ${packagePrefix.toNamespaceName().escapeAll()}\\Client\\ApiRequest;
            |
            |/** @psalm-suppress PropertyNotSetInConstructor */
            |class ${type.toRequestName()} extends ApiRequest
            |{
            |   // const RESULT_TYPE = <returnType(request.returnType)>::class;
            |
            |    /**
            |     <<${type.allParams()?.asSequence()?.map { "* @param string $$it" }?.joinToString(separator = "\n") ?: "*"}>>
            |     * @param object $!body
            |     * @param array $!headers
            |     */
            |    public function __construct(${type.allParams()?.asSequence()?.map { "$$it, "  }?.joinToString(separator = "") ?: ""}$!body = null, array $!headers = [])
            |    {
            |        $!uri = str_replace([${type.allParams()?.asSequence()?.map { "'{$it}'"  }?.joinToString(separator = ", ") ?: ""}], [${type.allParams()?.asSequence()?.map { "$$it"  }?.joinToString(separator = ", ") ?: ""}], '${type.resource().fullUri.template}');
            |        // <request.method; format="ensureHeader">
            |        parent::__construct('${type.methodName.toUpperCase()}', $!uri, $!headers, ${type.firstBody()?.serialize()?: "!is_null(\$body) ? json_encode(\$body) : null"});
            |    }
            |
            |    /**
            |     * @param ResponseInterface $!response
            |     * @param ResultMapper $!mapper
            |     * @return <returnType(request.returnType)>
            |     */
            |   // public function map(ResponseInterface $!response, ResultMapper $!mapper):  <returnType(request.returnType)>
            |   // {
            |   //     return parent::map($!response, $!mapper);
            |   // }
            |
            |   // <if(request.method.queryParameters)>
            |   // <request.method.queryParameters: {param |<withParam(request.name, param)>}>
            |   // <endif>
            |}
        """.trimMargin().keepIndentation("<<", ">>").forcedLiteralEscape()
        val relativeTypeNamespace = vrapType.`package`.toNamespaceName().replace(packagePrefix.toNamespaceName() + "\\", "").replace("\\", "/")
        val relativePath = "src/" + relativeTypeNamespace + "/" + type.toRequestName() + ".php"
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun Body.serialize(): String {
        if (this.type is FileType) {
            return "!is_null(\$body) ? \$body->getStream() : null"
        }
        return "!is_null(\$body) ? json_encode(\$body) : null"
    }





//    fun getAllParamNames(): List<String>? {
//        val params = getAbsoluteUri().getComponents().stream()
//                .filter({ uriTemplatePart -> uriTemplatePart is Expression })
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
}

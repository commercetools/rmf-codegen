package io.vrap.codegen.languages.php.test

import io.vrap.codegen.languages.extensions.deprecated
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.codegen.languages.php.AbstractRequestBuilder
import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ResourceRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance
import org.eclipse.emf.ecore.EObject
import java.net.URLEncoder

class PhpRequestTestRenderer constructor(api: Api, vrapTypeProvider: VrapTypeProvider, clientConstants: ClientConstants) : ResourceRenderer, AbstractRequestBuilder(api, vrapTypeProvider, clientConstants), EObjectTypeExtensions {
    private val resourcePackage = "Resource"

    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val clientTestPackageName = basePackagePrefix + "/test" + clientPackageName.replace(basePackagePrefix, "")
        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${clientTestPackageName.toNamespaceName().escapeAll()}\\$resourcePackage;
            |
            |use PHPUnit\\Framework\\TestCase;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Exception\\ApiClientException;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Exception\\ApiServerException;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Client\\ApiRequest;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\JsonObject;
            |use ${clientPackageName.toNamespaceName().escapeAll()}\\${rootResource()};
            |use Psr\\Http\\Message\\RequestInterface;
            |use GuzzleHttp\\Exception\\ClientException;
            |use GuzzleHttp\\Exception\\ServerException;
            |use GuzzleHttp\\ClientInterface;
            |use GuzzleHttp\\Psr7\\Response;
            |<<${type.resources.joinToString("\n") { r -> "use ${clientPackageName.toNamespaceName()}\\$resourcePackage\\${r.resourceBuilderName()};".escapeAll() }}>>
            |
            |/**
            | <<${type.methods.map { it.toRequestName() }.plus(type.resourceBuilderName()).joinToString("\n") { "* @covers \\${clientPackageName.toNamespaceName()}\\$resourcePackage\\$it".escapeAll() }}>>
            | */
            |class ${type.resourceBuilderName()}Test extends TestCase
            |{
            |    ${if (type.methods.any { !it.deprecated() }) """/**
            |     * @dataProvider getRequests()
            |     */
            |    public function testBuilder(callable $!builderFunction, string $!method, string $!relativeUri, ?string $!body = null)
            |    {
            |        $!builder = new ${rootResource()}();
            |        $!request = $!builderFunction($!builder);
            |        $!this->assertSame(strtolower($!method), strtolower($!request->getMethod()));
            |        $!this->assertSame($!relativeUri, (string) $!request->getUri());
            |        if (!is_null($!body)) {
            |            $!this->assertJsonStringEqualsJsonString($!body, (string) $!request->getBody());
            |        } else {
            |            $!this->assertSame("", (string) $!request->getBody());
            |        }
            |    }""".trimMargin() else ""}
            |
            |    ${if (type.resources.any { !it.deprecated() }) """/**
            |     * @dataProvider getResources()
            |     */
            |    public function testResources(callable $!builderFunction, string $!class, array $!expectedArgs)
            |    {
            |        $!builder = new ${rootResource()}();
            |        $!resource = $!builderFunction($!builder);
            |        $!this->assertInstanceOf($!class, $!resource);
            |        $!this->assertEquals($!expectedArgs, $!resource->getArgs());
            |    }""".trimMargin() else ""}
            |
            |    ${if (type.methods.any { !it.deprecated() }) """/**
            |     * @dataProvider getRequestBuilderResponses()
            |     */
            |    public function testMapFromResponse(callable $!builderFunction, $!statusCode)
            |    {
            |        $!builder = new ${rootResource()}();
            |        $!request = $!builderFunction($!builder);
            |        $!this->assertInstanceOf(ApiRequest::class, $!request);
            |
            |        $!response = new Response($!statusCode, [], "{}");
            |        $!this->assertInstanceOf(JsonObject::class, $!request->mapFromResponse($!response));
            |    }""".trimMargin() else ""}
            |
            |    ${if (type.methods.any { !it.deprecated() }) """/**
            |     * @dataProvider getRequestBuilders()
            |     */
            |    public function testExecuteClientException(callable $!builderFunction)
            |    {
            |        $!client = $!this->createMock(ClientInterface::class);
            |        
            |        $!builder = new ${rootResource()}($!client);
            |        $!request = $!builderFunction($!builder);
            |        $!client->method("send")->willThrowException(new ClientException("Oops!", $!request, new Response(400)));
            |        
            |        $!this->expectException(ApiClientException::class);
            |        $!request->execute();
            |    }""".trimMargin() else ""}
            |
            |    ${if (type.methods.any { !it.deprecated() }) """/**
            |     * @dataProvider getRequestBuilders()
            |     */
            |    public function testExecuteServerException(callable $!builderFunction)
            |    {
            |        $!client = $!this->createMock(ClientInterface::class);
            |        
            |        $!builder = new ${rootResource()}($!client);
            |        $!request = $!builderFunction($!builder);
            |        $!client->method("send")->willThrowException(new ServerException("Oops!", $!request, new Response(500)));

            |        $!this->expectException(ApiServerException::class);
            |        $!request->execute();
            |    }""".trimMargin() else ""}
            |
            |    public function getRequests()
            |    {
            |        return [
            |            <<${type.methods.filterNot { it.deprecated() }.flatMap { method -> method.queryParameters.map { parameterTestProvider(type, method, it) }.plus(parameterTestProvider(type, method)) }.joinToString(",\n")}>>
            |        ];
            |    }
            |    
            |    public function getResources()
            |    {
            |        return [
            |            <<${type.resources.filterNot { it.deprecated() }.joinToString(",\n") { resourceTestProvider(it) }}>>
            |        ];
            |    }
            |    
            |    public function getRequestBuilders()
            |    {
            |        return [
            |            <<${type.methods.filterNot { it.deprecated() }.joinToString(",\n") { method -> requestTestProvider(type, method) }}>>
            |        ];
            |    }
            |
            |    public function getRequestBuilderResponses()
            |    {
            |        return [
            |            <<${type.methods.filterNot { it.deprecated() }.flatMap { m -> m.responses.map { r -> requestTestProvider(type, m, r.statusCode) }.plus(requestTestProvider(type, m, "599")) }.joinToString(",\n")}>>
            |        ];
            |    }
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()

        val relativeTypeNamespace = vrapType.`package`.toNamespaceName().replace(basePackagePrefix.toNamespaceName() + "\\", "").replace("\\", "/") + "/$resourcePackage"
        val relativePath = "test/unit/" + relativeTypeNamespace + "/" + type.resourceBuilderName() + "Test.php"

        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun parameterTestProvider(resource: Resource, method: Method): String {
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "test_$p"} }\"" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null) "null" else ""})")

        return """
            |'${method.toRequestName()}' => [
            |    function (${rootResource()} $!builder): RequestInterface {
            |        return $!builder
            |            <<${builderChain.joinToString("\n->", "->")}>>;
            |    },
            |    '${method.method}',
            |    '${resource.fullUri.expand(resource.fullUriParameters.map { it.name to "test_${it.name}" }.toMap()).trimStart('/')}',
            |]
        """.trimMargin()
    }

    private fun requestTestProvider(resource: Resource, method: Method): String {
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") }\"" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null) "null" else ""})")

        return """
            |'${method.toRequestName()}' => [
            |    function (${rootResource()} $!builder): RequestInterface {
            |        return $!builder
            |            <<${builderChain.joinToString("\n->", "->")}>>;
            |    }
            |]
        """.trimMargin()
    }

    private fun requestTestProvider(resource: Resource, method: Method, statusCode: String): String {
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") }\"" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null) "null" else ""})")

        return """
            |'${method.toRequestName()}_$statusCode' => [
            |    function (${rootResource()} $!builder): RequestInterface {
            |        return $!builder
            |            <<${builderChain.joinToString("\n->", "->")}>>;
            |    },
            |    $statusCode
            |]
        """.trimMargin()
    }

    private fun resourceTestProvider(resource: Resource): String {
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "test_$p"} }\"" else ""})" }

        return """
            |'${resource.resourceBuilderName()}' => [
            |    function (${rootResource()} $!builder): ${resource.resourceBuilderName()} {
            |        return $!builder
            |            <<${builderChain.joinToString("\n->", "->")}>>;
            |    },
            |    ${resource.resourceBuilderName()}::class,
            |    [${resource.fullUriParameters.joinToString(", ") { "'${it.name}' => 'test_${it.name}'" }}],
            |    '${resource.fullUri.template}'
            |]
        """.trimMargin()
    }

    private fun parameterTestProvider(resource: Resource, method: Method, parameter: QueryParameter): String {
        val anno = parameter.getAnnotation("placeholderParam", true)

        var paramName: String = parameter.name
        var template = parameter.template()
        if (anno != null) {
            val o = anno.value as ObjectInstance
            val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
            val placeholderTemplate = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
            paramName = placeholderTemplate.value.replace("<${placeholder.value}>", placeholder.value)
            template = "'${placeholder.value}', '${paramName}'"
        }

        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\'${r.relativeUri.paramValues().joinToString("\', \'") { p -> "test_$p"} }\'" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null) "null" else ""})")
                .plus("${parameter.methodName()}(${template})")
        return """
            |'${method.toRequestName()}_${parameter.methodName()}' => [
            |    function (${rootResource()} $!builder): RequestInterface {
            |        return $!builder
            |            <<${builderChain.joinToString("\n->", "->")}>>;
            |    },
            |    '${method.method}',
            |    '${resource.fullUri.expand(resource.fullUriParameters.map { it.name to "test_${it.name}" }.toMap()).trimStart('/')}?${URLEncoder.encode(paramName, "UTF-8")}=${URLEncoder.encode(paramName, "UTF-8")}',
            |]
        """.trimMargin()
    }
}

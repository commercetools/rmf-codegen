package io.vrap.codegen.languages.php.test

import com.google.common.collect.Lists
import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.codegen.languages.php.AbstractRequestBuilder
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance
import org.eclipse.emf.ecore.EObject

class PhpRequestTestRenderer @Inject constructor(api: Api, vrapTypeProvider: VrapTypeProvider) : ResourceRenderer, AbstractRequestBuilder(api, vrapTypeProvider), EObjectTypeExtensions {
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
            |use Prophecy\\Argument;
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
            |    /**
            |     * @dataProvider getRequests()
            |     */
            |    public function testBuilder(callable $!builderFunction, string $!method, string $!relativeUri, string $!body = null)
            |    {
            |        $!builder = new ${rootResource()}();
            |        $!request = $!builderFunction($!builder);
            |        $!this->assertSame(strtolower($!method), strtolower($!request->getMethod()));
            |        $!this->assertStringContainsString(str_replace(['{', '}'], '', $!relativeUri), (string) $!request->getUri());
            |        if (!is_null($!body)) {
            |            $!this->assertJsonStringEqualsJsonString($!body, (string) $!request->getBody());
            |        };
            |    }
            |
            |    /**
            |     * @dataProvider getResources()
            |     */
            |    public function testResources(callable $!builderFunction, string $!class)
            |    {
            |        $!builder = new ${rootResource()}();
            |        $!this->assertInstanceOf($!class, $!builderFunction($!builder));
            |    }
            |
            |    /**
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
            |    }
            |
            |    /**
            |     * @dataProvider getRequestBuilders()
            |     */
            |    public function testExecuteClientException(callable $!builderFunction)
            |    {
            |        $!client = $!this->prophesize(ClientInterface::class);
            |        $!client->send(Argument::any(), Argument::any())->willThrow(ClientException::class);
            |        
            |        $!builder = new ${rootResource()}($!client->reveal());
            |        $!request = $!builderFunction($!builder);
            |        $!this->expectException(ApiClientException::class);
            |        $!request->execute();
            |    }
            |
            |    /**
            |     * @dataProvider getRequestBuilders()
            |     */
            |    public function testExecuteServerException(callable $!builderFunction)
            |    {
            |        $!client = $!this->prophesize(ClientInterface::class);
            |        $!client->send(Argument::any(), Argument::any())->willThrow(ServerException::class);
            |        
            |        $!builder = new ${rootResource()}($!client->reveal());
            |        $!request = $!builderFunction($!builder);
            |        $!this->expectException(ApiServerException::class);
            |        $!request->execute();
            |    }
            |
            |    public function getRequests()
            |    {
            |        return [
            |            <<${type.methods.flatMap { method -> method.queryParameters.map { parameterTestProvider(type, method, it) }.plus(parameterTestProvider(type, method)) }.joinToString(",\n")}>>
            |        ];
            |    }
            |    
            |    public function getResources()
            |    {
            |        return [
            |            <<${type.resources.map { resourceTestProvider(it) }.joinToString(",\n")}>>
            |        ];
            |    }
            |    
            |    public function getRequestBuilders()
            |    {
            |        return [
            |            <<${type.methods.joinToString(",\n") { method -> requestTestProvider(type, method) }}>>
            |        ];
            |    }
            |
            |    public function getRequestBuilderResponses()
            |    {
            |        return [
            |            <<${type.methods.flatMap { m -> m.responses.map { r -> requestTestProvider(type, m, r.statusCode) } }.joinToString(",\n")}>>
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
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") }\"" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null) "null" else ""})")

        return """
            |'${method.toRequestName()}' => [
            |    function (${rootResource()} $!builder): RequestInterface {
            |        return $!builder
            |            <<${builderChain.joinToString("\n->", "->")}>>;
            |    },
            |    '${method.method}',
            |    '${resource.fullUri.template.trimStart('/')}',
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
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") }\"" else ""})" }

        return """
            |'${resource.resourceBuilderName()}' => [
            |    function (${rootResource()} $!builder): ${resource.resourceBuilderName()} {
            |        return $!builder
            |            <<${builderChain.joinToString("\n->", "->")}>>;
            |    },
            |    ${resource.resourceBuilderName()}::class
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

        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\'${r.relativeUri.paramValues().joinToString("\', \'") }\'" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null) "null" else ""})")
                .plus("${parameter.methodName()}(${template})")
        return """
            |'${method.toRequestName()}_${parameter.methodName()}' => [
            |    function (${rootResource()} $!builder): RequestInterface {
            |        return $!builder
            |            <<${builderChain.joinToString("\n->", "->")}>>;
            |    },
            |    '${method.method}',
            |    '${resource.fullUri.template.trimStart('/')}?${paramName}=${paramName}',
            |]
        """.trimMargin()
    }



    private fun Resource.resourcePathList(): List<Resource> {
        val path = Lists.newArrayList<Resource>()
        if (this.fullUri.template == "/") {
            return path
        }
        path.add(this)
        var t = this.eContainer()
        while (t is Resource) {
            val template = t.fullUri.template
            if (template != "/") {
                path.add(t)
            }
            t = t.eContainer()
        }
        return Lists.reverse(path)
    }
}

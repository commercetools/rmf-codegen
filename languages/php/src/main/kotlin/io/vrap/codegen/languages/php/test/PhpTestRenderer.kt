package io.vrap.codegen.languages.php.test

import com.damnhandy.uri.template.UriTemplate
import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.php.AbstractRequestBuilder
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.forcedLiteralEscape
import io.vrap.codegen.languages.php.extensions.paramValues
import io.vrap.codegen.languages.php.extensions.resourcePathList
import io.vrap.codegen.languages.php.extensions.toNamespaceName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.util.StringCaseFormat

class PhpTestRenderer @Inject constructor(api: Api, vrapTypeProvider: VrapTypeProvider) : FileProducer, AbstractRequestBuilder(api, vrapTypeProvider) {

    override fun produceFiles(): List<TemplateFile> = listOf(
            configTest(api),
            rootResourceTest(api)
    )

    private fun configTest(type: Api): TemplateFile {
        val clientTestPackageName = basePackagePrefix + "/test" + clientPackageName.replace(basePackagePrefix, "")

        return TemplateFile(relativePath = "test/unit/Client/ConfigTest.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${clientTestPackageName.toNamespaceName().escapeAll()};
                    |
                    |use ${clientPackageName.toNamespaceName().escapeAll()}\\Config;
                    |use PHPUnit\\Framework\\TestCase;
                    |
                    |class ConfigTest extends TestCase
                    |{
                    |    public function testDefaultUri()
                    |    {
                    |        $!c = new Config(${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.testParams().joinToString(", ") } else ""});
                    |        $!expectedUri = "${api.baseUri.value.expand(api.baseUri.value.variables.map { it to "test_${it}" }.toMap())}";
                    |        self::assertSame($!expectedUri, $!c->getApiUri());
                    |        self::assertSame($!expectedUri, $!c->getOptions()[Config::OPT_BASE_URI]);
                    |    }
                    |
                    |    public function testBaseUri()
                    |    {
                    |        $!c = new Config(${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.testParams().joinToString(", ", postfix = ", ") } else ""}[], "baseUri");
                    |        self::assertSame("baseUri", $!c->getApiUri());
                    |        self::assertSame("baseUri", $!c->getOptions()[Config::OPT_BASE_URI]);
                    |    }
                    |
                    |    public function testOptionsBaseUri()
                    |    {
                    |        $!c = new Config(${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.testParams().joinToString(", ", postfix = ", ") } else ""}[Config::OPT_BASE_URI => "optBaseUri"], "baseUri");
                    |        self::assertSame("baseUri", $!c->getApiUri());
                    |        self::assertSame("optBaseUri", $!c->getOptions()[Config::OPT_BASE_URI]);
                    |    }
                    |}
                """.trimMargin().keepAngleIndent().forcedLiteralEscape())
    }

    private fun UriTemplate.testParams(): List<String> = variables.map {
        "\"test_${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)}\""
    }

    private fun rootResourceTest(type: Api): TemplateFile {
        val rootResource = type.resources.firstOrNull { resource -> resource.resourcePath == "/" }
        val clientTestPackageName = basePackagePrefix + "/test" + clientPackageName.replace(basePackagePrefix, "")
        val resources = if (rootResource != null && type.resources.size == 1) rootResource.resources else type.resources

        return TemplateFile(relativePath = "test/unit/Client/${rootResource()}Test.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${clientTestPackageName.toNamespaceName().escapeAll()};
                    |
                    |<<${type.imports()}>>
                    |use ${clientPackageName.toNamespaceName().escapeAll()}\\${rootResource()};
                    |use PHPUnit\\Framework\\TestCase;
                    |use GuzzleHttp\\ClientInterface;
                    |
                    |class ${rootResource()}Test extends TestCase
                    |{
                    |    public function testConstruct()
                    |    {
                    |        $!client = $!this->prophesize(ClientInterface::class);
                    |        $!root = new ${rootResource()}($!client->reveal());
                    |        $!this->assertInstanceOf(ClientInterface::class, $!root->getClient());
                    |        $!this->assertSame('', $!root->getUri());
                    |    }
                    |    
                    |    /**
                    |     * @dataProvider getResources()
                    |     */
                    |    public function testResources(callable $!builderFunction, string $!class, array $!expectedArgs)
                    |    {
                    |        $!builder = new ${rootResource()}();
                    |        $!resource = $!builderFunction($!builder);
                    |        $!this->assertInstanceOf($!class, $!resource);
                    |        $!this->assertEquals($!expectedArgs, $!resource->getArgs());
                    |    }
                    |    
                    |    public function getResources()
                    |    {
                    |        return [
                    |            <<${resources.map { resourceTestProvider(it) }.joinToString(",\n")}>>
                    |        ];
                    |    }
                    |}
                """.trimMargin().keepAngleIndent().forcedLiteralEscape())
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
}

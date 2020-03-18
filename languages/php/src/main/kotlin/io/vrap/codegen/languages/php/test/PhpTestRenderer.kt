package io.vrap.codegen.languages.php.test

import com.google.common.collect.Lists
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

class PhpTestRenderer @Inject constructor(api: Api, vrapTypeProvider: VrapTypeProvider) : FileProducer, AbstractRequestBuilder(api, vrapTypeProvider) {

    override fun produceFiles(): List<TemplateFile> = listOf(
            configTest(),
            rootResourceTest(api)
    )

    private fun configTest(): TemplateFile {
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
                    |        $!c = new Config();
                    |        self::assertSame(Config::API_URI, $!c->getApiUri());
                    |        self::assertSame(Config::API_URI, $!c->getOptions()[Config::OPT_BASE_URI]);
                    |    }
                    |
                    |    public function testBaseUri()
                    |    {
                    |        $!c = new Config([], "baseUri");
                    |        self::assertSame("baseUri", $!c->getApiUri());
                    |        self::assertSame("baseUri", $!c->getOptions()[Config::OPT_BASE_URI]);
                    |    }
                    |
                    |    public function testOptionsBaseUri()
                    |    {
                    |        $!c = new Config([Config::OPT_BASE_URI => "optBaseUri"], "baseUri");
                    |        self::assertSame("baseUri", $!c->getApiUri());
                    |        self::assertSame("optBaseUri", $!c->getOptions()[Config::OPT_BASE_URI]);
                    |    }
                    |}
                """.trimMargin().keepAngleIndent().forcedLiteralEscape())
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
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") }\"" else ""})" }

        return """
            |'${resource.resourceBuilderName()}' => [
            |    function (${rootResource()} $!builder): ${resource.resourceBuilderName()} {
            |        return $!builder
            |            <<${builderChain.joinToString("\n->", "->")}>>;
            |    },
            |    ${resource.resourceBuilderName()}::class,
            |    [${resource.fullUriParameters.joinToString(", ") { "'${it.name}' => '${it.name}'" }}],
            |    '${resource.fullUri.template}'
            |]
        """.trimMargin()
    }
}

package io.vrap.codegen.languages.php.base

import com.google.inject.Inject
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.raml.model.modules.Api

class PhpBaseTestFileProducer @Inject constructor(val api: Api) : FileProducer {

    @Inject
    @BasePackageName
    lateinit var packagePrefix:String

    override fun produceFiles(): List<TemplateFile> = listOf(
        apiRequestTest()
    )

    private fun apiRequestTest(): TemplateFile {
        return TemplateFile(relativePath = "test/unit/Client/ApiRequestTest.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Test\Client;
                    |
                    |use ${packagePrefix.toNamespaceName()}\Client\ApiRequest;
                    |use ${packagePrefix.toNamespaceName()}\Base\ResultMapper;
                    |use ${packagePrefix.toNamespaceName()}\Base\JsonObject;
                    |use PHPUnit\Framework\TestCase;
                    |use Psr\Http\Message\ResponseInterface;
                    |use GuzzleHttp\ClientInterface;
                    |
                    |/**
                    | * @covers \${packagePrefix.toNamespaceName()}\Client\ApiRequest
                    | */
                    |class ApiRequestTest extends TestCase
                    |{
                    |    public function testWithQueryParam()
                    |    {
                    |        $!client = $!this->prophesize(ClientInterface::class);
                    |        $!request = new ApiRequest($!client->reveal(), 'get', '/');
                    |        $!request = $!request->withQueryParam('foo', 'bar');
                    |        $!this->assertSame('foo=bar', $!request->getUri()->getQuery());
                    |        $!request = $!request->withQueryParam('foo', 'baz');
                    |        $!this->assertSame('foo=bar&foo=baz', $!request->getUri()->getQuery());
                    |        $!request = $!request->withQueryParam('bar', 'foo');
                    |        $!this->assertSame('bar=foo&foo=bar&foo=baz', $!request->getUri()->getQuery());
                    |        $!request = $!request->withQueryParam('bar', 'baz');
                    |        $!this->assertSame('bar=foo&bar=baz&foo=bar&foo=baz', $!request->getUri()->getQuery());
                    |    }
                    |
                    |    public function testContentTypeHeader()
                    |    {
                    |        $!client = $!this->prophesize(ClientInterface::class);
                    |        $!request = new ApiRequest($!client->reveal(), 'get', '/');
                    |        $!this->assertSame('application/json', $!request->getHeaderLine('content-type'));
                    |    }
                    |
                    |    public function testEnsureHeaders()
                    |    {
                    |        $!client = $!this->prophesize(ClientInterface::class);
                    |        $!request = new ApiRequest($!client->reveal(), 'get', '/', ['X-Foo' => 'bar']);
                    |        $!this->assertSame('application/json', $!request->getHeaderLine('content-type'));
                    |        $!this->assertSame('bar', $!request->getHeaderLine('x-foo'));
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }
}

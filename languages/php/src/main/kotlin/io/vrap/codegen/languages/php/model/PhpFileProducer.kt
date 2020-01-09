package io.vrap.codegen.languages.php.model

import com.damnhandy.uri.template.UriTemplate
import com.google.inject.Inject
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.di.SharedPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.util.StringCaseFormat

class PhpFileProducer @Inject constructor(val api: Api) : FileProducer {

    @Inject
    @BasePackageName
    lateinit var packagePrefix:String

    @Inject
    @ClientPackageName
    lateinit var clientPackageName: String

    @Inject
    @SharedPackageName
    lateinit var sharedPackageName: String


    override fun produceFiles(): List<TemplateFile> = listOf(
            authConfig(api),
            clientCredentialsConfig(api),
            composerJson(),
            config(api),
            psalm()
    )

    private fun composerJson(): TemplateFile {
        val vendorName = sharedPackageName.toLowerCase();
        val composerPackageName = packagePrefix.replace(sharedPackageName, "").trim('/').toLowerCase()
        return TemplateFile(relativePath = "composer.json",
                content = """
                    |{
                    |  "name": "${vendorName}/spec-sdk-${composerPackageName}",
                    |  "license": "MIT",
                    |  "type": "library",
                    |  "description": "",
                    |  "autoload": {
                    |    "psr-4": {
                    |      "${packagePrefix.toNamespaceName().escapeAll()}\\": [
                    |        "src/"
                    |      ]
                    |    }
                    |  },
                    |  "autoload-dev": {
                    |    "psr-4": {
                    |      "${packagePrefix.toNamespaceName().escapeAll()}\\Test\\": [
                    |        "test/unit/${packagePrefix.toNamespaceDir()}"
                    |      ]
                    |    }
                    |  },
                    |  "require": {
                    |    "php": ">=7.2",
                    |    "ext-json": "*",
                    |    "guzzlehttp/psr7": "^1.1",
                    |    "guzzlehttp/guzzle": "^6.0",
                    |    "psr/cache": "^1.0",
                    |    "psr/simple-cache": "^1.0",
                    |    "psr/log": "^1.0",
                    |    "psr/http-client": "^1.0",
                    |    "psr/http-message": "^1.0",
                    |    "cache/filesystem-adapter": "^1.0",
                    |    "${vendorName}/spec-base": "@dev"
                    |  },
                    |  "require-dev": {
                    |    "monolog/monolog": "^1.3",
                    |    "phpunit/phpunit": "^8.0",
                    |    "vimeo/psalm": "^3.4",
                    |    "cache/array-adapter": "^1.0",
                    |    "squizlabs/php_codesniffer": "^3.0"
                    |  },
                    |  "repositories": [
                    |    {
                    |      "type": "path",
                    |      "url": "../${vendorName}-base"
                    |    }
                    |  ]
                    |}
                """.trimMargin())
    }

    private fun psalm(): TemplateFile {
        return TemplateFile(relativePath = "psalm.xml",
                content = """
                    |<?xml version="1.0"?>
                    |<psalm
                    |    totallyTyped="true"
                    |    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    |    xmlns="https://getpsalm.org/schema/config"
                    |    xsi:schemaLocation="https://getpsalm.org/schema/config vendor/vimeo/psalm/config.xsd"
                    |
                    |    strictBinaryOperands="true"
                    |>
                    |    <projectFiles>
                    |        <directory name="src" />
                    |        <ignoreFiles>
                    |            <directory name="vendor" />
                    |        </ignoreFiles>
                    |    </projectFiles>
                    |</psalm>
                """.trimMargin()
        )
    }

    private fun config(api: Api): TemplateFile {
        return TemplateFile(relativePath = "src/${clientPackageName.replace(packagePrefix, "").toNamespaceDir()}/Config.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${clientPackageName.toNamespaceName().escapeAll()};
                    |
                    |use ${sharedPackageName.toNamespaceName()}\\Client\\Config as BaseConfig;
                    |
                    |class Config implements BaseConfig
                    |{
                    |    const API_URI = '${api.baseUri.template}';
                    |
                    |    <<${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.constVariables()} else ""}>>
                    |
                    |    /** @psalm-var string */
                    |    private $!apiUri;
                    |
                    |    /** @psalm-var array */
                    |    private $!clientOptions;
                    |
                    |    public function __construct(${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.params() } else ""}array $!clientOptions = [], string $!baseUri = null)
                    |    {
                    |        /** @psalm-var string $!apiUri */
                    |        $!apiUri = $!baseUri ?? static::API_URI;
                    |        <<${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.replaceValues()} else ""}>>
                    |        $!this->apiUri = $!apiUri;
                    |        $!this->clientOptions = $!clientOptions;
                    |    }
                    |
                    |    public function getApiUri(): string
                    |    {
                    |        return $!this->apiUri;
                    |    }
                    |
                    |    public function getClientOptions(): array
                    |    {
                    |        return $!this->clientOptions;
                    |    }
                    |
                    |    public function getOptions(): array
                    |    {
                    |        return array_replace(
                    |            [self::OPT_BASE_URI => $!this->apiUri],
                    |            $!this->clientOptions
                    |        );
                    |    }
                    |}
                """.trimMargin().keepAngleIndent().forcedLiteralEscape())
    }

    fun UriTemplate.replaceValues(): String = variables
            .map { "$!apiUri = str_replace('{$it}', $!${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)}, $!apiUri);" }
            .joinToString(separator = "\n")

    fun UriTemplate.params(): String = variables
            .map { "string $${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)}" }
            .joinToString(separator = ", ", postfix = ", ")


    fun UriTemplate.constVariables(): String = variables
            .map { "const OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}= '$it';" }
            .joinToString(separator = "\n")

    private fun authConfig(api: Api): TemplateFile {
        return TemplateFile(relativePath = "src/${clientPackageName.replace(packagePrefix, "").toNamespaceDir()}/AuthConfig.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${clientPackageName.toNamespaceName()};
                    |
                    |use ${sharedPackageName.toNamespaceName()}\Client\AuthConfig as BaseAuthConfig;
                    |
                    |abstract class AuthConfig implements BaseAuthConfig
                    |{
                    |    const AUTH_URI = '${api.authUri()}';
                    |
                    |    const GRANT_TYPE = '';
                    |
                    |    /** @psalm-var string */
                    |    private $!authUri;
                    |
                    |    /** @psalm-var array */
                    |    private $!clientOptions;
                    |
                    |    public function __construct(array $!clientOptions = [], string $!authUri = self::AUTH_URI)
                    |    {
                    |        /** @psalm-var string authUri */
                    |        $!this->authUri = $!authUri;
                    |        $!this->clientOptions = $!clientOptions;
                    |    }
                    |
                    |    public function getGrantType(): string
                    |    {
                    |        /** @psalm-var string */
                    |        return static::GRANT_TYPE;
                    |    }
                    |
                    |    public function getAuthUri(): string
                    |    {
                    |        return $!this->authUri;
                    |    }
                    |
                    |    public function getClientOptions(): array
                    |    {
                    |        return $!this->clientOptions;
                    |    }
                    |
                    |    public function getOptions(): array
                    |    {
                    |        return array_replace(
                    |            [self::OPT_BASE_URI => $!this->authUri],
                    |            $!this->clientOptions
                    |        );
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun clientCredentialsConfig(api: Api): TemplateFile {
        return TemplateFile(relativePath = "src/${clientPackageName.replace(packagePrefix, "").toNamespaceDir()}/ClientCredentialsConfig.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${clientPackageName.toNamespaceName()};
                    |
                    |use ${sharedPackageName.toNamespaceName()}\Client\ClientCredentials;
                    |use ${sharedPackageName.toNamespaceName()}\Client\ClientCredentialsConfig as BaseClientCredentialsConfig;
                    |
                    |class ClientCredentialsConfig extends AuthConfig implements BaseClientCredentialsConfig
                    |{
                    |    const AUTH_URI = '${api.authUri()}';
                    |
                    |    const GRANT_TYPE = 'client_credentials';
                    |
                    |    /** @psalm-var ClientCredentials */
                    |    private $!credentials;
                    |
                    |    public function __construct(ClientCredentials $!credentials, array $!clientOptions = [], string $!authUri = self::AUTH_URI)
                    |    {
                    |        parent::__construct($!clientOptions, $!authUri);
                    |        $!this->credentials = $!credentials;
                    |    }
                    |
                    |    public function getCredentials(): ClientCredentials
                    |    {
                    |        return $!this->credentials;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }
}

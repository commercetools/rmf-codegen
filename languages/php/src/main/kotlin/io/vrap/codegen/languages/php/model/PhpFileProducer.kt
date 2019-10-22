package io.vrap.codegen.languages.php.model

import com.damnhandy.uri.template.UriTemplate
import com.google.inject.Inject
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.di.SharedPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
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
            config(api),
            authConfig(api),
            clientCredentialsConfig(api),
            composerJson(),
            psalm()
    )

    private fun composerJson(): TemplateFile {
        val vendorName = sharedPackageName.toLowerCase();
        val composerPackageName = packagePrefix.replace(sharedPackageName, "").trim('/').toLowerCase()
        return TemplateFile(relativePath = "composer.json",
                content = """
                    |{
                    |  "name": "${vendorName}/raml-sdk-${composerPackageName}",
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
                    |    "${vendorName}/raml-base": "@dev"
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
                    |      "url": "../../../build/gensrc/commercetools-raml-base"
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
                    |    /** @var string */
                    |    private $!apiUri;
                    |
                    |    /** @var array */
                    |    private $!clientOptions;
                    |
                    |    public function __construct(${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.params() } else ""}array $!config = [])
                    |    {
                    |        /** @var string $!apiUri */
                    |        $!apiUri = isset($!config[self::OPT_BASE_URI]) ? $!config[self::OPT_BASE_URI] : static::API_URI;
                    |        <<${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.replaceValues()} else ""}>>
                    |        $!this->apiUri = $!apiUri;
                    |        $!this->clientOptions = isset($!config[self::OPT_CLIENT_OPTIONS]) && is_array($!config[self::OPT_CLIENT_OPTIONS]) ?
                    |            $!config[self::OPT_CLIENT_OPTIONS] : [];
                    |    }
                    |
                    |    public function getApiUri(): string
                    |    {
                    |        return $!this->apiUri;
                    |    }
                    |
                    |    public function setApiUri(string $!apiUri): BaseConfig
                    |    {
                    |        $!this->apiUri = $!apiUri;
                    |        return $!this;
                    |    }
                    |
                    |    public function getClientOptions(): array
                    |    {
                    |        return $!this->clientOptions;
                    |    }
                    |
                    |    public function setClientOptions(array $!options): BaseConfig
                    |    {
                    |        $!this->clientOptions = $!options;
                    |        return $!this;
                    |    }
                    |
                    |    public function getOptions(): array
                    |    {
                    |        return array_replace(
                    |            [self::OPT_BASE_URI => $!this->getApiUri()],
                    |            $!this->clientOptions
                    |        );
                    |    }
                    |}
                """.trimMargin().keepIndentation("<<", ">>").forcedLiteralEscape())
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
                    |    /** @var string */
                    |    private $!authUri;
                    |
                    |    /** @var array */
                    |    private $!clientOptions;
                    |
                    |    public function __construct(array $!config = [])
                    |    {
                    |        /** @var string authUri */
                    |        $!this->authUri = isset($!config[self::OPT_AUTH_URI]) ? $!config[self::OPT_AUTH_URI] : static::AUTH_URI;
                    |        $!this->clientOptions = isset($!config[self::OPT_CLIENT_OPTIONS]) && is_array($!config[self::OPT_CLIENT_OPTIONS]) ?
                    |            $!config[self::OPT_CLIENT_OPTIONS] : [];
                    |    }
                    |
                    |    public function getGrantType(): string
                    |    {
                    |        /** @var string */
                    |        return static::GRANT_TYPE;
                    |    }
                    |
                    |    public function getAuthUri(): string
                    |    {
                    |        return $!this->authUri;
                    |    }
                    |
                    |    public function setAuthUri(string $!authUri): BaseAuthConfig
                    |    {
                    |        $!this->authUri = $!authUri;
                    |        return $!this;
                    |    }
                    |
                    |    public function getClientOptions(): array
                    |    {
                    |        return $!this->clientOptions;
                    |    }
                    |
                    |    public function setClientOptions(array $!options): BaseAuthConfig
                    |    {
                    |        $!this->clientOptions = $!options;
                    |        return $!this;
                    |    }
                    |
                    |    public function getOptions(): array
                    |    {
                    |        return array_replace(
                    |            [self::OPT_BASE_URI => $!this->authUri],
                    |            $!this->clientOptions
                    |        );
                    |    }
                    |    
                    |    abstract function getCacheKey(): string;
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
                    |use ${sharedPackageName.toNamespaceName()}\Client\ClientCredentialsConfig as BaseClientCredentialsConfig;
                    |
                    |class ClientCredentialsConfig extends AuthConfig implements BaseClientCredentialsConfig
                    |{
                    |    const AUTH_URI = '${api.authUri()}';
                    |
                    |    const GRANT_TYPE = 'client_credentials';
                    |
                    |    /** @var string */
                    |    private $!clientId;
                    |
                    |    /** @var string */
                    |    private $!clientSecret;
                    |
                    |    /** @var ?string */
                    |    private $!scope;
                    |
                    |    /**
                    |     * @psalm-param array<string, string> $!authConfig
                    |     */
                    |    public function __construct(array $!authConfig = [])
                    |    {
                    |        parent::__construct($!authConfig);
                    |        $!this->clientId = isset($!authConfig[self::CLIENT_ID]) ? $!authConfig[self::CLIENT_ID] : '';
                    |        $!this->clientSecret = isset($!authConfig[self::CLIENT_SECRET]) ? $!authConfig[self::CLIENT_SECRET] : '';
                    |        $!this->scope = isset($!authConfig[self::SCOPE]) ? $!authConfig[self::SCOPE] : null;
                    |    }
                    |
                    |    public function getClientId(): string
                    |    {
                    |        return $!this->clientId;
                    |    }
                    |
                    |    public function getScope(): ?string
                    |    {
                    |        return $!this->scope;
                    |    }
                    |
                    |    public function setScope(string $!scope = null): BaseClientCredentialsConfig
                    |    {
                    |        $!this->scope = $!scope;
                    |        return $!this;
                    |    }
                    |
                    |    public function setClientId(string $!clientId): BaseClientCredentialsConfig
                    |    {
                    |        $!this->clientId = $!clientId;
                    |        return $!this;
                    |    }
                    |
                    |    public function getClientSecret(): string
                    |    {
                    |        return $!this->clientSecret;
                    |    }
                    |
                    |    public function setClientSecret(string $!clientSecret): BaseClientCredentialsConfig
                    |    {
                    |        $!this->clientSecret = $!clientSecret;
                    |        return $!this;
                    |    }
                    |
                    |    public function getCacheKey(): string
                    |    {
                    |        return sha1($!this->clientId . (string)$!this->scope);
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }
}

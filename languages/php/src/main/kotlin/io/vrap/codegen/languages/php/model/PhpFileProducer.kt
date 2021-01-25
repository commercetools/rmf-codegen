package io.vrap.codegen.languages.php.model

import com.damnhandy.uri.template.UriTemplate
import com.google.inject.Inject
import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.UriParameter
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.util.StringCaseFormat

class PhpFileProducer @Inject constructor(val api: Api, clientConstants: ClientConstants) : FileProducer {

    private val basePackagePrefix = clientConstants.basePackagePrefix

    private val sharedPackageName = clientConstants.sharedPackageName

    private val clientPackageName = clientConstants.clientPackage

    override fun produceFiles(): List<TemplateFile> = listOf(
            authConfig(api),
            clientCredentialsConfig(api),
            composerJson(),
            config(api),
            psalm()
    )

    private fun composerJson(): TemplateFile {
        val vendorName = sharedPackageName.toLowerCase()
        val composerPackageName = basePackagePrefix.replace(sharedPackageName, "").trim('/').toLowerCase()
        return TemplateFile(relativePath = "composer.json",
                content = """
                    |{
                    |  "name": "${vendorName}/${vendorName}-sdk-${composerPackageName}",
                    |  "license": "MIT",
                    |  "type": "library",
                    |  "description": "",
                    |  "autoload": {
                    |    "psr-4": {
                    |      "${basePackagePrefix.toNamespaceName().escapeAll()}\\": [
                    |        "src/"
                    |      ]
                    |    }
                    |  },
                    |  "autoload-dev": {
                    |    "psr-4": {
                    |      "${basePackagePrefix.toNamespaceName().escapeAll()}\\Test\\": [
                    |        "test/unit/${basePackagePrefix.toNamespaceDir()}"
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
                    |    "$vendorName/$vendorName-sdk-base": "@dev"
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
                    |      "url": "../$vendorName-base"
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
        return TemplateFile(relativePath = "src/${clientPackageName.replace(basePackagePrefix, "").toNamespaceDir()}/Config.php",
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
                    |    public const API_URI = '${api.baseUri.template}';
                    |
                    |    <<${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.constVariables()} else ""}>>
                    |
                    |    /** @psalm-var string */
                    |    private $!apiUri;
                    |
                    |    /** @psalm-var array */
                    |    private $!options;
                    |
                    |    public function __construct(${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.paramDefinitions() } else ""}array $!clientOptions = [], string $!baseUri = null)
                    |    {
                    |        /** @psalm-var string $!apiUri */
                    |        $!apiUri = $!baseUri ?? static::API_URI;
                    |        <<${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.replaceValues(api.baseUriParameters.defaultValues())} else ""}>>
                    |        $!this->apiUri = $!apiUri;
                    |        $!this->options = array_replace(
                    |            [self::OPT_BASE_URI => $!this->apiUri],
                    |            $!clientOptions
                    |        );
                    |    }
                    |
                    |    public function getApiUri(): string
                    |    {
                    |        return $!this->apiUri;
                    |    }
                    |
                    |    public function getOptions(): array
                    |    {
                    |        return $!this->options;
                    |    }
                    |}
                """.trimMargin().keepAngleIndent().forcedLiteralEscape())
    }

    private fun UriTemplate.replaceValues(defaultValues: Map<String, String>): String = replaceValues("apiUri", defaultValues)

    private fun UriTemplate.replaceValues(variableName: String, defaultValues: Map<String, String>): String {
        return """
            |$!$variableName = str_replace(
            |    [
            |        ${variables.joinToString(",\n") { "self::OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}" }}
            |    ],
            |    [
            |        ${variables.joinToString(",\n") { "$${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)} ?? \"${defaultValues.getOrDefault(it, "")}\"" }}
            |    ],
            |    $!$variableName
            |);""".trimMargin()
    }

    private fun List<UriParameter>.defaultValues(): Map<String, String> = this.associateBy( {it.name}, { it.type?.default?.value.toString() })

    private fun UriTemplate.defaultValues(accessTokenUriParams: ObjectInstance?): Map<String, String> {
        return variables.associateBy({it}, {(accessTokenUriParams?.getValue(it) as? ObjectInstance)?.getValue("default")?.value.toString() } )
    }

    private fun UriTemplate.paramDefinitions(): String = variables.joinToString(separator = ", ", postfix = ", ") {
        "string $${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)} = null"
    }

    private fun UriTemplate.paramVariables(): String = variables.joinToString(separator = ", ", postfix = ", ") { "$${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)}" }

    private fun UriTemplate.constVariables(): String = variables.joinToString(separator = "\n") { "public const OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)} = '{$it}';" }

    private fun authConfig(api: Api): TemplateFile {

        return TemplateFile(relativePath = "src/${clientPackageName.replace(basePackagePrefix, "").toNamespaceDir()}/BaseAuthConfig.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${clientPackageName.toNamespaceName().escapeAll()};
                    |
                    |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Client\\AuthConfig;
                    |
                    |abstract class BaseAuthConfig implements AuthConfig
                    |{
                    |    public const AUTH_URI = '${api.accessTokenUri().template}';
                    |
                    |    <<${if (api.accessTokenUri().variables.isNotEmpty()) { api.accessTokenUri().constVariables()} else ""}>>
                    |
                    |    public const GRANT_TYPE = '';
                    |
                    |    /** @psalm-var string */
                    |    private $!authUri;
                    |
                    |    /** @psalm-var array */
                    |    private $!options;
                    |
                    |    public function __construct(${if (api.accessTokenUri().variables.isNotEmpty()) { api.accessTokenUri().paramDefinitions() } else ""}array $!clientOptions = [], string $!authUri = self::AUTH_URI)
                    |    {
                    |        /** @psalm-var string authUri */
                    |        <<${if (api.accessTokenUri().variables.isNotEmpty()) { api.accessTokenUri().replaceValues("authUri", api.accessTokenUri().defaultValues(api.accessTokenUriParams()))} else ""}>>
                    |        $!this->authUri = $!authUri;
                    |        $!this->options = array_replace(
                    |            [self::OPT_BASE_URI => $!this->authUri],
                    |            $!clientOptions
                    |        );
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
                    |    public function getOptions(): array
                    |    {
                    |        return $!this->options;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape().keepAngleIndent())
    }

    private fun clientCredentialsConfig(api: Api): TemplateFile {
        return TemplateFile(relativePath = "src/${clientPackageName.replace(basePackagePrefix, "").toNamespaceDir()}/ClientCredentialsConfig.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${clientPackageName.toNamespaceName().escapeAll()};
                    |
                    |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Client\\ClientCredentials;
                    |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Client\\ClientCredentialsConfig as BaseClientCredentialsConfig;
                    |
                    |class ClientCredentialsConfig extends BaseAuthConfig implements BaseClientCredentialsConfig
                    |{
                    |    public const AUTH_URI = '${api.accessTokenUri().template}';
                    |
                    |    public const GRANT_TYPE = 'client_credentials';
                    |
                    |    /** @psalm-var ClientCredentials */
                    |    private $!credentials;
                    |
                    |    public function __construct(ClientCredentials $!credentials, ${if (api.accessTokenUri().variables.isNotEmpty()) { api.accessTokenUri().paramDefinitions() } else ""}array $!clientOptions = [], string $!authUri = self::AUTH_URI)
                    |    {
                    |        parent::__construct(${if (api.accessTokenUri().variables.isNotEmpty()) { api.accessTokenUri().paramVariables() } else ""}$!clientOptions, $!authUri);
                    |        $!this->credentials = $!credentials;
                    |    }
                    |
                    |    public function getCredentials(): ClientCredentials
                    |    {
                    |        return $!this->credentials;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape().keepAngleIndent()
        )
    }

//    fun UriTemplate.params(accessTokenUriParams: ObjectInstance?): String {
//        return variables.joinToString(separator = ", ", postfix = ", ") {
//            "string $${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)} = \"${(accessTokenUriParams?.getValue(it) as? ObjectInstance)?.getValue("default")?.value}\""
//        }
//    }
}

package io.vrap.codegen.languages.php.model

import com.damnhandy.uri.template.UriTemplate
import com.google.inject.Inject
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
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

    override fun produceFiles(): List<TemplateFile> = listOf(
            collection(),
            baseNullable(),
            clientFactory(),
            tokenProvider(),
            token(),
            composerJson(),
            config(api),
            credentialTokenProvider(),
            cachedProvider(),
            rawTokenProvider(),
            oauth2Handler(),
            middlewareFactory(),
            authConfig(api),
            clientCredentialsConfig(api),
            tokenModel(),
            oauthHandlerFactory(),
            baseException(),
            invalidArgumentException(),
            apiRequest(),
            mapperFactory(),
            jsonObjectModel(),
            jsonObject(),
            baseJsonObject(),
            mapperIterator(),
            mapCollection(),
            psalm(),
            resource(),
            resultMapper(),
            mapperInterface(),
            jsonObjectCollection(),
            apiClientException(),
            apiServerException()
    )

    private fun collection(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/Collection.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |interface Collection
                    |{
                    |}
                """.trimMargin()
        )
    }

    private fun resultMapper(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/ResultMapper.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
                    |use Psr\Http\Message\ResponseInterface;
                    |
                    |class ResultMapper implements MapperInterface
                    |{
                    |    /**
                    |     * @template T
                    |     * @psalm-param class-string<T> $!type
                    |     * @psalm-return T
                    |     */
                    |    public function mapResponseToClass($!type, ResponseInterface $!response)
                    |    {
                    |        return new $!type($!this->responseData($!response));
                    |    }
                    |
                    |    /**
                    |     * @psalm-return scalar
                    |     */
                    |    private function responseData(ResponseInterface $!response)
                    |    {
                    |        $!body = (string)$!response->getBody();
                    |        /** @psalm-var ?scalar $!data */
                    |        $!data = json_decode($!body);
                    |        if (is_null($!data)) {
                    |           throw new InvalidArgumentException();
                    |        }
                    |        return $!data;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun mapperInterface(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/MapperInterface.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |use Psr\Http\Message\ResponseInterface;
                    |
                    |interface MapperInterface
                    |{
                    |    /**
                    |     * @template T
                    |     * @psalm-param class-string<T> $!type
                    |     * @psalm-return T
                    |     */
                    |    public function mapResponseToClass($!type, ResponseInterface $!response);
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun mapperFactory(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/MapperFactory.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |use DateTime;
                    |use DateTimeImmutable;
                    |
                    |class MapperFactory
                    |{
                    |    const DATETIME_FORMAT = "Y-m-d?H:i:s.uT";
                    |
                    |    /**
                    |     * @psalm-return callable(mixed): ?string
                    |     */
                    |    public static function stringMapper() {
                    |       return
                    |           /** @psalm-param ?mixed $!data */
                    |           function ($!data): ?string {
                    |               if (is_null($!data)) {
                    |                   return null;
                    |               }
                    |               return (string)$!data;
                    |           };
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable(?mixed): ?float
                    |     */
                    |    public static function numberMapper() {
                    |       return
                    |           /** @psalm-param ?mixed $!data */
                    |           function ($!data): ?float {
                    |               if (is_null($!data)) {
                    |                   return null;
                    |               }
                    |               return (float)$!data;
                    |           };
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable(?mixed): ?int
                    |     */
                    |    public static function integerMapper() {
                    |       return
                    |           /** @psalm-param ?mixed $!data */
                    |           function ($!data): ?int {
                    |               if (is_null($!data)) {
                    |                   return null;
                    |               }
                    |               return (int)$!data;
                    |           };
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable(?string): ?DateTimeImmutable
                    |     */
                    |    public static function dateTimeMapper(string $!format = self::DATETIME_FORMAT) {
                    |       return
                    |           /** @psalm-param ?string $!data */
                    |           function ($!data) use ($!format): ?DateTimeImmutable {
                    |               if (is_null($!data)) {
                    |                   return null;
                    |               }
                    |               $!date = DateTimeImmutable::createFromFormat($!format, $!data);
                    |               if ($!date === false) {
                    |                   return null;
                    |               }
                    |               return $!date;
                    |           };
                    |    }
                    |
                    |    /**
                    |     * @template T
                    |     * @psalm-return callable(?mixed): ?T
                    |     * @psalm-param class-string<T> $!className
                    |     */
                    |    public static function classMapper(string $!className) {
                    |       return
                    |           /**
                    |            * @psalm-param ?mixed $!data
                    |            * @psalm-return ?T
                    |            */
                    |           function ($!data) use ($!className): ?object {
                    |               if (is_null($!data)) {
                    |                   return null;
                    |               }
                    |               return new $!className($!data);
                    |           };
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun jsonObjectModel(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/JsonObjectModel.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |class JsonObjectModel extends BaseJsonObject implements JsonObject
                    |{
                    |    /**
                    |     * @psalm-return scalar|array<int, mixed>|JsonObject|JsonObjectCollection|null
                    |     */
                    |    final public function get(string $!field)
                    |    {
                    |        $!data = $!this->raw($!field);
                    |        if ($!data instanceof \stdClass) {
                    |            return new JsonObjectModel($!data);
                    |        }
                    |        if (is_array($!data) && isset($!data[0]) && $!data[0] instanceof \stdClass) {
                    |            /** @psalm-var ?array<int, object> $!data */
                    |            return new JsonObjectCollection($!data);
                    |        }
                    |        return $!data;
                    |    }
                    |
                    |    final protected function toArray(): array
                    |    {
                    |        $!data = array_filter(
                    |            get_object_vars($!this),
                    |            /**
                    |             * @psalm-param mixed|null $!value
                    |             * @return bool
                    |             */
                    |            function($!value) {
                    |                return !is_null($!value);
                    |            },
                    |            ARRAY_FILTER_USE_BOTH
                    |        );
                    |        $!data = array_merge($!this->getRawDataArray(), $!data);
                    |        return $!data;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun jsonObject(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/JsonObject.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |interface JsonObject extends \JsonSerializable
                    |{
                    |    /**
                    |     * @psalm-return scalar|array<int, mixed>|JsonObject|JsonObjectCollection|null
                    |     */
                    |    public function get(string $!field);
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun baseJsonObject(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/BaseJsonObject.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |abstract class BaseJsonObject implements JsonObject
                    |{
                    |    /** @var ?object */
                    |    private $!rawData;
                    |
                    |    public function __construct(object $!data = null)
                    |    {
                    |        $!this->rawData = $!data;
                    |    }
                    |
                    |    /**
                    |     * @psalm-return scalar|array<int, mixed>|\stdClass|null
                    |     */
                    |    final protected function raw(string $!field)
                    |    {
                    |        if (isset($!this->rawData->$!field)) {
                    |            /** @psalm-suppress PossiblyNullPropertyFetch */
                    |            return $!this->rawData->$!field;
                    |        }
                    |        return null;
                    |    }
                    |
                    |    public function jsonSerialize()
                    |    {
                    |        return (object)$!this->toArray();
                    |    }
                    |
                    |    /**
                    |     * @return array
                    |     */
                    |    final protected function getRawDataArray(): array
                    |    {
                    |        if (is_null($!this->rawData)) {
                    |            return [];
                    |        }
                    |        return get_object_vars($!this->rawData);
                    |    }
                    |
                    |    /**
                    |     * @return array
                    |     */
                    |    abstract protected function toArray(): array;
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun baseNullable(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/Nullable.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |interface Nullable
                    |{
                    |    public function isPresent(): bool;
                    |}
                """.trimMargin())
    }

    private fun clientFactory(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/ClientFactory.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
                    |use GuzzleHttp\Client as HttpClient;
                    |use GuzzleHttp\HandlerStack;
                    |use Psr\Cache\CacheItemPoolInterface;
                    |use Psr\Log\LoggerInterface;
                    |use Psr\SimpleCache\CacheInterface;
                    |
                    |class ClientFactory
                    |{
                    |    /**
                    |     * @param Config|array $!config
                    |     * @psalm-param CacheItemPoolInterface|CacheInterface|null $!cache
                    |     * @psalm-param array<string, callable> $!middlewares
                    |     * @throws InvalidArgumentException
                    |     */
                    |    public function createGuzzleClient(AuthConfig $!authConfig, LoggerInterface $!logger, $!cache = null, $!config = [],  array $!middlewares = []): HttpClient
                    |    {
                    |        $!config = $!this->createConfig($!config);
                    |        $!middlewares = array_merge(
                    |           MiddlewareFactory::createDefaultMiddlewares($!logger, $!authConfig, $!cache),
                    |           $!middlewares
                    |        );
                    |        return $!this->createGuzzle6Client($!config->getOptions(), $!middlewares);
                    |    }
                    |
                    |    /**
                    |     * @param Config|array $!config
                    |     * @psalm-param array<string, callable> $!middlewares
                    |     * @throws InvalidArgumentException
                    |     */
                    |    public function createGuzzleClientForMiddlewares(
                    |       $!config = [],
                    |       array $!middlewares = []): HttpClient
                    |    {
                    |        $!config = $!this->createConfig($!config);
                    |        return $!this->createGuzzle6Client($!config->getOptions(), $!middlewares);
                    |    }
                    |
                    |    /**
                    |     * @psalm-assert Config $!config
                    |     * @psalm-param mixed $!config
                    |     * @param Config|array $!config
                    |     * @throws InvalidArgumentException
                    |     */
                    |    private function createConfig($!config): Config
                    |    {
                    |        if ($!config instanceof Config) {
                    |            return $!config;
                    |        }
                    |        if (is_array($!config)) {
                    |            return new Config($!config);
                    |        }
                    |        throw new InvalidArgumentException('Provide either a configuration array or a Config instance.');
                    |    }
                    |
                    |    /**
                    |     * @throws InvalidArgumentException
                    |     */
                    |    private function createGuzzle6Client(array $!options, array $!middlewares = []): HttpClient
                    |    {
                    |        if (isset($!options['handler']) && $!options['handler'] instanceof HandlerStack) {
                    |            $!stack = $!options['handler'];
                    |        } else {
                    |            $!stack = HandlerStack::create();
                    |            $!options['handler'] = $!stack;
                    |        }
                    |
                    |        $!options = array_merge(
                    |            [
                    |                'allow_redirects' => false,
                    |                'verify' => true,
                    |                'timeout' => 60,
                    |                'connect_timeout' => 10,
                    |                'pool_size' => 25
                    |            ],
                    |            $!options
                    |        );
                    |        /**
                    |         * @var string $!key
                    |         * @var callable $!middleware
                    |         */
                    |        foreach ($!middlewares as $!key => $!middleware) {
                    |            if(!is_callable($!middleware)) {
                    |                throw new InvalidArgumentException('Middleware isn\'t callable');
                    |            }
                    |            $!name = is_numeric($!key) ? '' : $!key;
                    |            $!stack->push($!middleware, $!name);
                    |        }
                    |
                    |        $!client = new HttpClient($!options);
                    |
                    |        return $!client;
                    |    }
                    |
                    |    public static function of(): ClientFactory
                    |    {
                    |        return new self();
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }
    
    private fun tokenProvider(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/TokenProvider.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |interface TokenProvider
                    |{
                    |    public function getToken(): Token;
                    |
                    |    public function refreshToken(): Token;
                    |}
                """.trimMargin()
        )
    }

    private fun token(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/Token.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |interface Token
                    |{
                    |    public function getValue(): string;
                    |
                    |    public function getExpiresIn(): int;
                    |}
                """.trimMargin()
        )
    }
    
    private fun composerJson(): TemplateFile {
        return TemplateFile(relativePath = "composer.json",
                content = """
                    |{
                    |  "name": "commercetools/raml-sdk",
                    |  "license": "MIT",
                    |  "type": "library",
                    |  "description": "",
                    |  "autoload": {
                    |    "psr-4": {
                    |      "${packagePrefix.toNamespaceName().escapeAll()}\\": [
                    |        "src/"
                    |      ],
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
                    |    "cache/filesystem-adapter": "^1.0"
                    |  },
                    |  "require-dev": {
                    |    "monolog/monolog": "^1.3",
                    |    "phpunit/phpunit": "^8.0",
                    |    "vimeo/psalm": "^3.4",
                    |    "cache/array-adapter": "^1.0",
                    |    "squizlabs/php_codesniffer": "^3.0"
                    |  }
                    |}
                """.trimMargin())
    }
    
    private fun config(api: Api): TemplateFile {
        return TemplateFile(relativePath = "src/Client/Config.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName().escapeAll()}\\Client;
                    |
                    |class Config
                    |{
                    |    const API_URI = '${api.baseUri.template}';
                    |
                    |    const OPT_BASE_URI = 'base_uri';
                    |    const OPT_CLIENT_OPTIONS = 'options';
                    |    <<${if (api.baseUri.value.variables.isNotEmpty()) { api.baseUri.value.constVariables()} else ""}>>
                    |
                    |    /** @var string */
                    |    private $!apiUri;
                    |
                    |    /** @var array */
                    |    private $!clientOptions;
                    |
                    |    public function __construct(array $!config = [])
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
                    |    public function setApiUri(string $!apiUri): Config
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
                    |    public function setClientOptions(array $!options): Config
                    |    {
                    |        $!this->clientOptions = $!options;
                    |        return $!this;
                    |    }
                    |
                    |    public function getOptions(): array
                    |    {
                    |        return array_merge(
                    |            [self::OPT_BASE_URI => $!this->getApiUri()],
                    |            $!this->clientOptions
                    |        );
                    |    }
                    |}
                """.trimMargin().keepIndentation("<<", ">>").forcedLiteralEscape())
    }

    fun UriTemplate.replaceValues(): String = variables
            .map { """
                |/** @psalm-var string $!${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)} */
                |$!${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)} = isset($!config[self::OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}]) ? $!config[self::OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}] : '{$it}';
                |$!apiUri = str_replace('{$it}', $!${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)}, $!apiUri);""".trimMargin() }
            .joinToString(separator = "\n")

    fun UriTemplate.constVariables(): String = variables
            .map { "const OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}= '$it';" }
            .joinToString(separator = "\n")

    private fun authConfig(api: Api): TemplateFile {
        return TemplateFile(relativePath = "src/Client/AuthConfig.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |abstract class AuthConfig
                    |{
                    |    const AUTH_URI = '${api.authUri()}';
                    |
                    |    const OPT_BASE_URI = 'base_uri';
                    |    const OPT_AUTH_URI = 'auth_uri';
                    |    const OPT_CLIENT_OPTIONS = 'options';
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
                    |    public function setAuthUri(string $!authUri): AuthConfig
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
                    |    public function setClientOptions(array $!options): AuthConfig
                    |    {
                    |        $!this->clientOptions = $!options;
                    |        return $!this;
                    |    }
                    |
                    |    public function getOptions(): array
                    |    {
                    |        return array_merge(
                    |            [self::OPT_BASE_URI => $!this->authUri],
                    |            $!this->clientOptions
                    |        );
                    |    }
                    |    
                    |    abstract function getCacheKey(): string;
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun cachedProvider(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/CachedTokenProvider.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
                    |use GuzzleHttp\Client;
                    |use Psr\Cache\CacheItemPoolInterface;
                    |use Psr\Cache\CacheItemInterface;
                    |use Psr\SimpleCache\CacheInterface;
                    |
                    |class CachedTokenProvider implements TokenProvider
                    |{
                    |    const TOKEN_CACHE_KEY = 'access_token';
                    |    
                    |    /** @var TokenProvider */
                    |    private $!provider;
                    |
                    |    /** @var CacheItemPoolInterface|CacheInterface */
                    |    private $!cache;
                    |    
                    |    /** @var string */
                    |    private $!cacheKey;
                    |
                    |    /**
                    |     * @psalm-param CacheItemPoolInterface|CacheInterface|mixed $!cache
                    |     */
                    |    public function __construct(TokenProvider $!provider, $!cache, string $!cacheKey = null)
                    |    {
                    |       $!this->validateCache($!cache);
                    |       $!this->cache = $!cache;
                    |       $!this->provider = $!provider;
                    |       $!this->cacheKey = self::TOKEN_CACHE_KEY . "_" . ($!cacheKey ?? sha1(self::TOKEN_CACHE_KEY));
                    |    }
                    |
                    |    /**
                    |     * @psalm-assert CacheItemPoolInterface|CacheInterface $!cache
                    |     * @psalm-param CacheItemPoolInterface|CacheInterface|mixed $!cache
                    |     */
                    |    private function validateCache($!cache) : void
                    |    {
                    |       if (!$!cache instanceof CacheInterface && !$!cache instanceof CacheItemPoolInterface) {
                    |           throw new InvalidArgumentException();
                    |       }
                    |    }
                    |
                    |    /**
                    |     * @inheritDoc
                    |     */
                    |    public function getToken(): Token
                    |    {
                    |        $!item = null;
                    |
                    |        $!token = $!this->getCacheToken();
                    |        if (!is_null($!token)) {
                    |            return new TokenModel($!token);
                    |        }
                    |
                    |        return $!this->refreshToken();
                    |    }
                    |
                    |    /**
                    |     * @inheritDoc
                    |     */
                    |    public function refreshToken(): Token
                    |    {
                    |        $!token = $!this->provider->refreshToken();
                    |        // ensure token to be invalidated in cache before TTL
                    |        $!ttl = max(1, ($!token->getExpiresIn() - 300));
                    |
                    |        $!this->cache($!token, $!ttl);
                    |
                    |        return $!token;
                    |    }
                    |
                    |    private function getCacheToken(): ?string
                    |    {
                    |        $!cache = $!this->cache;
                    |        if ($!cache instanceof CacheInterface) {
                    |            /** @var ?string $!var */
                    |            $!var = $!cache->get($!this->cacheKey, null);
                    |            return $!var;
                    |        }
                    |        
                    |        $!item = $!cache->getItem($!this->cacheKey);
                    |        if ($!item->isHit()) {
                    |            return (string)$!item->get();
                    |        }
                    |        
                    |        return null;
                    |    }
                    |
                    |    private function cache(Token $!token, int $!ttl): void
                    |    {
                    |        $!cache = $!this->cache;
                    |        if ($!cache instanceof CacheInterface) {
                    |            $!cache->set($!this->cacheKey, $!token->getValue(), $!ttl);
                    |        } else {
                    |            $!item = $!cache->getItem($!this->cacheKey)->set($!token->getValue())->expiresAfter($!ttl);
                    |            $!cache->save($!item);
                    |        }
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun credentialTokenProvider() : TemplateFile {
        return TemplateFile(relativePath = "src/Client/ClientCredentialTokenProvider.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |use GuzzleHttp\Client;
                    |
                    |class ClientCredentialTokenProvider implements TokenProvider
                    |{
                    |    const GRANT_TYPE = 'grant_type';
                    |    const CLIENT_ID = 'clientId';
                    |    const CLIENT_SECRET = 'clientSecret';
                    |    const SCOPE = 'scope';
                    |    const ACCESS_TOKEN = 'access_token';
                    |    const EXPIRES_IN = 'expires_in';
                    |
                    |    /** @var Client */
                    |    private $!client;
                    |
                    |    /** @var ClientCredentialsConfig */
                    |    private $!authConfig;
                    |
                    |    public function __construct(Client $!client, ClientCredentialsConfig $!authConfig)
                    |    {
                    |        $!this->authConfig = $!authConfig;
                    |        $!this->client = $!client;
                    |    }
                    |
                    |    public function getToken(): Token
                    |    {
                    |        $!data = [
                    |            self::GRANT_TYPE => $!this->authConfig->getGrantType()
                    |        ];
                    |        if (!is_null($!this->authConfig->getScope())) {
                    |            $!data[self::SCOPE] = $!this->authConfig->getScope();
                    |        }
                    |        $!options = [
                    |            'form_params' => $!data,
                    |            'auth' => [$!this->authConfig->getClientId(), $!this->authConfig->getClientSecret()]
                    |        ];
                    |
                    |        $!result = $!this->client->post($!this->authConfig->getAuthUri(), $!options);
                    |
                    |        /** @var array $!body */
                    |        $!body = json_decode((string)$!result->getBody(), true);
                    |        return new TokenModel((string)$!body[self::ACCESS_TOKEN], (int)$!body[self::EXPIRES_IN]);
                    |    }
                    |
                    |    /**
                    |     * @return Token
                    |     */
                    |    public function refreshToken(): Token
                    |    {
                    |        return $!this->getToken();
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }
    
    private fun oauth2Handler(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/OAuth2Handler.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |use GuzzleHttp\Client;
                    |use Psr\Cache\CacheItemInterface;
                    |use Psr\Cache\CacheItemPoolInterface;
                    |use Psr\Http\Message\RequestInterface;
                    |
                    |class OAuth2Handler
                    |{
                    |    /** @var TokenProvider */
                    |    private $!provider;
                    |
                    |    /**
                    |     * OAuth2Handler constructor.
                    |     * @param TokenProvider $!provider
                    |     */
                    |    public function __construct(TokenProvider $!provider)
                    |    {
                    |        $!this->provider = $!provider;
                    |    }
                    |
                    |    public function __invoke(RequestInterface $!request, array $!options = []): RequestInterface
                    |    {
                    |        return $!request->withHeader('Authorization', $!this->getAuthorizationHeader());
                    |    }
                    |
                    |    public function getAuthorizationHeader(): string
                    |    {
                    |        return 'Bearer ' . $!this->provider->getToken()->getValue();
                    |    }
                    |
                    |    public function refreshToken(): Token
                    |    {
                    |        return $!this->provider->refreshToken();
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun middlewareFactory(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/MiddlewareFactory.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |use GuzzleHttp\MessageFormatter;
                    |use GuzzleHttp\Middleware;
                    |use GuzzleHttp\Promise\PromiseInterface;
                    |use Psr\Cache\CacheItemPoolInterface;
                    |use Psr\SimpleCache\CacheInterface;
                    |use Psr\Log\LoggerInterface;
                    |use Psr\Log\LogLevel;
                    |use Psr\Http\Message\RequestInterface;
                    |use Psr\Http\Message\ResponseInterface;
                    |
                    |class MiddlewareFactory
                    |{
                    |    /**
                    |     * @psalm-return array<string, callable>
                    |     * @psalm-param CacheItemPoolInterface|CacheInterface|null $!cache
                    |     */
                    |    public static function createDefaultMiddlewares(
                    |        LoggerInterface $!logger,
                    |        AuthConfig $!authConfig,
                    |        $!cache = null
                    |    ) {
                    |        $!handler = OAuthHandlerFactory::ofAuthConfig($!authConfig, $!cache);
                    |        return [
                    |            'oauth' => self::createMiddlewareForOAuthHandler($!handler),
                    |            'reauth' => self::createReauthenticateMiddleware($!handler),
                    |            'logger' => self::createLoggerMiddleware($!logger)
                    |        ];
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable()
                    |     */
                    |    public static function createMiddlewareForOAuthHandler(OAuth2Handler $!handler)
                    |    {
                    |        return Middleware::mapRequest($!handler);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param CacheItemPoolInterface|CacheInterface|null $!cache
                    |     * @psalm-return callable()
                    |     */
                    |    public static function createOAuthMiddleware(AuthConfig $!authConfig, $!cache = null)
                    |    {
                    |        $!handler = OAuthHandlerFactory::ofAuthConfig($!authConfig, $!cache);
                    |        return Middleware::mapRequest($!handler);
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable()
                    |     */
                    |    public static function createOAuthMiddlewareForProvider(TokenProvider $!provider)
                    |    {
                    |        $!handler = OAuthHandlerFactory::ofProvider($!provider);
                    |        return Middleware::mapRequest($!handler);
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable()
                    |     */
                    |    public static function createLoggerMiddleware(LoggerInterface $!logger, string $!logLevel = LogLevel::INFO, string $!template = MessageFormatter::CLF)
                    |    {
                    |        return Middleware::log($!logger, new MessageFormatter($!template), $!logLevel);
                    |    }
                    |
                    |    /**
                    |     * Middleware that reauthenticates on invalid token error
                    |     *
                    |     * @return callable Returns a function that accepts the next handler.
                    |     */
                    |    public static function createReauthenticateMiddleware(OAuth2Handler $!oauthHandler, int $!maxRetries = 1)
                    |    {
                    |        return
                    |            /**
                    |             * @psalm-param callable(RequestInterface, array{reauth: int}): PromiseInterface $!handler 
                    |             * @psalm-return callable(RequestInterface, array{reauth: int})
                    |             */
                    |            function (callable $!handler) use ($!oauthHandler, $!maxRetries) {
                    |                return
                    |                    /**
                    |                     * @psalm-return PromiseInterface
                    |                     * @psalm-param array{reauth: int} $!options
                    |                     */
                    |                    function (RequestInterface $!request, array $!options) use ($!handler, $!oauthHandler, $!maxRetries) {
                    |                        return $!handler($!request, $!options)->then(
                    |                            function (ResponseInterface $!response) use (
                    |                                $!request,
                    |                                $!handler,
                    |                                $!oauthHandler,
                    |                                $!options,
                    |                                $!maxRetries
                    |                            ) {
                    |                                if ($!response->getStatusCode() == 401) {
                    |                                    if (!isset($!options['reauth'])) {
                    |                                        $!options['reauth'] = 0;
                    |                                    }
                    |                                    if ($!options['reauth'] < $!maxRetries) {
                    |                                        $!options['reauth']++;
                    |                                        $!token = $!oauthHandler->refreshToken();
                    |                                        $!request = $!request->withHeader(
                    |                                            'Authorization',
                    |                                            'Bearer ' . $!token->getValue()
                    |                                        );
                    |                                        return $!handler($!request, $!options);
                    |                                    }
                    |                                }
                    |                                return $!response;
                    |                            }
                    |                        );
                    |                        return $!result;
                    |                    };
                    |            };
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun clientCredentialsConfig(api: Api): TemplateFile {
        return TemplateFile(relativePath = "src/Client/ClientCredentialsConfig.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |class ClientCredentialsConfig extends AuthConfig
                    |{
                    |    const AUTH_URI = '${api.authUri()}';
                    |
                    |    const CLIENT_ID = 'clientId';
                    |    const CLIENT_SECRET = 'clientSecret';
                    |    const SCOPE = 'scope';
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
                    |    public function setScope(string $!scope = null): ClientCredentialsConfig
                    |    {
                    |        $!this->scope = $!scope;
                    |        return $!this;
                    |    }
                    |
                    |    public function setClientId(string $!clientId): ClientCredentialsConfig
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
                    |    public function setClientSecret(string $!clientSecret): ClientCredentialsConfig
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

    private fun rawTokenProvider(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/RawTokenProvider.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |class RawTokenProvider implements TokenProvider
                    |{
                    |    const TOKEN = 'token';
                    |
                    |    /** @var Token */
                    |    private $!token;
                    |
                    |    public function __construct(Token $!token)
                    |    {
                    |        $!this->token = $!token;
                    |    }
                    |
                    |    public function getToken(): Token
                    |    {
                    |        return $!this->token;
                    |    }
                    |
                    |    public function refreshToken(): Token
                    |    {
                    |        return $!this->token;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun tokenModel(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/TokenModel.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |class TokenModel implements Token
                    |{
                    |    /** @var string */
                    |    private $!value;
                    |
                    |    /** @var int */
                    |    private $!expiresIn;
                    |
                    |    public function __construct(string $!value, int $!expiresIn = null)
                    |    {
                    |        $!this->value = $!value;
                    |        $!this->expiresIn = $!expiresIn ?? 0;
                    |    }
                    |
                    |    public function getValue(): string
                    |    {
                    |        return $!this->value;
                    |    }
                    |
                    |    public function getExpiresIn(): int
                    |    {
                    |        return $!this->expiresIn;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun oauthHandlerFactory(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/OAuthHandlerFactory.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
                    |use Cache\Adapter\Filesystem\FilesystemCachePool;
                    |use GuzzleHttp\Client;
                    |use League\Flysystem\Adapter\Local;
                    |use League\Flysystem\Filesystem;
                    |use Psr\Cache\CacheItemPoolInterface;
                    |use Psr\SimpleCache\CacheInterface;
                    |
                    |class OAuthHandlerFactory
                    |{
                    |    /**
                    |     * @psalm-param CacheItemPoolInterface|CacheInterface|null $!cache
                    |     * @psalm-return CacheItemPoolInterface|CacheInterface
                    |     */
                    |    private static function validateCache($!cache = null)
                    |    {
                    |        if ($!cache instanceof CacheItemPoolInterface || $!cache instanceof CacheInterface) {
                    |            return $!cache;
                    |        }
                    |
                    |        $!filesystemAdapter = new Local(getcwd());
                    |        $!filesystem        = new Filesystem($!filesystemAdapter);
                    |        $!cache = new FilesystemCachePool($!filesystem);
                    |        
                    |        return $!cache;
                    |    }
                    |
                    |    /**
                    |     * @psalm-param CacheItemPoolInterface|CacheInterface|null $!cache
                    |     */
                    |    public static function ofAuthConfig(AuthConfig $!authConfig, $!cache = null): OAuth2Handler
                    |    {
                    |        $!cache = self::validateCache($!cache);
                    |        switch(true) {
                    |           case $!authConfig instanceof ClientCredentialsConfig:
                    |               $!provider = new CachedTokenProvider(
                    |                   new ClientCredentialTokenProvider(
                    |                       new Client($!authConfig->getClientOptions()),
                    |                       $!authConfig
                    |                   ),
                    |                   $!cache,
                    |                   $!authConfig->getCacheKey()
                    |               );
                    |               break;
                    |           default:
                    |               throw new InvalidArgumentException('Unknown authorization configuration');
                    |
                    |        }
                    |        return self::ofProvider($!provider);
                    |    }
                    |
                    |    public static function ofProvider(TokenProvider $!provider): OAuth2Handler
                    |    {
                    |        return new OAuth2Handler($!provider);
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun baseException(): TemplateFile {
        return TemplateFile(relativePath = "src/Exception/BaseException.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Exception;
                    |
                    |use Exception;
                    |
                    |abstract class BaseException extends Exception
                    |{
                    |}
                """.trimMargin())
    }

    private fun invalidArgumentException(): TemplateFile {
        return TemplateFile(relativePath = "src/Exception/InvalidArgumentException.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Exception;
                    |
                    |use Exception;
                    |
                    |class InvalidArgumentException extends BaseException
                    |{
                    |}
                """.trimMargin())
    }

    private fun apiServerException(): TemplateFile {
        return TemplateFile(relativePath = "src/Exception/ApiServerException.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Exception;
                    |
                    |use GuzzleHttp\Exception\ServerException;
                    |use Psr\Http\Message\RequestInterface;
                    |use Psr\Http\Message\ResponseInterface;
                    |use ${packagePrefix.toNamespaceName()}\Base\JsonObject;
                    |
                    |class ApiServerException extends ServerException
                    |{
                    |    /**
                    |     * @var ?JsonObject
                    |     */
                    |    private $!result;
                    |    
                    |    /**
                    |     * @param string $!message
                    |     * @param ?JsonObject $!result
                    |     */
                    |    public function __construct($!message, $!result, RequestInterface $!request, ResponseInterface $!response = null, \Exception $!previous = null, array $!handlerContext = [])
                    |    {
                    |        $!this->result = $!result;
                    |        parent::__construct($!message, $!request, $!response, $!previous, $!handlerContext);
                    |    }
                    |
                    |    /**
                    |     * @return ?JsonObject
                    |     */
                    |    public function getResult()
                    |    {
                    |        return $!this->result;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun apiClientException(): TemplateFile {
        return TemplateFile(relativePath = "src/Exception/ApiClientException.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Exception;
                    |
                    |use GuzzleHttp\Exception\ClientException;
                    |use Psr\Http\Message\RequestInterface;
                    |use Psr\Http\Message\ResponseInterface;
                    |use ${packagePrefix.toNamespaceName()}\Base\JsonObject;
                    |
                    |class ApiClientException extends ClientException
                    |{
                    |    /**
                    |     * @var ?JsonObject
                    |     */
                    |    private $!result;
                    |    
                    |    /**
                    |     * @param string $!message
                    |     * @param ?JsonObject $!result
                    |     */
                    |    public function __construct($!message, $!result, RequestInterface $!request, ResponseInterface $!response = null, \Exception $!previous = null, array $!handlerContext = [])
                    |    {
                    |        $!this->result = $!result;
                    |        parent::__construct($!message, $!request, $!response, $!previous, $!handlerContext);
                    |    }
                    |    
                    |    /**
                    |     * @return ?JsonObject
                    |     */
                    |    public function getResult()
                    |    {
                    |        return $!this->result;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun apiRequest(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/ApiRequest.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
                    |use GuzzleHttp\Client;
                    |use GuzzleHttp\Psr7;
                    |use GuzzleHttp\Psr7\Request;
                    |use Psr\Http\Message\ResponseInterface;
                    |
                    |/** @psalm-suppress PropertyNotSetInConstructor */
                    |class ApiRequest extends Request
                    |{
                    |    /** @psalm-var array<string, scalar[]> */
                    |    private $!queryParts;
                    |    /** @psalm-var string */
                    |    private $!query;
                    |    /** @var Client|null */
                    |    private $!client;
                    |
                    |    /**
                    |     * @psalm-param array<string, scalar|scalar[]> $!headers
                    |     * @param string|null|resource|\Psr\Http\Message\StreamInterface $!body
                    |     */
                    |    public function __construct(?Client $!client, string $!method, string $!uri, array $!headers = [], $!body = null, string $!version = '1.1')
                    |    {
                    |        $!this->client = $!client;
                    |        $!headers = $!this->ensureHeader($!headers, 'Content-Type', 'application/json');
                    |
                    |        parent::__construct($!method, $!uri, $!headers, $!body, $!version);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param array<string, scalar|scalar[]> $!headers
                    |     * @psalm-param string|string[] $!defaultValue
                    |     * @psalm-return array<string, scalar|scalar[]>
                    |     * @param array $!headers
                    |     * @param string $!header
                    |     * @param scalar|array $!defaultValue
                    |     * @return array
                    |     */
                    |    protected function ensureHeader(array $!headers, string $!header, $!defaultValue): array
                    |    {
                    |        $!normalizedHeader = strtolower($!header);
                    |        foreach ($!headers as $!headerName => $!value) {
                    |            $!normalized = strtolower($!headerName);
                    |            if ($!normalized !== $!normalizedHeader) {
                    |                continue;
                    |            }
                    |            return $!headers;
                    |        }
                    |        $!headers[$!header] = $!defaultValue;
                    |
                    |        return $!headers;
                    |    }
                    |
                    |    /**
                    |     * @param string $!parameterName
                    |     * @psalm-param scalar $!value
                    |     * @param mixed $!value
                    |     * @psalm-return static
                    |     */
                    |    public function withQueryParam(string $!parameterName, $!value): ApiRequest
                    |    {
                    |        $!query = $!this->getUri()->getQuery();
                    |        if ($!this->query !== $!query) {
                    |            /** @var array<string, scalar[]> */
                    |            $!this->queryParts = array_map(
                    |                /**
                    |                 * @psalm-param scalar|scalar[] $!value
                    |                 * @psalm-return scalar[]
                    |                 */
                    |                function($!value): array {
                    |                    if(is_array($!value)) {
                    |                        return $!value;
                    |                    }
                    |                    return [$!value];
                    |                },
                    |                Psr7\parse_query($!query)
                    |            );
                    |        }
                    |        $!this->queryParts[$!parameterName][] = $!value;
                    |        ksort($!this->queryParts);
                    |        $!this->query = Psr7\build_query($!this->queryParts);
                    |
                    |        return $!this->withUri($!this->getUri()->withQuery($!this->query));
                    |    }
                    |    
                    |    /**
                    |     * @param array $!options
                    |     * @return ResponseInterface
                    |     * @throws InvalidArgumentException
                    |     * @throws \GuzzleHttp\Exception\GuzzleException
                    |     */
                    |    public function send(array $!options = [])
                    |    {
                    |        if (is_null($!this->client)) {
                    |           throw new InvalidArgumentException();
                    |        }
                    |        return $!this->client->send($!this, $!options);
                    |    }
                    |    
                    |    public function getClient(): ?Client
                    |    {
                    |       return $!this->client;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun mapperIterator(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/MapperIterator.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |class MapperIterator extends \IteratorIterator
                    |{
                    |    /**
                    |     * @var callable
                    |     */
                    |    private $!mapper;
                    |
                    |    public function __construct(\Traversable $!iterator, callable $!mapper)
                    |    {
                    |        parent::__construct($!iterator);
                    |        $!this->mapper = $!mapper;
                    |    }
                    |
                    |    public function current()
                    |    {
                    |        return call_user_func($!this->mapper, parent::current(), parent::key());
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun jsonObjectCollection(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/JsonObjectCollection.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
                    |
                    |/**
                    | * @extends MapCollection<JsonObject>
                    | * @method JsonObject current()
                    | * @method JsonObject at($!offset)
                    | */
                    |class JsonObjectCollection extends MapCollection
                    |{
                    |    /**
                    |     * @psalm-assert JsonObject $!value
                    |     * @psalm-param JsonObject|object $!value
                    |     * @return JsonObjectCollection
                    |     * @throws InvalidArgumentException
                    |     */
                    |    public function add($!value)
                    |    {
                    |        if (!$!value instanceof JsonObject) {
                    |            throw new InvalidArgumentException();
                    |        }
                    |        $!this->store($!value);
                    |
                    |        return $!this;
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable(int):?JsonObject
                    |     */
                    |    protected function mapper()
                    |    {
                    |        return function(int $!index): ?JsonObject {
                    |            $!data = $!this->get($!index);
                    |            if (!is_null($!data) && !$!data instanceof JsonObject) {
                    |                $!data = new JsonObjectModel($!data);
                    |                $!this->set($!data, $!index);
                    |            }
                    |            return $!data;
                    |        };
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }
    private fun mapCollection(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/MapCollection.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |/**
                    | * @template T
                    | */
                    |abstract class MapCollection implements Collection, \ArrayAccess, \JsonSerializable, \IteratorAggregate
                    |{
                    |    /** @psalm-var ?array<int, T|object> */
                    |    private $!data;
                    |    /** @var array<string, array<string, int>> */
                    |    private $!indexes = [];
                    |    /** @var MapperIterator */
                    |    private $!iterator;
                    |
                    |    /**
                    |     * @psalm-param ?array<int, T|object> $!data
                    |     * @param array|null $!data
                    |     */
                    |    public function __construct(array $!data = null)
                    |    {
                    |        if (!is_null($!data)) {
                    |            $!this->index($!data);
                    |        }
                    |        $!this->data = $!data;
                    |        $!this->iterator = $!this->getIterator();
                    |    }
                    |
                    |    public function jsonSerialize(): ?array
                    |    {
                    |        return $!this->data;
                    |    }
                    |
                    |    public function isPresent(): bool
                    |    {
                    |        return !is_null($!this->data);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param array<int, T|object> $!data
                    |     * @return static
                    |     */
                    |    public static function fromArray(array $!data)
                    |    {
                    |        return new static($!data);
                    |    }
                    |
                    |    /**
                    |     * @param mixed $!data
                    |     */
                    |    protected function index($!data): void
                    |    {
                    |    }
                    |
                    |    /**
                    |     * @psalm-return T|object|null
                    |     */
                    |    final protected function get(int $!index)
                    |    {
                    |        if (isset($!this->data[$!index])) {
                    |            return $!this->data[$!index];
                    |        }
                    |        return null;
                    |    }
                    |
                    |    /**
                    |     * @psalm-param T|object $!data
                    |     */
                    |    final protected function set($!data, ?int $!index): void
                    |    {
                    |        if (is_null($!index)) {
                    |            $!this->data[] = $!data;
                    |        } else {
                    |            $!this->data[$!index] = $!data;
                    |        }
                    |    }
                    |
                    |    /**
                    |     * @psalm-param T|object $!value
                    |     * @param $!value
                    |     * @return Collection
                    |     */
                    |    public function add($!value)
                    |    {
                    |        return $!this->store($!value);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param T|object $!value
                    |     * @param $!value
                    |     * @return Collection
                    |     */
                    |    final protected function store($!value)
                    |    {
                    |        $!this->set($!value, null);
                    |        $!this->iterator = $!this->getIterator();
                    |
                    |        return $!this;
                    |    }
                    |
                    |    /**
                    |     * @psalm-return ?T
                    |     */
                    |    public function at(int $!index)
                    |    {
                    |        return $!this->mapper()($!index);
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable(int): ?T
                    |     */
                    |    abstract protected function mapper();
                    |
                    |    /**
                    |     * @psalm-param T|object $!value
                    |     */
                    |    final protected function addToIndex(string $!field, string $!key, int $!index): void
                    |    {
                    |        $!this->indexes[$!field][$!key] = $!index;
                    |    }
                    |
                    |    /**
                    |     * @psalm-return ?T
                    |     */
                    |    final protected function valueByKey(string $!field, string $!key)
                    |    {
                    |        return isset($!this->indexes[$!field][$!key]) ? $!this->at($!this->indexes[$!field][$!key]) : null;
                    |    }
                    |
                    |    public function getIterator(): MapperIterator
                    |    {
                    |        $!keys = !is_null($!this->data) ? array_keys($!this->data) : [];
                    |        $!keyIterator = new \ArrayIterator(array_combine($!keys, $!keys));
                    |        $!iterator = new MapperIterator(
                    |            $!keyIterator,
                    |            $!this->mapper()
                    |        );
                    |        $!iterator->rewind();
                    |
                    |        return $!iterator;
                    |    }
                    |
                    |    /**
                    |     * @return ?T
                    |     */
                    |    public function current()
                    |    {
                    |        /** @psalm-var ?T $!current  */
                    |        $!current = $!this->iterator->current();
                    |        return $!current;
                    |    }
                    |
                    |    /**
                    |     * @return void
                    |     */
                    |    public function next()
                    |    {
                    |        $!this->iterator->next();
                    |    }
                    |
                    |    /**
                    |     * @return int
                    |     */
                    |    public function key()
                    |    {
                    |        /** @var int $!key  */
                    |        $!key = $!this->iterator->key();
                    |        return $!key;
                    |    }
                    |
                    |    /**
                    |     * @return bool
                    |     */
                    |    public function valid()
                    |    {
                    |        return $!this->iterator->valid();
                    |    }
                    |
                    |    /**
                    |     * @return void
                    |     */
                    |    public function rewind()
                    |    {
                    |        $!this->iterator->rewind();
                    |    }
                    |
                    |    /**
                    |     * @param int $!offset
                    |     * @return bool
                    |     */
                    |    public function offsetExists($!offset)
                    |    {
                    |        return !is_null($!this->data) && array_key_exists($!offset, $!this->data);
                    |    }
                    |
                    |    /**
                    |     * @param int $!offset
                    |     * @return ?T
                    |     */
                    |    public function offsetGet($!offset)
                    |    {
                    |        return $!this->at($!offset);
                    |    }
                    |
                    |    /**
                    |     * @param int $!offset
                    |     * @psalm-param T|object $!value
                    |     * @param mixed $!value
                    |     * @return void
                    |     */
                    |    public function offsetSet($!offset, $!value)
                    |    {
                    |        $!this->set($!value, $!offset);
                    |        $!this->iterator = $!this->getIterator();
                    |    }
                    |
                    |    /**
                    |     * @param int $!offset
                    |     * @return void
                    |     */
                    |    public function offsetUnset($!offset)
                    |    {
                    |        if ($!this->offsetExists($!offset)) {
                    |            /** @psalm-suppress PossiblyNullArrayAccess */
                    |            unset($!this->data[$!offset]);
                    |            $!this->iterator = $!this->getIterator();
                    |        }
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
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

    private fun resource(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/ApiResource.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |use GuzzleHttp\Client;
                    |
                    |class ApiResource
                    |{
                    |    /**
                    |     * @var string
                    |     */
                    |    private $!uri;
                    |
                    |    /**
                    |     * @psalm-var array<string, scalar>
                    |     */
                    |    private $!args = [];
                    |
                    |    /**
                    |     * @var ?Client
                    |     */
                    |    private $!client;
                    |
                    |    /**
                    |     * @param string $!uri
                    |     * @psalm-param array<string, scalar> $!args
                    |     */
                    |    public function __construct(string $!uri = '', array $!args = [], Client $!client = null)
                    |    {
                    |        $!this->uri = $!uri;
                    |        $!this->args = $!args;
                    |        $!this->client = $!client;
                    |    }
                    |
                    |    /**
                    |     * @return string
                    |     */
                    |    final protected function getUri(): string
                    |    {
                    |        return $!this->uri;
                    |    }
                    |
                    |    /**
                    |     * @psalm-return array<string, scalar>
                    |     */
                    |    final protected function getArgs(): array
                    |    {
                    |        return $!this->args;
                    |    }
                    |
                    |    public function getClient(): ?Client
                    |    {
                    |       return $!this->client;
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }
}

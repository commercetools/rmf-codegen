package io.vrap.codegen.languages.php.base

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

class PhpBaseFileProducer @Inject constructor(val api: Api) : FileProducer {

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
            config(),
            credentialTokenProvider(),
            cachedProvider(),
            rawTokenProvider(),
            oauth2Handler(),
            middlewareFactory(),
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
            mapperInterface(),
            mapperSequence(),
            mapperScalarSequence(),
            mapperMap(),
            psalm(),
            resource(),
            resultMapper(),
            mapperInterface(),
            jsonObjectCollection(),
            dateTimeImmutableCollection(),
            apiClientException(),
            apiServerException(),
            authConfig(),
            clientCredentialsConfig(),
            builder()
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
                    |use stdClass;
                    |use ReflectionClass;
                    |use ReflectionParameter;
                    |use ReflectionException;
                    |
                    |class ResultMapper implements MapperInterface
                    |{
                    |    /**
                    |     * @template T of JsonObject
                    |     * @psalm-param class-string<T> $!type
                    |     * @psalm-return T
                    |     */
                    |    public function mapResponseToClass(string $!type, ResponseInterface $!response)
                    |    {
                    |        return $!type::of($!this->responseData($!response));
                    |    }
                    |
                    |    /**
                    |     * @psalm-return stdClass
                    |     */
                    |    private function responseData(ResponseInterface $!response)
                    |    {
                    |        $!body = (string)$!response->getBody();
                    |        /** @psalm-var ?stdClass $!data */
                    |        $!data = json_decode($!body);
                    |        if (is_null($!data)) {
                    |           throw new InvalidArgumentException();
                    |        }
                    |        return $!data;
                    |    }
                    |    
                    |    /**
                    |     * @template T
                    |     * @psalm-param class-string<T> $!type
                    |     * @psalm-param array<string, mixed> $!data
                    |     * @psalm-return T
                    |     * @throws InvalidArgumentException
                    |     * @throws ReflectionException
                    |     */
                    |    public function mapToConstructor(string $!type, array $!data) {
                    |        $!typeClass = new ReflectionClass($!type);
                    |        $!constructor = $!typeClass->getConstructor();
                    |        if (is_null($!constructor)) {
                    |            throw new InvalidArgumentException();
                    |        }
                    |        $!params = $!constructor->getParameters();
                    |
                    |        /** @var array<int, mixed> $!args */
                    |        $!args = array_map(
                    |            function (ReflectionParameter $!param) use ($!data) {
                    |                return ($!data[$!param->name] ?? null);
                    |            },
                    |            $!params
                    |        );
                    |        return $!typeClass->newInstanceArgs($!args);
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
                    |    public function mapResponseToClass(string $!type, ResponseInterface $!response);
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
                    |    const TIME_FORMAT = "H:i:s.u";
                    |    const DATE_FORMAT = "Y-m-d";
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
                    |use stdClass;
                    |
                    |class JsonObjectModel extends BaseJsonObject implements JsonObject
                    |{
                    |    /**
                    |     * @psalm-return scalar|array<int|string, mixed>|JsonObject|JsonObjectCollection|null
                    |     */
                    |    final public function get(string $!field)
                    |    {
                    |        $!data = $!this->raw($!field);
                    |        if ($!data instanceof stdClass) {
                    |            return JsonObjectModel::of($!data);
                    |        }
                    |        if (is_array($!data) && isset($!data[0]) && $!data[0] instanceof stdClass) {
                    |            /** @psalm-var ?array<int, stdClass> $!data */
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
                    |use stdClass;
                    |
                    |interface JsonObject extends \JsonSerializable
                    |{
                    |    /**
                    |     * @psalm-return scalar|array<int|string, mixed>|JsonObject|JsonObjectCollection|null
                    |     */
                    |    public function get(string $!field);
                    |    
                    |    /**
                    |     * @psalm-param stdClass|array<string, mixed>|null $!data
                    |     * @psalm-return static
                    |     */
                    |    public static function of($!data = null);
                    |
                    |    /**
                    |     * @psalm-param array<string, mixed> $!data
                    |     * @psalm-return static
                    |     */
                    |    public static function fromArray(array $!data = []);
                    |
                    |    /**
                    |     * @psalm-param ?stdClass $!data
                    |     * @psalm-return static
                    |     */
                    |    public static function fromStdClass(stdClass $!data = null);
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
                    |use stdClass;
                    |
                    |abstract class BaseJsonObject implements JsonObject
                    |{
                    |    /** @var ?stdClass */
                    |    private $!rawData;
                    |
                    |    /**
                    |     * @psalm-param ?stdClass|array<string, mixed> $!data
                    |     * @return static
                    |     */
                    |    final public static function of($!data = null)
                    |    {
                    |        if (is_array($!data)) {
                    |            return self::fromArray($!data);
                    |        }
                    |        return self::fromStdClass($!data);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param ?stdClass $!data
                    |     * @return static
                    |     */
                    |    final public static function fromStdClass(stdClass $!data = null)
                    |    {
                    |        $!t = new static();
                    |        $!t->rawData = $!data;
                    |        return $!t;
                    |    }
                    |
                    |    /**
                    |     * @psalm-param array<string, mixed> $!data
                    |     * @return static
                    |     */
                    |    final public static function fromArray(array $!data = [])
                    |    {
                    |        return static::of((object)$!data);
                    |    }
                    |
                    |    /**
                    |     * @psalm-return scalar|array<int, mixed>|array<string, mixed>|stdClass|null
                    |     */
                    |    final protected function raw(string $!field)
                    |    {
                    |        if (isset($!this->rawData->$!field)) {
                    |            /**
                    |             * @psalm-suppress PossiblyNullPropertyFetch
                    |             * @psalm-var scalar|array<int, mixed>|array<string, mixed>|stdClass|null
                    |             */
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

    private fun builder(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/Builder.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |/**
                    | * @template T
                    | */
                    |interface Builder
                    |{
                    |    /**
                    |     * @psalm-return T
                    |     */
                    |    public function build();
                    |}
                """.trimMargin())
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
                    |     * @psalm-param CacheItemPoolInterface|CacheInterface|null $!cache
                    |     * @psalm-param array<string, callable> $!middlewares
                    |     * @throws InvalidArgumentException
                    |     */
                    |    public function createGuzzleClient(Config $!config, AuthConfig $!authConfig, LoggerInterface $!logger, $!cache = null, array $!middlewares = []): HttpClient
                    |    {
                    |        $!middlewares = array_merge(
                    |           MiddlewareFactory::createDefaultMiddlewares($!logger, $!authConfig, $!cache),
                    |           $!middlewares
                    |        );
                    |        return $!this->createGuzzle6Client($!config->getOptions(), $!middlewares);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param array<string, callable> $!middlewares
                    |     * @throws InvalidArgumentException
                    |     */
                    |    public function createGuzzleClientForMiddlewares(
                    |       Config $!config,
                    |       array $!middlewares = []): HttpClient
                    |    {
                    |        return $!this->createGuzzle6Client($!config->getOptions(), $!middlewares);
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
                    |        $!options = array_replace(
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
                    |  "name": "${packagePrefix.toLowerCase()}/raml-base",
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

    fun UriTemplate.replaceValues(): String = variables
            .map { """
                |/** @psalm-var string $!${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)} */
                |$!${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)} = isset($!config[self::OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}]) ? $!config[self::OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}] : '{$it}';
                |$!apiUri = str_replace('{$it}', $!${StringCaseFormat.LOWER_CAMEL_CASE.apply(it)}, $!apiUri);""".trimMargin() }
            .joinToString(separator = "\n")

    fun UriTemplate.constVariables(): String = variables
            .map { "const OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}= '$it';" }
            .joinToString(separator = "\n")

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
                    |use ${packagePrefix.toNamespaceName()}\Base\JsonObjectModel;
                    |use ${packagePrefix.toNamespaceName()}\Base\ResultMapper;
                    |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
                    |use GuzzleHttp\Client;
                    |use GuzzleHttp\Exception\GuzzleException;
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
                    |     * @throws GuzzleException
                    |     * @psalm-suppress InvalidThrow
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
                    | * @extends MapperSequence<JsonObject>
                    | * @method JsonObject current()
                    | * @method JsonObject at($!offset)
                    | */
                    |class JsonObjectCollection extends MapperSequence
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
                    |                $!data = JsonObjectModel::of($!data);
                    |                $!this->set($!data, $!index);
                    |            }
                    |            return $!data;
                    |        };
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun dateTimeImmutableCollection(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/DateTimeImmutableCollection.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |use DateTimeImmutable;
                    |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
                    |
                    |/**
                    | * @extends MapperScalarSequence<DateTimeImmutable>
                    | * @method DateTimeImmutable current()
                    | * @method DateTimeImmutable at($!offset)
                    | */
                    |class DateTimeImmutableCollection extends MapperScalarSequence
                    |{
                    |    /**
                    |     * @psalm-assert DateTimeImmutable $!value
                    |     * @psalm-param DateTimeImmutable|scalar $!value
                    |     * @return DateTimeImmutableCollection
                    |     * @throws InvalidArgumentException
                    |     */
                    |    public function add($!value)
                    |    {
                    |        if (!$!value instanceof DateTimeImmutable) {
                    |            throw new InvalidArgumentException();
                    |        }
                    |        $!this->store($!value);
                    |
                    |        return $!this;
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable(int):?DateTimeImmutable
                    |     */
                    |    protected function mapper()
                    |    {
                    |        return function(int $!index): ?DateTimeImmutable {
                    |            $!data = $!this->get($!index);
                    |            if (!is_null($!data) && !$!data instanceof DateTimeImmutable) {
                    |                $!data = new DateTimeImmutable((string)$!data);
                    |                $!this->set($!data, $!index);
                    |            }
                    |            return $!data;
                    |        };
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun mapperSequence(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/MapperSequence.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |use stdClass;
                    |
                    |/**
                    | * @template T
                    | */
                    |abstract class MapperSequence implements Collection, \ArrayAccess, \JsonSerializable, \IteratorAggregate
                    |{
                    |    /** @psalm-var ?array<int, T|stdClass> */
                    |    private $!data;
                    |    /** @var array<string, array<string, int>> */
                    |    private $!indexes = [];
                    |    /** @var MapperIterator */
                    |    private $!iterator;
                    |
                    |    /**
                    |     * @psalm-param ?array<int, T|stdClass> $!data
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
                    |    public function toArray(): ?array
                    |    {
                    |        return $!this->data;
                    |    }
                    |    
                    |    public function jsonSerialize(): ?array
                    |    {
                    |        return $!this->data;
                    |    }
                    |
                    |    /**
                    |     * @psalm-param array<int, T|stdClass> $!data
                    |     * @return static
                    |     */
                    |    final public static function fromArray(array $!data)
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
                    |     * @psalm-return T|stdClass|null
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
                    |     * @psalm-param T|stdClass $!data
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
                    |     * @psalm-param T|stdClass $!value
                    |     * @param $!value
                    |     * @return Collection
                    |     */
                    |    public function add($!value)
                    |    {
                    |        return $!this->store($!value);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param T|stdClass $!value
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
                    |     * @psalm-param T|stdClass $!value
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

    private fun mapperScalarSequence(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/MapperScalarSequence.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |use stdClass;
                    |
                    |/**
                    | * @template T
                    | */
                    |abstract class MapperScalarSequence implements Collection, \ArrayAccess, \JsonSerializable, \IteratorAggregate
                    |{
                    |    /** @psalm-var ?array<int, T|scalar> */
                    |    private $!data;
                    |    /** @var array<string, array<string, int>> */
                    |    private $!indexes = [];
                    |    /** @var MapperIterator */
                    |    private $!iterator;
                    |
                    |    /**
                    |     * @psalm-param ?array<int, T|scalar> $!data
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
                    |    public function toArray(): ?array
                    |    {
                    |        return $!this->data;
                    |    }
                    |    
                    |    public function jsonSerialize(): ?array
                    |    {
                    |        return $!this->data;
                    |    }
                    |
                    |    /**
                    |     * @psalm-param array<int, T|scalar> $!data
                    |     * @return static
                    |     */
                    |    final public static function fromArray(array $!data)
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
                    |     * @psalm-return T|scalar|null
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
                    |     * @psalm-param T|scalar $!data
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
                    |     * @psalm-param T|scalar $!value
                    |     * @param $!value
                    |     * @return Collection
                    |     */
                    |    public function add($!value)
                    |    {
                    |        return $!this->store($!value);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param T|scalar $!value
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
                    |     * @psalm-param T|scalar $!value
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

    private fun mapperMap(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/MapperMap.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Base;
                    |
                    |use stdClass;
                    |
                    |/**
                    | * @template T
                    | */
                    |abstract class MapperMap implements Collection, \ArrayAccess, \JsonSerializable, \IteratorAggregate
                    |{
                    |    /** @psalm-var ?array<string, T|stdClass> */
                    |    private $!data;
                    |    /** @var array<string, array<string, string>> */
                    |    private $!indexes = [];
                    |    /** @var MapperIterator */
                    |    private $!iterator;
                    |
                    |    /**
                    |     * @psalm-param ?array<string, T|stdClass> $!data
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
                    |    /**
                    |     * @psalm-param ?stdClass|array<string, T|stdClass> $!data
                    |     * @return static
                    |     */
                    |    final public static function of($!data = null)
                    |    {
                    |        if (is_array($!data)) {
                    |            return self::fromArray($!data);
                    |        }
                    |        /** @var stdClass $!data) */
                    |        return self::fromStdClass($!data);
                    |    }
                    |
                    |    /**
                    |     * @psalm-return array<string, stdClass|mixed>
                    |     */
                    |    public function toArray(): ?array
                    |    {
                    |        return $!this->data;
                    |    }
                    |
                    |    /**
                    |     * @psalm-return array<string, stdClass|mixed>
                    |     */
                    |    public function jsonSerialize(): ?array
                    |    {
                    |        return $!this->data;
                    |    }
                    |
                    |    /**
                    |     * @psalm-param ?stdClass $!data
                    |     * @psalm-return static
                    |     */
                    |    final public static function fromStdClass(stdClass $!data = null)
                    |    {
                    |        /** @var array<string, T|stdClass> $!t */
                    |        $!t = (array)$!data;
                    |        return new static($!t);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param array<string, T|stdClass> $!data
                    |     * @return static
                    |     */
                    |    final public static function fromArray(array $!data)
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
                    |     * @psalm-return T|stdClass|null
                    |     */
                    |    final protected function get(string $!key)
                    |    {
                    |        if (isset($!this->data[$!key])) {
                    |            return $!this->data[$!key];
                    |        }
                    |        return null;
                    |    }
                    |
                    |    /**
                    |     * @psalm-param T|stdClass $!data
                    |     */
                    |    final protected function set($!data, string $!key): void
                    |    {
                    |        $!this->data[$!key] = $!data;
                    |    }
                    |
                    |    /**
                    |     * @psalm-param T|stdClass $!value
                    |     * @param $!value
                    |     * @return $!this
                    |     */
                    |    public function put(string $!key, $!value)
                    |    {
                    |        return $!this->store($!key, $!value);
                    |    }
                    |
                    |    /**
                    |     * @psalm-param T|stdClass $!value
                    |     * @param string $!key
                    |     * @param $!value
                    |     * @return $!this
                    |     */
                    |    final protected function store(string $!key, $!value)
                    |    {
                    |        $!this->set($!value, $!key);
                    |        $!this->iterator = $!this->getIterator();
                    |
                    |        return $!this;
                    |    }
                    |
                    |    /**
                    |     * @psalm-return ?T
                    |     */
                    |    public function at(string $!key)
                    |    {
                    |        return $!this->mapper()($!key);
                    |    }
                    |
                    |    /**
                    |     * @psalm-return callable(string): ?T
                    |     */
                    |    abstract protected function mapper();
                    |
                    |    /**
                    |     * @psalm-param T|stdClass $!value
                    |     */
                    |    final protected function addToIndex(string $!field, string $!key, string $!indexKey): void
                    |    {
                    |        $!this->indexes[$!field][$!key] = $!indexKey;
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
                    |     * @return string
                    |     */
                    |    public function key()
                    |    {
                    |        /** @var string $!key  */
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
                    |     * @param string $!offset
                    |     * @return bool
                    |     */
                    |    public function offsetExists($!offset)
                    |    {
                    |        return !is_null($!this->data) && array_key_exists($!offset, $!this->data);
                    |    }
                    |
                    |    /**
                    |     * @param string $!offset
                    |     * @return ?T
                    |     */
                    |    public function offsetGet($!offset)
                    |    {
                    |        return $!this->at($!offset);
                    |    }
                    |
                    |    /**
                    |     * @param string $!offset
                    |     * @psalm-param T|stdClass $!value
                    |     * @param mixed $!value
                    |     * @return void
                    |     */
                    |    public function offsetSet($!offset, $!value)
                    |    {
                    |        $!this->store($!offset, $!value);
                    |    }
                    |
                    |    /**
                    |     * @param string $!offset
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

    private fun authConfig(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/AuthConfig.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |interface AuthConfig
                    |{
                    |    const OPT_BASE_URI = 'base_uri';
                    |    const OPT_AUTH_URI = 'auth_uri';
                    |    const OPT_CLIENT_OPTIONS = 'options';
                    |
                    |    public function getGrantType(): string;
                    |
                    |    public function getAuthUri(): string;
                    |
                    |    public function setAuthUri(string $!authUri): AuthConfig;
                    |
                    |    public function getClientOptions(): array;
                    |
                    |    public function setClientOptions(array $!options): AuthConfig;
                    |
                    |    public function getOptions(): array;
                    |    
                    |    public function getCacheKey(): string;
                    |}
                """.trimMargin().forcedLiteralEscape())
    }

    private fun clientCredentialsConfig(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/ClientCredentialsConfig.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |interface ClientCredentialsConfig extends AuthConfig
                    |{
                    |    const CLIENT_ID = 'clientId';
                    |    const CLIENT_SECRET = 'clientSecret';
                    |    const SCOPE = 'scope';
                    |
                    |    public function getClientId(): string;
                    |
                    |    public function getScope(): ?string;
                    |
                    |    public function setScope(string $!scope = null): ClientCredentialsConfig;
                    |
                    |    public function setClientId(string $!clientId): ClientCredentialsConfig;
                    |
                    |    public function getClientSecret(): string;
                    |
                    |    public function setClientSecret(string $!clientSecret): ClientCredentialsConfig;
                    |
                    |    public function getCacheKey(): string;
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun config(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/Config.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName().escapeAll()}\Client;
                    |
                    |interface Config
                    |{
                    |    const OPT_BASE_URI = 'base_uri';
                    |    const OPT_CLIENT_OPTIONS = 'options';
                    |
                    |    public function getApiUri(): string;
                    |
                    |    public function setApiUri(string $!apiUri): Config;
                    |
                    |    public function getClientOptions(): array;
                    |
                    |    public function setClientOptions(array $!options): Config;
                    |
                    |    public function getOptions(): array;
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
}

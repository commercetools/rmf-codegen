package io.vrap.codegen.kt.languages.php.model

import com.damnhandy.uri.template.UriTemplate
import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.kt.languages.java.extensions.forcedLiteralEscape
import io.vrap.codegen.kt.languages.php.PhpSubTemplates
import io.vrap.codegen.kt.languages.php.extensions.authUri
import io.vrap.codegen.kt.languages.php.extensions.toNamespaceDir
import io.vrap.codegen.kt.languages.php.extensions.toNamespaceName
import io.vrap.rmf.codegen.kt.di.VrapConstants
import io.vrap.rmf.codegen.kt.io.TemplateFile
import io.vrap.rmf.codegen.kt.rendring.FileProducer
import io.vrap.rmf.codegen.kt.rendring.utils.escapeAll
import io.vrap.rmf.codegen.kt.rendring.utils.keepIndentation
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.util.StringCaseFormat

class PhpFileProducer @Inject constructor() : FileProducer {

    @Inject
    @Named(VrapConstants.PACKAGE_NAME)
    lateinit var packagePrefix:String

    @Inject
    lateinit var api:Api

    override fun produceFiles(): List<TemplateFile> = listOf(
            baseCollection(),
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
            authConfig(),
            clientCredentialsConfig(),
            tokenModel(),
            oauthHandlerFactory(),
            baseException(),
            invalidArgumentException()
    )

    private fun baseCollection(): TemplateFile {
        return TemplateFile(relativePath = "src/Base/Collection.php",
                content = """
                        |<?php
                        |${PhpSubTemplates.generatorInfo}
                        |
                        |namespace ${packagePrefix.toNamespaceName()}\Base;
                        |
                        |class Collection
                        |{
                        |}
                    """.trimMargin()
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
                    |
                    |class ClientFactory
                    |{
                    |    /**
                    |     * @param Config|array $!config
                    |     * @throws InvalidArgumentException
                    |     * @return HttpClient
                    |     */
                    |    public function createGuzzleClient($!config = [], array $!middlewares = []): HttpClient
                    |    {
                    |        $!config = $!this->createConfig($!config);
                    |        return $!this->createGuzzle6Client($!config->getOptions(), $!middlewares);
                    |    }
                    |
                    |    /**
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
                    |    public function getExpiresIn(): ?int;
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
                    |    "php": ">=7.1",
                    |    "guzzlehttp/psr7": "^1.1",
                    |    "guzzlehttp/guzzle": "^6.0",
                    |    "psr/cache": "^1.0",
                    |    "psr/log": "^1.0",
                    |    "cache/filesystem-adapter": "^1.0"
                    |  },
                    |  "require-dev": {
                    |    "monolog/monolog": "^1.3",
                    |    "phpunit/phpunit": "^6.0",
                    |    "cache/array-adapter": "^1.0"
                    |  }
                    |}
                """.trimMargin())
    }
    
    private fun config(): TemplateFile {
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
            .map { "$!apiUri = str_replace('{$it}', (isset($!config[self::OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}]) ? $!config[self::OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}] : '{$it}'), $!apiUri);" }
            .joinToString(separator = "\n")

    fun UriTemplate.constVariables(): String = variables
            .map { "const OPT_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)}= '$it';" }
            .joinToString(separator = "\n")

    private fun authConfig(): TemplateFile {
        return TemplateFile(relativePath = "src/Client/AuthConfig.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |class AuthConfig
                    |{
                    |    const AUTH_URI = '${api.authUri()}';
                    |
                    |    const OPT_BASE_URI = 'base_uri';
                    |    const OPT_AUTH_URI = 'auth_uri';
                    |    const OPT_CLIENT_OPTIONS = 'options';
                    |    const GRANT_TYPE = '';
                    |    const OPT_CACHE_DIR = 'cacheDir';
                    |
                    |    /** @var string */
                    |    private $!authUri;
                    |
                    |    /** @var array */
                    |    private $!clientOptions;
                    |
                    |    /** @var string */
                    |    private $!cacheDir;
                    |
                    |    public function __construct(array $!config = [])
                    |    {
                    |        $!this->authUri = isset($!config[self::OPT_AUTH_URI]) ? $!config[self::OPT_AUTH_URI] : static::AUTH_URI;
                    |        $!this->clientOptions = isset($!config[self::OPT_CLIENT_OPTIONS]) && is_array($!config[self::OPT_CLIENT_OPTIONS]) ?
                    |            $!config[self::OPT_CLIENT_OPTIONS] : [];
                    |        $!this->cacheDir = isset($!config[self::OPT_CACHE_DIR]) ? $!config[self::OPT_CACHE_DIR] : getcwd();
                    |    }
                    |
                    |    public function getGrantType(): string
                    |    {
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
                    |    public function getCacheDir(): string
                    |    {
                    |        return $!this->cacheDir;
                    |    }
                    |
                    |    public function setCacheDir($!cacheDir): AuthConfig
                    |    {
                    |        $!this->cacheDir = $!cacheDir;
                    |        return $!this;
                    |    }
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
                    |use GuzzleHttp\Client;
                    |use Psr\Cache\CacheItemPoolInterface;
                    |use Psr\Cache\CacheItemInterface;
                    |
                    |class CachedTokenProvider implements TokenProvider
                    |{
                    |    /** @var TokenProvider */
                    |    private $!provider;
                    |
                    |    /** @var CacheItemPoolInterface */
                    |    private $!cache;
                    |
                    |    public function __construct(TokenProvider $!provider, CacheItemPoolInterface $!cache)
                    |    {
                    |       $!this->cache = $!cache;
                    |       $!this->provider = $!provider;
                    |    }
                    |
                    |    public function getToken(): Token
                    |    {
                    |        $!item = $!this->cache->getItem(sha1('access_token'));
                    |        if ($!item->isHit()) {
                    |            return new TokenModel((string)$!item->get());
                    |        }
                    |
                    |        $!token = $!this->provider->getToken();
                    |        // ensure token to be invalidated in cache before TTL
                    |        $!ttl = max(1, floor($!token->getExpiresIn()/2));
                    |        $!this->saveToken($!token->getValue(), $!item, (int)$!ttl);
                    |
                    |        return $!token;
                    |    }
                    |
                    |    private function saveToken(string $!token, CacheItemInterface $!item, int $!ttl): void
                    |    {
                    |        if (!is_null($!this->cache)) {
                    |            $!item->set($!token);
                    |            $!item->expiresAfter($!ttl);
                    |            $!this->cache->save($!item);
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
                    |    /** @var ClientCredentials */
                    |    private $!credentials;
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
                    |        $!body = json_decode((string)$!result->getBody(), true);
                    |        return new TokenModel((string)$!body[self::ACCESS_TOKEN], (int)$!body[self::EXPIRES_IN]);
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
                    |    /** @var CacheItemPoolInterface */
                    |    private $!cache;
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
                    |use Psr\Log\LoggerInterface;
                    |use Psr\Log\LogLevel;
                    |
                    |class MiddlewareFactory
                    |{
                    |    public static function createOAuthMiddleware(AuthConfig $!authConfig, CacheItemPoolInterface $!cache = null)
                    |    {
                    |        $!handler = OAuthHandlerFactory::ofAuthConfig($!authConfig, $!cache);
                    |        return Middleware::mapRequest($!handler);
                    |    }
                    |
                    |    public static function createOAuthMiddlewareForProvider(TokenProvider $!provider)
                    |    {
                    |        $!handler = OAuthHandlerFactory::ofProvider($!provider);
                    |        return Middleware::mapRequest($!handler);
                    |    }
                    |
                    |    public static function createLoggerMiddleware(LoggerInterface $!logger, $!logLevel = LogLevel::INFO, $!template = MessageFormatter::CLF)
                    |    {
                    |        return Middleware::log($!logger, new MessageFormatter($!template), $!logLevel);
                    |    }
                    |}
                """.trimMargin().forcedLiteralEscape()
        )
    }

    private fun clientCredentialsConfig(): TemplateFile {
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
                    |    /** @var string */
                    |    private $!scope;
                    |
                    |    public function __construct(array $!authConfig = [])
                    |    {
                    |        parent::__construct($!authConfig);
                    |        $!this->clientId = isset($!authConfig[self::CLIENT_ID]) ? $!authConfig[self::CLIENT_ID] : null;
                    |        $!this->clientSecret = isset($!authConfig[self::CLIENT_SECRET]) ? $!authConfig[self::CLIENT_SECRET] : null;
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
                    |    public function setClientSecret($!clientSecret): ClientCredentialsConfig
                    |    {
                    |        $!this->clientSecret = $!clientSecret;
                    |        return $!this;
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
                    |    /** @return string */
                    |    private $!value;
                    |
                    |    /** @return int */
                    |    private $!expiresIn;
                    |
                    |    public function __construct(string $!value, int $!expiresIn = null)
                    |    {
                    |        $!this->value = $!value;
                    |        $!this->expiresIn = $!expiresIn;
                    |    }
                    |
                    |    public function getValue(): string
                    |    {
                    |        return $!this->value;
                    |    }
                    |
                    |    public function getExpiresIn(): ?int
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
                    |
                    |class OAuthHandlerFactory
                    |{
                    |    public static function ofAuthConfig(AuthConfig $!authConfig, CacheItemPoolInterface $!cache = null): OAuth2Handler
                    |    {
                    |        if (is_null($!cache)) {
                    |            $!cacheDir = $!authConfig->getCacheDir();
                    |            $!filesystemAdapter = new Local($!cacheDir);
                    |            $!filesystem        = new Filesystem($!filesystemAdapter);
                    |            $!cache = new FilesystemCachePool($!filesystem);
                    |        }
                    |        switch(true) {
                    |           case $!authConfig instanceof ClientCredentialsConfig:
                    |               $!provider = new CachedTokenProvider(
                    |                   new ClientCredentialTokenProvider(
                    |                       new Client($!authConfig->getClientOptions()),
                    |                       $!authConfig
                    |                   ),
                    |                   $!cache
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
}

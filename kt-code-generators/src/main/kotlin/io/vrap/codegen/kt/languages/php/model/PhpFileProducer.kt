package io.vrap.codegen.kt.languages.php.model

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.kt.languages.java.extensions.forcedLiteralEscape
import io.vrap.codegen.kt.languages.php.PhpSubTemplates
import io.vrap.codegen.kt.languages.php.extensions.toNamespaceDir
import io.vrap.codegen.kt.languages.php.extensions.toNamespaceName
import io.vrap.rmf.codegen.kt.di.VrapConstants
import io.vrap.rmf.codegen.kt.io.TemplateFile
import io.vrap.rmf.codegen.kt.rendring.FileProducer
import io.vrap.rmf.codegen.kt.rendring.utils.escapeAll

class PhpFileProducer @Inject constructor() : FileProducer {

    @Inject
    @Named(VrapConstants.PACKAGE_NAME)
    lateinit var packagePrefix:String

    override fun produceFiles(): List<TemplateFile> = listOf(
            baseCollection(),
            baseNullable(),
            clientFactory(),
            tokenProvider(),
            token(),
            composerJson(),
            config(),
            credentialTokenProvider(),
            oauth2Handler()
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
                    |    /*
                    |     * @var bool
                    |     */
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
                    |use Cache\Adapter\Filesystem\FilesystemCachePool;
                    |use GuzzleHttp\Client as HttpClient;
                    |use GuzzleHttp\HandlerStack;
                    |use GuzzleHttp\MessageFormatter;
                    |use GuzzleHttp\Middleware;
                    |use GuzzleHttp\Psr7\Response;
                    |use League\Flysystem\Adapter\Local;
                    |use League\Flysystem\Filesystem;
                    |use Psr\Cache\CacheItemPoolInterface;
                    |use Psr\Http\Message\RequestInterface;
                    |use Psr\Http\Message\ResponseInterface;
                    |use Psr\Log\LoggerInterface;
                    |use Psr\Log\LogLevel;
                    |
                    |class ClientFactory
                    |{
                    |    /**
                    |     * @param Config|array $!config
                    |     * @param LoggerInterface $!logger
                    |     * @param CacheItemPoolInterface $!cache
                    |     * @param TokenProvider $!provider
                    |     * @return HttpClient
                    |     */
                    |    public function create(
                    |        $!config = [],
                    |        LoggerInterface $!logger = null,
                    |        CacheItemPoolInterface $!cache = null,
                    |        TokenProvider $!provider = null
                    |    ) {
                    |        return $!this->createClient($!config, $!logger, $!cache, $!provider);
                    |    }
                    |
                    |    /**
                    |     * @param Config|array $!config
                    |     * @return Config
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
                    |        throw new InvalidArgumentException();
                    |    }
                    |
                    |    /**
                    |     * @param Config|array $!config
                    |     * @param LoggerInterface $!logger
                    |     * @param CacheItemPoolInterface $!cache
                    |     * @param TokenProvider $!provider
                    |     * @return HttpClient
                    |     */
                    |    public function createClient(
                    |        $!config,
                    |        LoggerInterface $!logger = null,
                    |        CacheItemPoolInterface $!cache = null,
                    |        TokenProvider $!provider = null
                    |    ) {
                    |        $!config = $!this->createConfig($!config);
                    |
                    |        if (is_null($!cache)) {
                    |            $!cacheDir = $!config->getCacheDir();
                    |
                    |            $!filesystemAdapter = new Local($!cacheDir);
                    |            $!filesystem        = new Filesystem($!filesystemAdapter);
                    |            $!cache = new FilesystemCachePool($!filesystem);
                    |        }
                    |        $!credentials = $!config->getCredentials()->toArray();
                    |        $!oauthHandler = $!this->getHandler($!credentials, $!config->getAuthUrl(), $!cache, $!provider);
                    |
                    |        $!options = $!config->getOptions();
                    |        return $!this->createGuzzle6Client($!options, $!logger, $!oauthHandler);
                    |    }
                    |
                    |    /**
                    |     * @param array $!options
                    |     * @param LoggerInterface|null $!logger
                    |     * @param OAuth2Handler $!oauthHandler
                    |     * @return HttpClient
                    |     */
                    |    private function createGuzzle6Client(
                    |        array $!options,
                    |        LoggerInterface $!logger = null,
                    |        OAuth2Handler $!oauthHandler
                    |    ): HttpClient {
                    |        if (isset($!options['handler']) && $!options['handler'] instanceof HandlerStack) {
                    |            $!handler = $!options['handler'];
                    |        } else {
                    |            $!handler = HandlerStack::create();
                    |            $!options['handler'] = $!handler;
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
                    |
                    |        if (!is_null($!logger)) {
                    |            $!handler->push(Middleware::log($!logger, new MessageFormatter()));
                    |        }
                    |        $!handler->push(
                    |            Middleware::mapRequest($!oauthHandler),
                    |            'oauth_2_0'
                    |        );
                    |
                    |        $!client = new HttpClient($!options);
                    |
                    |        return $!client;
                    |    }
                    |
                    |    /**
                    |     * @param array $!credentials
                    |     * @param string $!accessTokenUrl
                    |     * @param CacheItemPoolInterface $!cache
                    |     * @param TokenProvider $!provider
                    |     * @return OAuth2Handler
                    |     */
                    |    private function getHandler(
                    |        array $!credentials,
                    |        string $!accessTokenUrl,
                    |        CacheItemPoolInterface $!cache = null,
                    |        TokenProvider $!provider = null
                    |    ): OAuth2Handler {
                    |        if (is_null($!provider)) {
                    |            $!provider = new CredentialTokenProvider(
                    |                new HttpClient(),
                    |                $!accessTokenUrl,
                    |                $!credentials
                    |            );
                    |        }
                    |        return new OAuth2Handler($!provider, $!cache);
                    |    }
                    |
                    |    /**
                    |     * @return ClientFactory
                    |     */
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
                    |    /**
                    |     * @return Token
                    |     */
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
                    |    /**
                    |     * @return string
                    |     */
                    |    public function getValue(): string;
                    |
                    |    /**
                    |     * @return int
                    |     */
                    |    public function getExpiresIn(): int;
                    |}
                """.trimMargin()
        )
    }
    
    private fun composerJson(): TemplateFile {
        return TemplateFile(relativePath = "composer.json",
                content = """
                    |{
                    |  "name": "commercetools/raml-php-sdk",
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
                    |    "phpunit/phpunit": "^6.0"
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
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |class Config
                    |{
                    |    const API_URI = '<api.baseUri>';
                    |
                    |    const OPT_BASE_URI = 'base_uri';
                    |    const OPT_CLIENT_OPTIONS = 'options';
                    |
                    |    /**
                    |     * @var string
                    |     */
                    |    private $!apiUrl;
                    |
                    |    /**
                    |     * @var array
                    |     */
                    |    private $!clientOptions;
                    |
                    |    /**
                    |     * @param array $!config
                    |     */
                    |    public function __construct(array $!config = [])
                    |    {
                    |        $!this->apiUrl = isset($!config[self::OPT_BASE_URI]) ? $!config[self::OPT_BASE_URI] : static::API_URI;
                    |        $!this->clientOptions = isset($!config[self::OPT_CLIENT_OPTIONS]) && is_array($!config[self::OPT_CLIENT_OPTIONS]) ?
                    |            $!config[self::OPT_CLIENT_OPTIONS] : [];
                    |    }
                    |
                    |    /**
                    |     * @return string
                    |     */
                    |    public function getApiUrl(): string
                    |    {
                    |        return $!this->apiUrl;
                    |    }
                    |
                    |    /**
                    |     * @param string $!apiUrl
                    |     * @return Config
                    |     */
                    |    public function setApiUrl(string $!apiUrl): Config
                    |    {
                    |        $!this->apiUrl = $!apiUrl;
                    |        return $!this;
                    |    }
                    |
                    |    /**
                    |     * @return array
                    |     */
                    |    public function getClientOptions(): array
                    |    {
                    |        return $!this->clientOptions;
                    |    }
                    |
                    |    /**
                    |     * @param $!options
                    |     * @return Config
                    |     */
                    |    public function setClientOptions(array $!options): Config
                    |    {
                    |        $!this->clientOptions = $!options;
                    |        return $!this;
                    |    }
                    |
                    |    public function getOptions(): array
                    |    {
                    |        return array_merge(
                    |            [self::OPT_BASE_URI => $!this->getApiUrl()],
                    |            $!this->clientOptions
                    |        );
                    |    }
                    |}
                    |
                """.trimMargin().forcedLiteralEscape())
    }
    
    private fun credentialTokenProvider() : TemplateFile {
        return TemplateFile(relativePath = "src/Client/CredentialTokenProvider.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${packagePrefix.toNamespaceName()}\Client;
                    |
                    |use GuzzleHttp\Client;
                    |
                    |class CredentialTokenProvider implements TokenProvider
                    |{
                    |    const GRANT_TYPE = 'grant_type';
                    |    const GRANT_TYPE_CLIENT_CREDENTIALS = 'client_credentials';
                    |    const CLIENT_ID = 'clientId';
                    |    const CLIENT_SECRET = 'clientSecret';
                    |    const SCOPE = 'scope';
                    |    const ACCESS_TOKEN = 'access_token';
                    |    const EXPIRES_IN = 'expires_in';
                    |
                    |    /**
                    |     * @var Client
                    |     */
                    |    private $!client;
                    |
                    |    /**
                    |     * @var array
                    |     */
                    |    private $!credentials;
                    |
                    |    /**
                    |     * @var string
                    |     */
                    |    private $!accessTokenUrl;
                    |
                    |    /**
                    |     * @param Client $!client
                    |     * @param string $!accessTokenUrl
                    |     * @param array $!credentials
                    |     */
                    |    public function __construct(Client $!client, string $!accessTokenUrl, array $!credentials)
                    |    {
                    |        $!this->accessTokenUrl = $!accessTokenUrl;
                    |        $!this->client = $!client;
                    |        $!this->credentials = $!credentials;
                    |    }
                    |
                    |    /**
                    |     * @return Token
                    |     */
                    |    public function getToken(): Token
                    |    {
                    |        $!data = [
                    |            self::GRANT_TYPE => self::GRANT_TYPE_CLIENT_CREDENTIALS
                    |        ];
                    |        if (isset($!this->credentials[self::SCOPE])) {
                    |            $!data[self::SCOPE] = $!this->credentials[self::SCOPE];
                    |        }
                    |        $!options = [
                    |            'form_params' => $!data,
                    |            'auth' => [$!this->credentials[self::CLIENT_ID], $!this->credentials[self::CLIENT_SECRET]]
                    |        ];
                    |
                    |        $!result = $!this->client->post($!this->accessTokenUrl, $!options);
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
                    |use Psr\Cache\CacheItemInterface;
                    |use Psr\Cache\CacheItemPoolInterface;
                    |use Psr\Http\Message\RequestInterface;
                    |
                    |class OAuth2Handler
                    |{
                    |
                    |    /**
                    |     * @var TokenProvider
                    |     */
                    |    private $!provider;
                    |    /**
                    |     * @var CacheItemPoolInterface
                    |     */
                    |    private $!cache;
                    |
                    |    /**
                    |     * OAuth2Handler constructor.
                    |     * @param TokenProvider $!provider
                    |     * @param CacheItemPoolInterface $!cache
                    |     */
                    |    public function __construct(TokenProvider $!provider, CacheItemPoolInterface $!cache) {
                    |        $!this->provider = $!provider;
                    |        $!this->cache = $!cache;
                    |    }
                    |
                    |    /**
                    |     * @param RequestInterface $!request
                    |     * @param array $!options
                    |     * @return RequestInterface
                    |     */
                    |    public function __invoke(RequestInterface $!request, array $!options = []): RequestInterface {
                    |        return $!request->withHeader('Authorization', $!this->getAuthorizationHeader());
                    |    }
                    |
                    |    /**
                    |     * @return string
                    |     */
                    |    public function getAuthorizationHeader(): string
                    |    {
                    |        return 'Bearer ' . $!this->getBearerToken();
                    |    }
                    |
                    |    /**
                    |     * @return string
                    |     */
                    |    private function getBearerToken(): string
                    |    {
                    |        $!item = null;
                    |        if (!is_null($!this->cache)) {
                    |            $!item = $!this->cache->getItem(sha1('access_token'));
                    |            if ($!item->isHit()) {
                    |                return (string)$!item->get();
                    |            }
                    |        }
                    |
                    |        $!token = $!this->provider->getToken();
                    |        // ensure token to be invalidated in cache before TTL
                    |        $!ttl = max(1, floor($!token->getExpiresIn()/2));
                    |        $!this->saveToken($!token->getValue(), $!item, (int)$!ttl);
                    |
                    |        return $!token->getValue();
                    |    }
                    |
                    |    /**
                    |     * @param string $!token
                    |     * @param CacheItemInterface $!item
                    |     * @param int $!ttl
                    |     */
                    |    private function saveToken(string $!token, CacheItemInterface $!item, int $!ttl)
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
}

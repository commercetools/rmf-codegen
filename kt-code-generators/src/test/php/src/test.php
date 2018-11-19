<?php
declare(strict_types = 1);
namespace Commercetools;

use Commercetools\Importer\Client\ClientCredentialsConfig;
use Commercetools\Importer\Client\ClientFactory;
use Commercetools\Importer\Client\Config;
use Commercetools\Importer\Client\MiddlewareFactory;
use Commercetools\Importer\Client\RawTokenProvider;
use Commercetools\Importer\Client\TokenModel;
use Monolog\Handler\StreamHandler;
use Monolog\Logger;

require __DIR__ . '/../vendor/autoload.php';

$config = new Config(['domain' => 'api', 'projectKey' => 'phpsphere-90']);
$client = ClientFactory::of()->createGuzzleClient(
    $config,
    [
        'oauth' => MiddlewareFactory::createOAuthMiddleware(
            new ClientCredentialsConfig(
                [
                    'clientId' => '42Q_fnD0bs705qMwlL6PpSkO',
                    'clientSecret' => 'ZGN05ioItsw_wvu1EX4zUcTNmxVweqT4'
                ]
            )
        ),
        'logger' => MiddlewareFactory::createLoggerMiddleware(new Logger('name', [new StreamHandler('./logs/requests.log')]))
    ]
);

var_dump($config);

$response = $client->get('');

var_dump((string)$response->getBody());

$client = ClientFactory::of()->createGuzzleClient(
    $config,
    [
        'oauth' => MiddlewareFactory::createOAuthMiddlewareForProvider(
            new RawTokenProvider(new TokenModel('BBG7BqDwvwuyXzo5zhbtl5XjFIp-rdWV'))
        ),
        'logger' => MiddlewareFactory::createLoggerMiddleware(new Logger('name', [new StreamHandler('./logs/requests.log')]))
    ]
);

$response = $client->get('');

var_dump((string)$response->getBody());

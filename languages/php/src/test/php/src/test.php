<?php
declare(strict_types = 1);

namespace Commercetools\Importer\Models\Product {
    function allVariants(ProductData $data)
    {
        $data = array_merge(
            [$data->getMasterVariant()],
            $data->getVariants()->jsonSerialize()
        );
        return new ProductVariantCollection($data);
    }
}

namespace Commercetools {

    use Cache\Adapter\Filesystem\FilesystemCachePool;
    use Commercetools\Importer\Base\ResultMapper;
    use Commercetools\Importer\Client\ApiRoot;
    use Commercetools\Importer\Client\CachedTokenProvider;
    use Commercetools\Importer\Client\ClientCredentialsConfig;
    use Commercetools\Importer\Client\ClientCredentialTokenProvider;
    use Commercetools\Importer\Client\ClientFactory;
    use Commercetools\Importer\Client\Config;
    use Commercetools\Importer\Client\MiddlewareFactory;
    use Commercetools\Importer\Client\Resource\ResourceByProjectKey;
    use Commercetools\Importer\Models\Cart\Cart;
    use Commercetools\Importer\Models\Cart\CartModel;
    use Commercetools\Importer\Models\Category\CategoryPagedQueryResponseModel;
    use Commercetools\Importer\Models\Common\LocalizedStringModel;
    use Commercetools\Importer\Models\Error\ErrorResponse;
    use Commercetools\Importer\Models\Error\ErrorResponseModel;
    use function Commercetools\Importer\Models\Product\allVariants;
    use Commercetools\Importer\Models\Product\ProductDataModel;
    use Commercetools\Importer\Models\Product\ProductProjectionPagedSearchResponse;
    use Commercetools\Importer\Models\Product\ProductProjectionPagedSearchResponseModel;
    use Commercetools\Importer\Models\Project\ProjectModel;
    use GuzzleHttp\Client;
    use GuzzleHttp\HandlerStack;
    use GuzzleHttp\Psr7\Response;
    use League\Flysystem\Adapter\Local;
    use League\Flysystem\Filesystem;
    use Monolog\Handler\StreamHandler;
    use Monolog\Logger;

    require __DIR__ . '/../vendor/autoload.php';


    $authHandler = HandlerStack::create();
    $authHandler->push(MiddlewareFactory::createLoggerMiddleware(new Logger('auth', [new StreamHandler('./logs/requests.log')])));

    $authConfig = new ClientCredentialsConfig(
        [
            'options' => ['handler' => $authHandler],
            'clientId' => '',
            'clientSecret' => ''
        ]
    );
    $logger = new Logger('client', [new StreamHandler('./logs/requests.log')]);

    $client = ClientFactory::of()->createGuzzleClient(
        $authConfig,
        $logger
    );


    $response = $client->get('');

    var_dump((string)$response->getBody());

//$client = ClientFactory::of()->createGuzzleClient(
//    $config,
//    [
//        'oauth' => MiddlewareFactory::createOAuthMiddlewareForProvider(
//            new RawTokenProvider(new TokenModel(''))
//        ),
//        'logger' => MiddlewareFactory::createLoggerMiddleware(new Logger('client', [new StreamHandler('./logs/requests.log')]))
//    ]
//);


    $root = new ApiRoot($client);

    $response = $root->withProjectKey('phpsphere-90')->get()->send();

    $project = new ProjectModel(json_decode((string)$response->getBody()));

    var_dump($project->getKey());
    var_dump($project->getCreatedAt());
    var_dump($project->getMessages());
    var_dump($project->getShippingRateInputType());
    var_dump($project);
    var_dump(json_encode($project));

//$project = new ProjectModel();
//var_dump($project);
//var_dump($project->getKey());

    $response = $root->withProjectKey('phpsphere-90')->categories()->get()->send();


    $catResponse = new CategoryPagedQueryResponseModel(json_decode((string)$response->getBody()));
    $categories = $catResponse->getResults();
    $c1 = $categories->current();
    var_dump($categories->current());
    var_dump($categories->current());
    $categories->next();
    var_dump($categories->current());
    var_dump($categories);
    $d = $categories->offsetGet(1);
    $t = $categories->getIterator();
    $t->next();
    $t->next();
    var_dump($t);
    var_dump($t->current());
    $t->next();
    var_dump($t->current());
    var_dump($categories);
    var_dump($c1->getName());
    var_dump($c1->getName()->by('en'));

    $c1->setCreatedAt(new \DateTimeImmutable());

    $root = new ApiRoot($client, ['projectKey' => 'phpsphere-90']);

    $t = $root->withProjectKey()->productProjections()->search()->get()->withFacet("categories.id");

    $r = $client->send($t);
    $c = $t->execute();


    var_dump($c->getResults()->current());
    var_dump($c->getFacets());
    var_dump((string)$r->getBody());


    $tr = new Response(500, [], '{"foo" : {"bar": "baz"}, "foos": [{"bars": "bazs"}], "bar": [1, 2, 3], "baz": "boz" }');
    $foo = $t->mapFromResponse($tr);

    $tr = new Response(200, [], '{"foo" : {"bar": "baz"}, "foos": [{"bars": "bazs"}], "bar": [1, 2, 3], "baz": "boz" }');
    /** @var CartModel $bar */
    $bar = $t->mapFromResponse($tr, CartModel::class);


    var_dump(get_class($foo));
    var_dump(get_class($bar));

    var_dump($foo->get('foo')->get("bar"));
    var_dump($foo->get('foos')->current());
    var_dump($foo->get('bar'));
    var_dump($foo->get('baz'));


    $tr = new Response(200, [], '{"de-DE" : "test" }');
    $l = $t->mapFromResponse($tr, LocalizedStringModel::class);
    var_dump($l->by("de-DE"));

    $t = new ProductDataModel();

    $t->setDescription(new LocalizedStringModel());
//    allVariants($t);

//    var_dump((new CartModel())->use());
}

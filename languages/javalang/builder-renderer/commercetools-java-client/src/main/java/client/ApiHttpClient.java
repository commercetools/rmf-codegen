package client;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApiHttpClient {

    private final Middleware reducedMiddleware;

    public ApiHttpClient(final List<Middleware> middlewares) {
        this.reducedMiddleware = middlewares.stream().reduce(ApiHttpClient::reduce).orElse(NoOpMiddleware.getInstance());
    }

    public ApiHttpClient(final Middleware... middlewares) {
        this.reducedMiddleware = Arrays.stream(middlewares).reduce(ApiHttpClient::reduce).orElse(NoOpMiddleware.getInstance());
    }

    public CompletableFuture<ApiHttpResponse> execute(final ApiHttpRequest request) {
        return CompletableFuture.completedFuture(null);
    }

    public ApiHttpResponse executeBlocking(ApiHttpRequest request) {
        try {
            return execute(request).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Middleware reduce(Middleware middleware1, Middleware middleware2) {
        return arg -> {
            Middleware intermediateMiddleware = intermediateOpArg -> middleware2.next(MiddlewareArg.from(intermediateOpArg.getRequest(), intermediateOpArg.getResponse(), intermediateOpArg.getError(), arg.getNext()));
            return middleware1.next(MiddlewareArg.from(arg.getRequest(), arg.getResponse(), arg.getError(), intermediateMiddleware));
        };
    }
}

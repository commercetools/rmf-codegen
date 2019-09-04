package client.middlewares;

import client.ApiHttpHeaders;
import client.Middleware;
import client.MiddlewareArg;
import json.CommercetoolsJsonUtils;

import java.util.concurrent.CompletableFuture;

public final class LoggerMiddleware implements Middleware {

    final
    @Override
    public CompletableFuture<MiddlewareArg> next(MiddlewareArg arg) {
        try {
            System.out.println(CommercetoolsJsonUtils.toJsonString(arg));
        } catch (Exception e) {
            System.out.println(e);
        }
        return arg.getNext().next(arg);
    }
}

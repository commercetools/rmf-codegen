package client;

import java.util.concurrent.CompletableFuture;

public class NoOpMiddleware implements Middleware {

    private static final NoOpMiddleware noOpMiddleware = new NoOpMiddleware();

    private NoOpMiddleware(){}

    public static NoOpMiddleware getInstance(){
        return noOpMiddleware;
    }

    @Override
    public CompletableFuture<MiddlewareArg> next(MiddlewareArg arg) {
        return CompletableFuture.completedFuture(arg);
    }
}

package client;

public class TempClient implements ApiHttpClient {
    
    public ApiHttpResponse execute(final ApiHttpRequest httpRequest) {
        return new ApiHttpResponse(200, null, "{}");
    }
}

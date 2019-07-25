package client;

public interface ApiHttpClient {

    public ApiHttpResponse execute(final ApiHttpRequest httpRequest);
    
}

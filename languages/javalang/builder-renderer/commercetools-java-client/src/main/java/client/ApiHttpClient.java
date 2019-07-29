package client;

import java.io.IOException;

public interface ApiHttpClient {

    public ApiHttpResponse execute(final ApiHttpRequest httpRequest) throws IOException;
    
}

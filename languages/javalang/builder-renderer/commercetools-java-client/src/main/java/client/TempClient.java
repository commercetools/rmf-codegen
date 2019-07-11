package client;

public class TempClient {
    
    public HttpResponse execute(final HttpRequest httpRequest) {
        return new HttpResponse(200, null, "{}");
    }
}

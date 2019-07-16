package client;

public class TempClient implements HttpClient {
    
    public HttpResponse execute(final HttpRequest httpRequest) {
        return new HttpResponse(200, null, "{}");
    }
}

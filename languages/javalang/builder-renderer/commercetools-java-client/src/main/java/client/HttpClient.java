package client;

public interface HttpClient {

    public HttpResponse execute(final HttpRequest httpRequest);
    
}

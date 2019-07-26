package client;

public class ApiHttpRequest {
    
    private ApiHttpMethod method;
    private String path;
    private ApiHttpHeaders headers;
    private String body;

    public ApiHttpRequest() {
        
    }
    
    public ApiHttpRequest(ApiHttpMethod method, String path, ApiHttpHeaders headers, String body) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    public ApiHttpMethod getMethod() {
        return method;
    }

    public void setMethod(ApiHttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ApiHttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(ApiHttpHeaders headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

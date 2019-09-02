package client;

import json.CommercetoolsJsonUtils;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class TempClient implements ApiHttpClient {
    private static final boolean LOG = true;
    
    private static final String AUTH_EUROPE_BASE_URL = "https://auth.sphere.io";
    private static final String AUTH_US_BASE_URL = "https://auth.commercetools.co";
    private static final String API_EUROPE_BASE_URL = "https://api.sphere.io";
    private static final String AUTH_PATH = "/oauth/token";
    private static final String INTROSPECT_PATH = "/oauth/introspect";
    
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();
    private String token = "";
    
    public ApiHttpResponse execute(final ApiHttpRequest apiHttpRequest) throws IOException {
        if(token.isEmpty() || !isTokenActive()){
            token = obtainAccessToken();
        }
        //set path
        Request.Builder httpRequestBuilder = new Request.Builder()
                .url(API_EUROPE_BASE_URL + apiHttpRequest.getPath());
        
        //set headers
        for(Map.Entry<String, String> entry : apiHttpRequest.getHeaders().getHeaders().entrySet()){
            httpRequestBuilder = httpRequestBuilder.header(entry.getKey(), entry.getValue());
        }
        httpRequestBuilder.header("Authorization", " Bearer " + token);
        
        //set method and body
        switch (apiHttpRequest.getMethod()){
            case GET:
                httpRequestBuilder = httpRequestBuilder.get();
                break;
            case DELETE:
                httpRequestBuilder = httpRequestBuilder.delete();
                break;
            case POST:
                httpRequestBuilder = httpRequestBuilder.post(RequestBody.create(apiHttpRequest.getBody(), JSON));
                break;
            case PUT:
                httpRequestBuilder = httpRequestBuilder.put(RequestBody.create(apiHttpRequest.getBody(), JSON));
                break;
            default:
                throw new RuntimeException("Non supported HTTP Method : " + apiHttpRequest.getMethod().toString());
        }

        print("<====NEW REQUEST====>");
        Request httpRequest = httpRequestBuilder.build();
        print("Request : " + httpRequest.toString());
        print("Request body : " + apiHttpRequest.getBody());
        Response response = client.newCall(httpRequest).execute();
        String responseString = response.body() == null ? "" : response.body().string();
        print("Response status code : " + response.code());
        print("Response body : " + responseString);
        
        ApiHttpHeaders apiHttpHeaders = new ApiHttpHeaders();
        for(Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()){
            apiHttpHeaders.addHeader(entry.getKey(), entry.getValue().get(0));
        }
        return new ApiHttpResponse(response.code(), apiHttpHeaders, responseString);
    }
    
    private String obtainAccessToken() throws IOException {
        print("<====OBTAINING NEW ACCESS TOKEN====>");

        String clientId = System.getenv("JVM_SDK_IT_CLIENT_ID");
        String clientSecret = System.getenv("JVM_SDK_IT_CLIENT_SECRET");
        String projectKey = System.getenv("JVM_SDK_IT_PROJECT_KEY");
        
        String auth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("scope", "manage_project:" + projectKey + " manage_api_clients:" + projectKey).build();
        
        Request request = new Request.Builder()
                .url(AUTH_EUROPE_BASE_URL + AUTH_PATH)
                .header("Authorization", "Basic " + auth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody).build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        print("Token obtaining response status code : " + response.code());
        print("Token obtaining response body : " + responseString);
        AuthenticationToken authenticationToken = CommercetoolsJsonUtils.fromJsonString(responseString, AuthenticationToken.class);
        String accessToken = authenticationToken.getAccessToken();
        print("Access token : " + accessToken);
        return accessToken;
    }
    
    private boolean isTokenActive() throws IOException {
        print("<====TOKEN INTROSPECTION====>");
        String clientId = System.getenv("JVM_SDK_IT_CLIENT_ID");
        String clientSecret = System.getenv("JVM_SDK_IT_CLIENT_SECRET");

        String auth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        RequestBody formBody = new FormBody.Builder()
                .add("token", token).build();
        Request request = new Request.Builder()
                .url(AUTH_EUROPE_BASE_URL + INTROSPECT_PATH)
                .header("Authorization", "Basic " + auth)
                .post(formBody).build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        print("Token introspection response status code : " + response.code());
        print("Token introspection response body : " + responseString);
        TokenIntrospection tokenIntrospection = CommercetoolsJsonUtils.fromJsonString(responseString, TokenIntrospection.class);
        return tokenIntrospection.isActive();
    }
    
    private void print(String msg) {
        if(LOG){
            System.out.println(msg);
        }
    }
}

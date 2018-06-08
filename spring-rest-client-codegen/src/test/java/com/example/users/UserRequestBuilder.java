package com.example.users;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class UserRequestBuilder {
    private final String baseUri;
    private final RestTemplate restTemplate;

    public UserRequestBuilder(final String baseUri, final RestTemplate restTemplate) {
        this.baseUri = baseUri;
        this.restTemplate = restTemplate;
    }

    public GetRequestBuilder GET(final String department, final String userId) {
        return new GetRequestBuilder(department, userId);
    }

    public class GetRequestBuilder {
        private final String department;
        private final String userId;

        private GetRequestBuilder(final String department, final String userId) {
            this.department = department;
            this.userId = userId;
        }

        public Request<User> build() {
            final Map<String, Object> parameters = new HashMap<>();

            parameters.put("department", department);
            parameters.put("userId", userId);

            return new RequestImpl<>(baseUri + "/{department}/users/{userId}", HttpMethod.GET, User.class, parameters);
        }
    }

    public DeleteRequestBuilder DELETE(final String department, final String userId) {
        return new DeleteRequestBuilder(department, userId);
    }

    public class DeleteRequestBuilder {
        private final String department;
        private final String userId;

        private DeleteRequestBuilder(final String department, final String userId) {
            this.department = department;
            this.userId = userId;
        }

        public Request<User> build() {
            final Map<String, Object> parameters = new HashMap<>();

            parameters.put("department", department);
            parameters.put("userId", userId);

            return new RequestImpl<>(baseUri + "/{department}/users/{userId}", HttpMethod.DELETE, User.class, parameters);
        }
    }

    public PostRequestBuilder POST(final String department, final User user) {
        return new PostRequestBuilder(department, user);
    }

    public class PostRequestBuilder {
        private final String department;
        private final User user;

        private PostRequestBuilder(final String department, final User user) {
            this.department = department;
            this.user = user;
        }

        public Request<String> build() {
            final Map<String, Object> parameters = new HashMap<>();

            parameters.put("department", department);

            final HttpEntity<User> body = new HttpEntity<>(user);

            return new RequestImpl<>(baseUri + "/{department}/users/{userId}", HttpMethod.DELETE, String.class, parameters, body);
        }
    }

    public interface Request<T> {
        T execute();
    }

    class RequestImpl<T> implements Request<T> {
        private final String uriTemplate;
        private final HttpMethod httpMethod;
        private final Class<T> resultClass;
        private final Map<String, Object> parameters;
        private final HttpEntity<?> entity;

        public RequestImpl(final String uriTemplate, final HttpMethod httpMethod, final Class<T> resultClass, final Map<String, Object> parameters) {
            this(uriTemplate, httpMethod, resultClass, parameters, null);
        }

        public RequestImpl(final String uriTemplate, final HttpMethod httpMethod, final Class<T> resultClass, final Map<String, Object> parameters, final HttpEntity<?> entity) {
            this.uriTemplate = uriTemplate;
            this.httpMethod = httpMethod;
            this.resultClass = resultClass;
            this.parameters = parameters;
            this.entity = entity;
        }

        public T execute() {
            return restTemplate.exchange(uriTemplate, httpMethod, entity, resultClass, parameters).getBody();
        }
    }
}

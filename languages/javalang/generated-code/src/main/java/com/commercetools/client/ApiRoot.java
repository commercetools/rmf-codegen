package com.commercetools.client;

public class ApiRoot {
   
   public static ByProjectKeyRequestBuilder withProjectKeyValue(String projectKey) {
      return new ByProjectKeyRequestBuilder(projectKey);
   }
}
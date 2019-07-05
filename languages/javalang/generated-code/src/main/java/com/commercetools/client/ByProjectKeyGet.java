package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyGet extends HttpRequest {
   
   private String projectKey;
   
   public ByProjectKeyGet(String projectKey){
      this.projectKey = projectKey;
      setMethod(HttpMethod.GET);
      setPath(String.format("/%s", this.projectKey));
      
   }
}
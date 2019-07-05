package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyCategoriesGet extends HttpRequest {
   
   private String projectKey;
   
   public ByProjectKeyCategoriesGet(String projectKey){
      this.projectKey = projectKey;
      setMethod(HttpMethod.GET);
      setPath(String.format("/%s/categories", this.projectKey));
      
   }
}
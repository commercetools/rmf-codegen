package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyCategoriesKeyByKeyGet extends HttpRequest {
   
   private String projectKey;
   
   private String key;
   
   public ByProjectKeyCategoriesKeyByKeyGet(String projectKey, String key){
      this.projectKey = projectKey;
      this.key = key;
      setMethod(HttpMethod.GET);
      setPath(String.format("/%s/categories/key=%s", this.projectKey, this.key));
      
   }
}
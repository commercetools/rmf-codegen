package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyCategoriesKeyByKeyDelete extends HttpRequest {
   
   private String projectKey;
   
   private String key;
   
   public ByProjectKeyCategoriesKeyByKeyDelete(String projectKey, String key){
      this.projectKey = projectKey;
      this.key = key;
      setMethod(HttpMethod.DELETE);
      setPath(String.format("/%s/categories/key=%s", this.projectKey, this.key));
      
   }
}
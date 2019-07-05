package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyCategoriesByIDGet extends HttpRequest {
   
   private String projectKey;
   
   private String ID;
   
   public ByProjectKeyCategoriesByIDGet(String projectKey, String ID){
      this.projectKey = projectKey;
      this.ID = ID;
      setMethod(HttpMethod.GET);
      setPath(String.format("/%s/categories/%s", this.projectKey, this.ID));
      
   }
}
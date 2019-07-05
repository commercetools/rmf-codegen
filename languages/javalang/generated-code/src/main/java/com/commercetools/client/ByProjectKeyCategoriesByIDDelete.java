package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyCategoriesByIDDelete extends HttpRequest {
   
   private String projectKey;
   
   private String ID;
   
   public ByProjectKeyCategoriesByIDDelete(String projectKey, String ID){
      this.projectKey = projectKey;
      this.ID = ID;
      setMethod(HttpMethod.DELETE);
      setPath(String.format("/%s/categories/%s", this.projectKey, this.ID));
      
   }
}
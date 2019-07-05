package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyCategoriesByIDPost extends HttpRequest {
   
   private String projectKey;
   
   private String ID;
   
   private com.commercetools.models.Common.Update update;
   
   public ByProjectKeyCategoriesByIDPost(String projectKey, String ID, com.commercetools.models.Common.Update update){
      this.projectKey = projectKey;
      this.ID = ID;
      this.update = update;
      setMethod(HttpMethod.POST);
      setPath(String.format("/%s/categories/%s", this.projectKey, this.ID));
      setBody(update.toJson());
   }
}
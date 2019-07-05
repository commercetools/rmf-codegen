package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyCategoriesKeyByKeyPost extends HttpRequest {
   
   private String projectKey;
   
   private String key;
   
   private com.commercetools.models.Common.Update update;
   
   public ByProjectKeyCategoriesKeyByKeyPost(String projectKey, String key, com.commercetools.models.Common.Update update){
      this.projectKey = projectKey;
      this.key = key;
      this.update = update;
      setMethod(HttpMethod.POST);
      setPath(String.format("/%s/categories/key=%s", this.projectKey, this.key));
      setBody(update.toJson());
   }
}
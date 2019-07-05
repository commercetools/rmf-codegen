package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyPost extends HttpRequest {
   
   private String projectKey;
   
   private com.commercetools.models.Common.Update update;
   
   public ByProjectKeyPost(String projectKey, com.commercetools.models.Common.Update update){
      this.projectKey = projectKey;
      this.update = update;
      setMethod(HttpMethod.POST);
      setPath(String.format("/%s", this.projectKey));
      setBody(update.toJson());
   }
}
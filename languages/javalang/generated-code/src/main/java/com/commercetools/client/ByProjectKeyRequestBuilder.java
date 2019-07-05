package com.commercetools.client;

public class ByProjectKeyRequestBuilder {
   
   private String projectKey;
   
   public ByProjectKeyRequestBuilder (String projectKey) {
      this.projectKey = projectKey;
   }
   
   public ByProjectKeyGet get() {
      return new ByProjectKeyGet(projectKey);
   }
   
   public ByProjectKeyPost post(com.commercetools.models.Common.Update update) {
      return new ByProjectKeyPost(projectKey, update);
   }
   
   public ByProjectKeyCategoriesRequestBuilder categories() {
      return new ByProjectKeyCategoriesRequestBuilder(projectKey);
   }
}

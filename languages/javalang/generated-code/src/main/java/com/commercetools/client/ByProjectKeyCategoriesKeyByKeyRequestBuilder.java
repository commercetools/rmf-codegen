package com.commercetools.client;

public class ByProjectKeyCategoriesKeyByKeyRequestBuilder {
   
   private String projectKey;
   private String key;
   
   public ByProjectKeyCategoriesKeyByKeyRequestBuilder (String projectKey,String key) {
      this.projectKey = projectKey;
   this.key = key;
   }
   
   public ByProjectKeyCategoriesKeyByKeyGet get() {
      return new ByProjectKeyCategoriesKeyByKeyGet(projectKey, key);
   }
   
   public ByProjectKeyCategoriesKeyByKeyPost post(com.commercetools.models.Common.Update update) {
      return new ByProjectKeyCategoriesKeyByKeyPost(projectKey, key, update);
   }
   
   public ByProjectKeyCategoriesKeyByKeyDelete delete() {
      return new ByProjectKeyCategoriesKeyByKeyDelete(projectKey, key);
   }
   
}

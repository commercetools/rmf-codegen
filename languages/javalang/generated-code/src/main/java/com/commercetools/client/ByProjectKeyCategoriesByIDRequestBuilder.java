package com.commercetools.client;

public class ByProjectKeyCategoriesByIDRequestBuilder {
   
   private String projectKey;
   private String ID;
   
   public ByProjectKeyCategoriesByIDRequestBuilder (String projectKey,String ID) {
      this.projectKey = projectKey;
   this.ID = ID;
   }
   
   public ByProjectKeyCategoriesByIDGet get() {
      return new ByProjectKeyCategoriesByIDGet(projectKey, ID);
   }
   
   public ByProjectKeyCategoriesByIDPost post(com.commercetools.models.Common.Update update) {
      return new ByProjectKeyCategoriesByIDPost(projectKey, ID, update);
   }
   
   public ByProjectKeyCategoriesByIDDelete delete() {
      return new ByProjectKeyCategoriesByIDDelete(projectKey, ID);
   }
   
}

package com.commercetools.client;

public class ByProjectKeyCategoriesRequestBuilder {
   
   private String projectKey;
   
   public ByProjectKeyCategoriesRequestBuilder (String projectKey) {
      this.projectKey = projectKey;
   }
   
   public ByProjectKeyCategoriesGet get() {
      return new ByProjectKeyCategoriesGet(projectKey);
   }
   
   public ByProjectKeyCategoriesPost post(com.commercetools.models.Category.CategoryDraft categoryDraft) {
      return new ByProjectKeyCategoriesPost(projectKey, categoryDraft);
   }
   
   public ByProjectKeyCategoriesKeyByKeyRequestBuilder withKey(String key) {
      return new ByProjectKeyCategoriesKeyByKeyRequestBuilder(projectKey, key);
   }
   public ByProjectKeyCategoriesByIDRequestBuilder withId(String ID) {
      return new ByProjectKeyCategoriesByIDRequestBuilder(projectKey, ID);
   }
}

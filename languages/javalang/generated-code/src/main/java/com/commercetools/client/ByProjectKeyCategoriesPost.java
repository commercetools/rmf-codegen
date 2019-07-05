package com.commercetools.client;

import client.HttpRequest;
import client.HttpMethod;

public class ByProjectKeyCategoriesPost extends HttpRequest {
   
   private String projectKey;
   
   private com.commercetools.models.Category.CategoryDraft categoryDraft;
   
   public ByProjectKeyCategoriesPost(String projectKey, com.commercetools.models.Category.CategoryDraft categoryDraft){
      this.projectKey = projectKey;
      this.categoryDraft = categoryDraft;
      setMethod(HttpMethod.POST);
      setPath(String.format("/%s/categories", this.projectKey));
      setBody(categoryDraft.toJson());
   }
}
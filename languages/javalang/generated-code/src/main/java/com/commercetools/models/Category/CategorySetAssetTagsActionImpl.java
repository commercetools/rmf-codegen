package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import java.lang.String;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.time.*;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import json.CommercetoolsJsonUtils;

@Generated(
    value = "io.vrap.rmf.codegen.rendring.CoreCodeGenerator",
    comments = "https://github.com/vrapio/rmf-codegen"
)
public class CategorySetAssetTagsActionImpl implements CategorySetAssetTagsAction {

   private String assetId;
   
   private String assetKey;
   
   private List<String> tags;

   @JsonCreator
   CategorySetAssetTagsActionImpl(@JsonProperty("assetId") final String assetId, @JsonProperty("assetKey") final String assetKey, @JsonProperty("tags") final List<String> tags) {
      this.assetId = assetId;
      this.assetKey = assetKey;
      this.tags = tags;
   }
   public CategorySetAssetTagsActionImpl() {
      
   }
   
   
   public String getAssetId(){
      return this.assetId;
   }
   
   
   public String getAssetKey(){
      return this.assetKey;
   }
   
   
   public List<String> getTags(){
      return this.tags;
   }

   public void setAssetId(final String assetId){
      this.assetId = assetId;
   }
   
   public void setAssetKey(final String assetKey){
      this.assetKey = assetKey;
   }
   
   public void setTags(final List<String> tags){
      this.tags = tags;
   }
   
   public String toJson() {
      try {
          return CommercetoolsJsonUtils.toJsonString(this);
      }catch(JsonProcessingException e) {
          e.printStackTrace();
      }
      return null;
   }

}
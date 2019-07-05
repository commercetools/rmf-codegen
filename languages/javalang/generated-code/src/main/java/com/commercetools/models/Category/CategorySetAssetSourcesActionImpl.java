package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import com.commercetools.models.Common.AssetSource;
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
public class CategorySetAssetSourcesActionImpl implements CategorySetAssetSourcesAction {

   private List<AssetSource> sources;
   
   private String assetId;
   
   private String assetKey;

   @JsonCreator
   CategorySetAssetSourcesActionImpl(@JsonProperty("sources") final List<AssetSource> sources, @JsonProperty("assetId") final String assetId, @JsonProperty("assetKey") final String assetKey) {
      this.sources = sources;
      this.assetId = assetId;
      this.assetKey = assetKey;
   }
   public CategorySetAssetSourcesActionImpl() {
      
   }
   
   
   public List<AssetSource> getSources(){
      return this.sources;
   }
   
   
   public String getAssetId(){
      return this.assetId;
   }
   
   
   public String getAssetKey(){
      return this.assetKey;
   }

   public void setSources(final List<AssetSource> sources){
      this.sources = sources;
   }
   
   public void setAssetId(final String assetId){
      this.assetId = assetId;
   }
   
   public void setAssetKey(final String assetKey){
      this.assetKey = assetKey;
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
package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import com.commercetools.models.Common.LocalizedString;
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
public class CategoryChangeAssetNameActionImpl implements CategoryChangeAssetNameAction {

   private String assetId;
   
   private com.commercetools.models.Common.LocalizedString name;
   
   private String assetKey;

   @JsonCreator
   CategoryChangeAssetNameActionImpl(@JsonProperty("assetId") final String assetId, @JsonProperty("name") final com.commercetools.models.Common.LocalizedString name, @JsonProperty("assetKey") final String assetKey) {
      this.assetId = assetId;
      this.name = name;
      this.assetKey = assetKey;
   }
   public CategoryChangeAssetNameActionImpl() {
      
   }
   
   
   public String getAssetId(){
      return this.assetId;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getName(){
      return this.name;
   }
   
   
   public String getAssetKey(){
      return this.assetKey;
   }

   public void setAssetId(final String assetId){
      this.assetId = assetId;
   }
   
   public void setName(final com.commercetools.models.Common.LocalizedString name){
      this.name = name;
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
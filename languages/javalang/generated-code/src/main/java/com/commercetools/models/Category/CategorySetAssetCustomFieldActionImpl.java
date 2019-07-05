package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import java.lang.Object;
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
public class CategorySetAssetCustomFieldActionImpl implements CategorySetAssetCustomFieldAction {

   private String assetId;
   
   private String name;
   
   private Object value;
   
   private String assetKey;

   @JsonCreator
   CategorySetAssetCustomFieldActionImpl(@JsonProperty("assetId") final String assetId, @JsonProperty("name") final String name, @JsonProperty("value") final Object value, @JsonProperty("assetKey") final String assetKey) {
      this.assetId = assetId;
      this.name = name;
      this.value = value;
      this.assetKey = assetKey;
   }
   public CategorySetAssetCustomFieldActionImpl() {
      
   }
   
   
   public String getAssetId(){
      return this.assetId;
   }
   
   
   public String getName(){
      return this.name;
   }
   
   
   public Object getValue(){
      return this.value;
   }
   
   
   public String getAssetKey(){
      return this.assetKey;
   }

   public void setAssetId(final String assetId){
      this.assetId = assetId;
   }
   
   public void setName(final String name){
      this.name = name;
   }
   
   public void setValue(final Object value){
      this.value = value;
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
package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import com.commercetools.models.Type.TypeReference;
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
public class CategorySetAssetCustomTypeActionImpl implements CategorySetAssetCustomTypeAction {

   private String assetId;
   
   private java.lang.Object fields;
   
   private com.commercetools.models.Type.TypeReference type;
   
   private String assetKey;

   @JsonCreator
   CategorySetAssetCustomTypeActionImpl(@JsonProperty("assetId") final String assetId, @JsonProperty("fields") final java.lang.Object fields, @JsonProperty("type") final com.commercetools.models.Type.TypeReference type, @JsonProperty("assetKey") final String assetKey) {
      this.assetId = assetId;
      this.fields = fields;
      this.type = type;
      this.assetKey = assetKey;
   }
   public CategorySetAssetCustomTypeActionImpl() {
      
   }
   
   
   public String getAssetId(){
      return this.assetId;
   }
   
   
   public java.lang.Object getFields(){
      return this.fields;
   }
   
   
   public com.commercetools.models.Type.TypeReference getType(){
      return this.type;
   }
   
   
   public String getAssetKey(){
      return this.assetKey;
   }

   public void setAssetId(final String assetId){
      this.assetId = assetId;
   }
   
   public void setFields(final java.lang.Object fields){
      this.fields = fields;
   }
   
   public void setType(final com.commercetools.models.Type.TypeReference type){
      this.type = type;
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
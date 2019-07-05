package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import com.commercetools.models.Type.TypeReference;
import java.lang.Object;
import java.lang.String;
import com.commercetools.models.Category.CategorySetAssetCustomTypeActionImpl;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.time.*;

import json.CommercetoolsJsonUtils;
import java.io.IOException;

@Generated(
    value = "io.vrap.rmf.codegen.rendring.CoreCodeGenerator",
    comments = "https://github.com/vrapio/rmf-codegen"
)
@JsonDeserialize(as = CategorySetAssetCustomTypeActionImpl.class)
public interface CategorySetAssetCustomTypeAction extends CategoryUpdateAction {

   
   
   @JsonProperty("assetId")
   public String getAssetId();
   
   
   @JsonProperty("assetKey")
   public String getAssetKey();
   
   @Valid
   @JsonProperty("type")
   public TypeReference getType();
   
   @Valid
   @JsonProperty("fields")
   public Object getFields();

   public void setAssetId(final String assetId);
   
   public void setAssetKey(final String assetKey);
   
   public void setType(final TypeReference type);
   
   public void setFields(final Object fields);

   public String toJson();
   
   public static CategorySetAssetCustomTypeActionImpl of(){
      return new CategorySetAssetCustomTypeActionImpl();
   }
   
   
   public static CategorySetAssetCustomTypeActionImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, CategorySetAssetCustomTypeActionImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
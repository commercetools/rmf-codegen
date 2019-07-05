package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import java.lang.String;
import com.commercetools.models.Category.CategoryRemoveAssetActionImpl;

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
@JsonDeserialize(as = CategoryRemoveAssetActionImpl.class)
public interface CategoryRemoveAssetAction extends CategoryUpdateAction {

   
   
   @JsonProperty("assetId")
   public String getAssetId();
   
   
   @JsonProperty("assetKey")
   public String getAssetKey();

   public void setAssetId(final String assetId);
   
   public void setAssetKey(final String assetKey);

   public String toJson();
   
   public static CategoryRemoveAssetActionImpl of(){
      return new CategoryRemoveAssetActionImpl();
   }
   
   
   public static CategoryRemoveAssetActionImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, CategoryRemoveAssetActionImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
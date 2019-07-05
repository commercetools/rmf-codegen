package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import java.lang.Object;
import java.lang.String;
import com.commercetools.models.Category.CategorySetCustomFieldActionImpl;

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
@JsonDeserialize(as = CategorySetCustomFieldActionImpl.class)
public interface CategorySetCustomFieldAction extends CategoryUpdateAction {

   
   @NotNull
   @JsonProperty("name")
   public String getName();
   
   
   @JsonProperty("value")
   public Object getValue();

   public void setName(final String name);
   
   public void setValue(final Object value);

   public String toJson();
   
   public static CategorySetCustomFieldActionImpl of(){
      return new CategorySetCustomFieldActionImpl();
   }
   
   
   public static CategorySetCustomFieldActionImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, CategorySetCustomFieldActionImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
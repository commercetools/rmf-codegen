package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import com.commercetools.models.Type.FieldContainer;
import com.commercetools.models.Type.TypeReference;
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
public class CategorySetCustomTypeActionImpl implements CategorySetCustomTypeAction {

   private com.commercetools.models.Type.FieldContainer fields;
   
   private com.commercetools.models.Type.TypeReference type;

   @JsonCreator
   CategorySetCustomTypeActionImpl(@JsonProperty("fields") final com.commercetools.models.Type.FieldContainer fields, @JsonProperty("type") final com.commercetools.models.Type.TypeReference type) {
      this.fields = fields;
      this.type = type;
   }
   public CategorySetCustomTypeActionImpl() {
      
   }
   
   
   public com.commercetools.models.Type.FieldContainer getFields(){
      return this.fields;
   }
   
   
   public com.commercetools.models.Type.TypeReference getType(){
      return this.type;
   }

   public void setFields(final com.commercetools.models.Type.FieldContainer fields){
      this.fields = fields;
   }
   
   public void setType(final com.commercetools.models.Type.TypeReference type){
      this.type = type;
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
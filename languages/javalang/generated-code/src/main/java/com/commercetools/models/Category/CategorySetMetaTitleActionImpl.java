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
public class CategorySetMetaTitleActionImpl implements CategorySetMetaTitleAction {

   private com.commercetools.models.Common.LocalizedString metaTitle;

   @JsonCreator
   CategorySetMetaTitleActionImpl(@JsonProperty("metaTitle") final com.commercetools.models.Common.LocalizedString metaTitle) {
      this.metaTitle = metaTitle;
   }
   public CategorySetMetaTitleActionImpl() {
      
   }
   
   
   public com.commercetools.models.Common.LocalizedString getMetaTitle(){
      return this.metaTitle;
   }

   public void setMetaTitle(final com.commercetools.models.Common.LocalizedString metaTitle){
      this.metaTitle = metaTitle;
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
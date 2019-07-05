package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryUpdateAction;
import java.lang.String;
import com.commercetools.models.Category.CategoryChangeOrderHintActionImpl;

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
@JsonDeserialize(as = CategoryChangeOrderHintActionImpl.class)
public interface CategoryChangeOrderHintAction extends CategoryUpdateAction {

   
   @NotNull
   @JsonProperty("orderHint")
   public String getOrderHint();

   public void setOrderHint(final String orderHint);

   public String toJson();
   
   public static CategoryChangeOrderHintActionImpl of(){
      return new CategoryChangeOrderHintActionImpl();
   }
   
   
   public static CategoryChangeOrderHintActionImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, CategoryChangeOrderHintActionImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
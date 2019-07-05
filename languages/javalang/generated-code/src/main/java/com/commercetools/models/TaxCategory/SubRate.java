package com.commercetools.models.TaxCategory;

import java.lang.Integer;
import java.lang.String;
import com.commercetools.models.TaxCategory.SubRateImpl;

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
@JsonDeserialize(as = SubRateImpl.class)
public interface SubRate  {

   
   @NotNull
   @JsonProperty("name")
   public String getName();
   
   @NotNull
   @JsonProperty("amount")
   public Integer getAmount();

   public void setName(final String name);
   
   public void setAmount(final Integer amount);

   public String toJson();
   
   public static SubRateImpl of(){
      return new SubRateImpl();
   }
   
   
   public static SubRateImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, SubRateImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
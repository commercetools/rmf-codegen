package com.commercetools.models.Common;

import com.commercetools.models.Common.Money;
import java.lang.Long;
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
public class PriceTierImpl implements PriceTier {

   private com.commercetools.models.Common.Money value;
   
   private Long minimumQuantity;

   @JsonCreator
   PriceTierImpl(@JsonProperty("value") final com.commercetools.models.Common.Money value, @JsonProperty("minimumQuantity") final Long minimumQuantity) {
      this.value = value;
      this.minimumQuantity = minimumQuantity;
   }
   public PriceTierImpl() {
      
   }
   
   
   public com.commercetools.models.Common.Money getValue(){
      return this.value;
   }
   
   
   public Long getMinimumQuantity(){
      return this.minimumQuantity;
   }

   public void setValue(final com.commercetools.models.Common.Money value){
      this.value = value;
   }
   
   public void setMinimumQuantity(final Long minimumQuantity){
      this.minimumQuantity = minimumQuantity;
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
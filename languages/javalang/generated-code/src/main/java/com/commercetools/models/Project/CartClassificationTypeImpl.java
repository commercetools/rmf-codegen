package com.commercetools.models.Project;

import com.commercetools.models.Project.ShippingRateInputType;
import com.commercetools.models.ShippingMethod.ShippingRateTierType;
import com.commercetools.models.Type.CustomFieldLocalizedEnumValue;
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
public class CartClassificationTypeImpl implements CartClassificationType {

   private List<CustomFieldLocalizedEnumValue> values;

   @JsonCreator
   CartClassificationTypeImpl(@JsonProperty("values") final List<CustomFieldLocalizedEnumValue> values) {
      this.values = values;
   }
   public CartClassificationTypeImpl() {
      
   }
   
   
   public List<CustomFieldLocalizedEnumValue> getValues(){
      return this.values;
   }

   public void setValues(final List<CustomFieldLocalizedEnumValue> values){
      this.values = values;
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
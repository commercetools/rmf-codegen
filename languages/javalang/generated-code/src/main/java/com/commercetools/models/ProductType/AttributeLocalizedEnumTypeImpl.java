package com.commercetools.models.ProductType;

import com.commercetools.models.ProductType.AttributeLocalizedEnumValue;
import com.commercetools.models.ProductType.AttributeType;
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
public class AttributeLocalizedEnumTypeImpl implements AttributeLocalizedEnumType {

   private List<AttributeLocalizedEnumValue> values;

   @JsonCreator
   AttributeLocalizedEnumTypeImpl(@JsonProperty("values") final List<AttributeLocalizedEnumValue> values) {
      this.values = values;
   }
   public AttributeLocalizedEnumTypeImpl() {
      
   }
   
   
   public List<AttributeLocalizedEnumValue> getValues(){
      return this.values;
   }

   public void setValues(final List<AttributeLocalizedEnumValue> values){
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
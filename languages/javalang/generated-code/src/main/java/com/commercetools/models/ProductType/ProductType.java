package com.commercetools.models.ProductType;

import com.commercetools.models.Common.LoggedResource;
import com.commercetools.models.ProductType.AttributeDefinition;
import java.lang.String;
import com.commercetools.models.ProductType.ProductTypeImpl;

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
@JsonDeserialize(as = ProductTypeImpl.class)
public interface ProductType extends LoggedResource {

   
   
   @JsonProperty("key")
   public String getKey();
   
   @NotNull
   @JsonProperty("name")
   public String getName();
   
   @NotNull
   @JsonProperty("description")
   public String getDescription();
   
   @Valid
   @JsonProperty("attributes")
   public List<AttributeDefinition> getAttributes();

   public void setKey(final String key);
   
   public void setName(final String name);
   
   public void setDescription(final String description);
   
   public void setAttributes(final List<AttributeDefinition> attributes);

   public String toJson();
   
   public static ProductTypeImpl of(){
      return new ProductTypeImpl();
   }
   
   
   public static ProductTypeImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, ProductTypeImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
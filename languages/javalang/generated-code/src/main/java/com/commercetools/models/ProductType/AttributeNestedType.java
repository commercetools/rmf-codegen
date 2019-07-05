package com.commercetools.models.ProductType;

import com.commercetools.models.ProductType.AttributeType;
import com.commercetools.models.ProductType.ProductTypeReference;
import java.lang.String;
import com.commercetools.models.ProductType.AttributeNestedTypeImpl;

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
@JsonDeserialize(as = AttributeNestedTypeImpl.class)
public interface AttributeNestedType extends AttributeType {

   
   @NotNull
   @Valid
   @JsonProperty("typeReference")
   public ProductTypeReference getTypeReference();

   public void setTypeReference(final ProductTypeReference typeReference);

   public String toJson();
   
   public static AttributeNestedTypeImpl of(){
      return new AttributeNestedTypeImpl();
   }
   
   
   public static AttributeNestedTypeImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, AttributeNestedTypeImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
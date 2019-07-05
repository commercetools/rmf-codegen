package com.commercetools.models.Product;

import com.commercetools.models.Product.ProductVariantChannelAvailabilityMap;
import java.lang.Boolean;
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
public class ProductVariantAvailabilityImpl implements ProductVariantAvailability {

   private Long availableQuantity;
   
   private com.commercetools.models.Product.ProductVariantChannelAvailabilityMap channels;
   
   private Long restockableInDays;
   
   private Boolean isOnStock;

   @JsonCreator
   ProductVariantAvailabilityImpl(@JsonProperty("availableQuantity") final Long availableQuantity, @JsonProperty("channels") final com.commercetools.models.Product.ProductVariantChannelAvailabilityMap channels, @JsonProperty("restockableInDays") final Long restockableInDays, @JsonProperty("isOnStock") final Boolean isOnStock) {
      this.availableQuantity = availableQuantity;
      this.channels = channels;
      this.restockableInDays = restockableInDays;
      this.isOnStock = isOnStock;
   }
   public ProductVariantAvailabilityImpl() {
      
   }
   
   
   public Long getAvailableQuantity(){
      return this.availableQuantity;
   }
   
   
   public com.commercetools.models.Product.ProductVariantChannelAvailabilityMap getChannels(){
      return this.channels;
   }
   
   
   public Long getRestockableInDays(){
      return this.restockableInDays;
   }
   
   
   public Boolean getIsOnStock(){
      return this.isOnStock;
   }

   public void setAvailableQuantity(final Long availableQuantity){
      this.availableQuantity = availableQuantity;
   }
   
   public void setChannels(final com.commercetools.models.Product.ProductVariantChannelAvailabilityMap channels){
      this.channels = channels;
   }
   
   public void setRestockableInDays(final Long restockableInDays){
      this.restockableInDays = restockableInDays;
   }
   
   public void setIsOnStock(final Boolean isOnStock){
      this.isOnStock = isOnStock;
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
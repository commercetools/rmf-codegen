package com.commercetools.models.Product;

import com.commercetools.models.Product.ProductVariantChannelAvailabilityMap;
import java.lang.Boolean;
import java.lang.Long;
import com.commercetools.models.Product.ProductVariantAvailabilityImpl;

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
@JsonDeserialize(as = ProductVariantAvailabilityImpl.class)
public interface ProductVariantAvailability  {

   
   
   @JsonProperty("isOnStock")
   public Boolean getIsOnStock();
   
   
   @JsonProperty("restockableInDays")
   public Long getRestockableInDays();
   
   
   @JsonProperty("availableQuantity")
   public Long getAvailableQuantity();
   
   @Valid
   @JsonProperty("channels")
   public ProductVariantChannelAvailabilityMap getChannels();

   public void setIsOnStock(final Boolean isOnStock);
   
   public void setRestockableInDays(final Long restockableInDays);
   
   public void setAvailableQuantity(final Long availableQuantity);
   
   public void setChannels(final ProductVariantChannelAvailabilityMap channels);

   public String toJson();
   
   public static ProductVariantAvailabilityImpl of(){
      return new ProductVariantAvailabilityImpl();
   }
   
   
   public static ProductVariantAvailabilityImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, ProductVariantAvailabilityImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
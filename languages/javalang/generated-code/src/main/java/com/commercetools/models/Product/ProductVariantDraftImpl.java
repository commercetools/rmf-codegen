package com.commercetools.models.Product;

import com.commercetools.models.Common.AssetDraft;
import com.commercetools.models.Common.Image;
import com.commercetools.models.Common.PriceDraft;
import com.commercetools.models.Product.Attribute;
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
public class ProductVariantDraftImpl implements ProductVariantDraft {

   private List<Image> images;
   
   private List<AssetDraft> assets;
   
   private List<Attribute> attributes;
   
   private List<PriceDraft> prices;
   
   private String sku;
   
   private String key;

   @JsonCreator
   ProductVariantDraftImpl(@JsonProperty("images") final List<Image> images, @JsonProperty("assets") final List<AssetDraft> assets, @JsonProperty("attributes") final List<Attribute> attributes, @JsonProperty("prices") final List<PriceDraft> prices, @JsonProperty("sku") final String sku, @JsonProperty("key") final String key) {
      this.images = images;
      this.assets = assets;
      this.attributes = attributes;
      this.prices = prices;
      this.sku = sku;
      this.key = key;
   }
   public ProductVariantDraftImpl() {
      
   }
   
   
   public List<Image> getImages(){
      return this.images;
   }
   
   
   public List<AssetDraft> getAssets(){
      return this.assets;
   }
   
   
   public List<Attribute> getAttributes(){
      return this.attributes;
   }
   
   
   public List<PriceDraft> getPrices(){
      return this.prices;
   }
   
   
   public String getSku(){
      return this.sku;
   }
   
   
   public String getKey(){
      return this.key;
   }

   public void setImages(final List<Image> images){
      this.images = images;
   }
   
   public void setAssets(final List<AssetDraft> assets){
      this.assets = assets;
   }
   
   public void setAttributes(final List<Attribute> attributes){
      this.attributes = attributes;
   }
   
   public void setPrices(final List<PriceDraft> prices){
      this.prices = prices;
   }
   
   public void setSku(final String sku){
      this.sku = sku;
   }
   
   public void setKey(final String key){
      this.key = key;
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
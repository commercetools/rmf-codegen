package com.commercetools.models.Product;

import com.commercetools.models.Common.Asset;
import com.commercetools.models.Common.Image;
import com.commercetools.models.Common.Price;
import com.commercetools.models.Common.ScopedPrice;
import com.commercetools.models.Product.Attribute;
import com.commercetools.models.Product.ProductVariantAvailability;
import java.lang.Boolean;
import java.lang.Long;
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
public class ProductVariantImpl implements ProductVariant {

   private com.commercetools.models.Common.ScopedPrice scopedPrice;
   
   private List<Image> images;
   
   private List<Asset> assets;
   
   private Boolean isMatchingVariant;
   
   private com.commercetools.models.Common.Price price;
   
   private Boolean scopedPriceDiscounted;
   
   private List<Attribute> attributes;
   
   private com.commercetools.models.Product.ProductVariantAvailability availability;
   
   private Long id;
   
   private List<Price> prices;
   
   private String sku;
   
   private String key;

   @JsonCreator
   ProductVariantImpl(@JsonProperty("scopedPrice") final com.commercetools.models.Common.ScopedPrice scopedPrice, @JsonProperty("images") final List<Image> images, @JsonProperty("assets") final List<Asset> assets, @JsonProperty("isMatchingVariant") final Boolean isMatchingVariant, @JsonProperty("price") final com.commercetools.models.Common.Price price, @JsonProperty("scopedPriceDiscounted") final Boolean scopedPriceDiscounted, @JsonProperty("attributes") final List<Attribute> attributes, @JsonProperty("availability") final com.commercetools.models.Product.ProductVariantAvailability availability, @JsonProperty("id") final Long id, @JsonProperty("prices") final List<Price> prices, @JsonProperty("sku") final String sku, @JsonProperty("key") final String key) {
      this.scopedPrice = scopedPrice;
      this.images = images;
      this.assets = assets;
      this.isMatchingVariant = isMatchingVariant;
      this.price = price;
      this.scopedPriceDiscounted = scopedPriceDiscounted;
      this.attributes = attributes;
      this.availability = availability;
      this.id = id;
      this.prices = prices;
      this.sku = sku;
      this.key = key;
   }
   public ProductVariantImpl() {
      
   }
   
   
   public com.commercetools.models.Common.ScopedPrice getScopedPrice(){
      return this.scopedPrice;
   }
   
   
   public List<Image> getImages(){
      return this.images;
   }
   
   
   public List<Asset> getAssets(){
      return this.assets;
   }
   
   
   public Boolean getIsMatchingVariant(){
      return this.isMatchingVariant;
   }
   
   
   public com.commercetools.models.Common.Price getPrice(){
      return this.price;
   }
   
   
   public Boolean getScopedPriceDiscounted(){
      return this.scopedPriceDiscounted;
   }
   
   
   public List<Attribute> getAttributes(){
      return this.attributes;
   }
   
   
   public com.commercetools.models.Product.ProductVariantAvailability getAvailability(){
      return this.availability;
   }
   
   
   public Long getId(){
      return this.id;
   }
   
   
   public List<Price> getPrices(){
      return this.prices;
   }
   
   
   public String getSku(){
      return this.sku;
   }
   
   
   public String getKey(){
      return this.key;
   }

   public void setScopedPrice(final com.commercetools.models.Common.ScopedPrice scopedPrice){
      this.scopedPrice = scopedPrice;
   }
   
   public void setImages(final List<Image> images){
      this.images = images;
   }
   
   public void setAssets(final List<Asset> assets){
      this.assets = assets;
   }
   
   public void setIsMatchingVariant(final Boolean isMatchingVariant){
      this.isMatchingVariant = isMatchingVariant;
   }
   
   public void setPrice(final com.commercetools.models.Common.Price price){
      this.price = price;
   }
   
   public void setScopedPriceDiscounted(final Boolean scopedPriceDiscounted){
      this.scopedPriceDiscounted = scopedPriceDiscounted;
   }
   
   public void setAttributes(final List<Attribute> attributes){
      this.attributes = attributes;
   }
   
   public void setAvailability(final com.commercetools.models.Product.ProductVariantAvailability availability){
      this.availability = availability;
   }
   
   public void setId(final Long id){
      this.id = id;
   }
   
   public void setPrices(final List<Price> prices){
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
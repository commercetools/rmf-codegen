package com.commercetools.models.Product;

import com.commercetools.models.Common.AssetDraft;
import com.commercetools.models.Common.Image;
import com.commercetools.models.Common.PriceDraft;
import com.commercetools.models.Product.Attribute;
import java.lang.String;
import com.commercetools.models.Product.ProductVariantDraft;
import javax.annotation.Nullable;
import java.util.List;

public final class ProductVariantDraftBuilder {
   
   @Nullable
   private List<Image> images;
   
   @Nullable
   private List<AssetDraft> assets;
   
   @Nullable
   private List<Attribute> attributes;
   
   @Nullable
   private List<PriceDraft> prices;
   
   @Nullable
   private String sku;
   
   @Nullable
   private String key;
   
   public ProductVariantDraftBuilder images(@Nullable final List<Image> images) {
      this.images = images;
      return this;
   }
   
   public ProductVariantDraftBuilder assets(@Nullable final List<AssetDraft> assets) {
      this.assets = assets;
      return this;
   }
   
   public ProductVariantDraftBuilder attributes(@Nullable final List<Attribute> attributes) {
      this.attributes = attributes;
      return this;
   }
   
   public ProductVariantDraftBuilder prices(@Nullable final List<PriceDraft> prices) {
      this.prices = prices;
      return this;
   }
   
   public ProductVariantDraftBuilder sku(@Nullable final String sku) {
      this.sku = sku;
      return this;
   }
   
   public ProductVariantDraftBuilder key(@Nullable final String key) {
      this.key = key;
      return this;
   }
   
   @Nullable
   public List<Image> getImages(){
      return this.images;
   }
   
   @Nullable
   public List<AssetDraft> getAssets(){
      return this.assets;
   }
   
   @Nullable
   public List<Attribute> getAttributes(){
      return this.attributes;
   }
   
   @Nullable
   public List<PriceDraft> getPrices(){
      return this.prices;
   }
   
   @Nullable
   public String getSku(){
      return this.sku;
   }
   
   @Nullable
   public String getKey(){
      return this.key;
   }

   public ProductVariantDraft build() {
       return new ProductVariantDraftImpl(images, assets, attributes, prices, sku, key);
   }
   
   
   public static ProductVariantDraftBuilder of() {
      return new ProductVariantDraftBuilder();
   }
   
   
   public static ProductVariantDraftBuilder of(final ProductVariantDraft template) {
      ProductVariantDraftBuilder builder = new ProductVariantDraftBuilder();
      builder.images = template.getImages();
      builder.assets = template.getAssets();
      builder.attributes = template.getAttributes();
      builder.prices = template.getPrices();
      builder.sku = template.getSku();
      builder.key = template.getKey();
      return builder;
   }
   
}
package com.commercetools.models.Product;

import com.commercetools.models.Common.LoggedResource;
import com.commercetools.models.Product.ProductCatalogData;
import com.commercetools.models.ProductType.ProductTypeReference;
import com.commercetools.models.Review.ReviewRatingStatistics;
import com.commercetools.models.State.StateReference;
import com.commercetools.models.TaxCategory.TaxCategoryReference;
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
public class ProductImpl implements Product {

   private ZonedDateTime createdAt;
   
   private ZonedDateTime lastModifiedAt;
   
   private String id;
   
   private Long version;
   
   private com.commercetools.models.Common.CreatedBy createdBy;
   
   private com.commercetools.models.Common.LastModifiedBy lastModifiedBy;
   
   private com.commercetools.models.Product.ProductCatalogData masterData;
   
   private com.commercetools.models.State.StateReference state;
   
   private com.commercetools.models.Review.ReviewRatingStatistics reviewRatingStatistics;
   
   private com.commercetools.models.ProductType.ProductTypeReference productType;
   
   private String key;
   
   private com.commercetools.models.TaxCategory.TaxCategoryReference taxCategory;

   @JsonCreator
   ProductImpl(@JsonProperty("createdAt") final ZonedDateTime createdAt, @JsonProperty("lastModifiedAt") final ZonedDateTime lastModifiedAt, @JsonProperty("id") final String id, @JsonProperty("version") final Long version, @JsonProperty("createdBy") final com.commercetools.models.Common.CreatedBy createdBy, @JsonProperty("lastModifiedBy") final com.commercetools.models.Common.LastModifiedBy lastModifiedBy, @JsonProperty("masterData") final com.commercetools.models.Product.ProductCatalogData masterData, @JsonProperty("state") final com.commercetools.models.State.StateReference state, @JsonProperty("reviewRatingStatistics") final com.commercetools.models.Review.ReviewRatingStatistics reviewRatingStatistics, @JsonProperty("productType") final com.commercetools.models.ProductType.ProductTypeReference productType, @JsonProperty("key") final String key, @JsonProperty("taxCategory") final com.commercetools.models.TaxCategory.TaxCategoryReference taxCategory) {
      this.createdAt = createdAt;
      this.lastModifiedAt = lastModifiedAt;
      this.id = id;
      this.version = version;
      this.createdBy = createdBy;
      this.lastModifiedBy = lastModifiedBy;
      this.masterData = masterData;
      this.state = state;
      this.reviewRatingStatistics = reviewRatingStatistics;
      this.productType = productType;
      this.key = key;
      this.taxCategory = taxCategory;
   }
   public ProductImpl() {
      
   }
   
   
   public ZonedDateTime getCreatedAt(){
      return this.createdAt;
   }
   
   
   public ZonedDateTime getLastModifiedAt(){
      return this.lastModifiedAt;
   }
   
   
   public String getId(){
      return this.id;
   }
   
   
   public Long getVersion(){
      return this.version;
   }
   
   
   public com.commercetools.models.Common.CreatedBy getCreatedBy(){
      return this.createdBy;
   }
   
   
   public com.commercetools.models.Common.LastModifiedBy getLastModifiedBy(){
      return this.lastModifiedBy;
   }
   
   
   public com.commercetools.models.Product.ProductCatalogData getMasterData(){
      return this.masterData;
   }
   
   
   public com.commercetools.models.State.StateReference getState(){
      return this.state;
   }
   
   
   public com.commercetools.models.Review.ReviewRatingStatistics getReviewRatingStatistics(){
      return this.reviewRatingStatistics;
   }
   
   
   public com.commercetools.models.ProductType.ProductTypeReference getProductType(){
      return this.productType;
   }
   
   
   public String getKey(){
      return this.key;
   }
   
   
   public com.commercetools.models.TaxCategory.TaxCategoryReference getTaxCategory(){
      return this.taxCategory;
   }

   public void setCreatedAt(final ZonedDateTime createdAt){
      this.createdAt = createdAt;
   }
   
   public void setLastModifiedAt(final ZonedDateTime lastModifiedAt){
      this.lastModifiedAt = lastModifiedAt;
   }
   
   public void setId(final String id){
      this.id = id;
   }
   
   public void setVersion(final Long version){
      this.version = version;
   }
   
   public void setCreatedBy(final com.commercetools.models.Common.CreatedBy createdBy){
      this.createdBy = createdBy;
   }
   
   public void setLastModifiedBy(final com.commercetools.models.Common.LastModifiedBy lastModifiedBy){
      this.lastModifiedBy = lastModifiedBy;
   }
   
   public void setMasterData(final com.commercetools.models.Product.ProductCatalogData masterData){
      this.masterData = masterData;
   }
   
   public void setState(final com.commercetools.models.State.StateReference state){
      this.state = state;
   }
   
   public void setReviewRatingStatistics(final com.commercetools.models.Review.ReviewRatingStatistics reviewRatingStatistics){
      this.reviewRatingStatistics = reviewRatingStatistics;
   }
   
   public void setProductType(final com.commercetools.models.ProductType.ProductTypeReference productType){
      this.productType = productType;
   }
   
   public void setKey(final String key){
      this.key = key;
   }
   
   public void setTaxCategory(final com.commercetools.models.TaxCategory.TaxCategoryReference taxCategory){
      this.taxCategory = taxCategory;
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
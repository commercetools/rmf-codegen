package com.commercetools.models.Product;

import com.commercetools.models.Common.LoggedResource;
import com.commercetools.models.Product.ProductCatalogData;
import com.commercetools.models.ProductType.ProductTypeReference;
import com.commercetools.models.Review.ReviewRatingStatistics;
import com.commercetools.models.State.StateReference;
import com.commercetools.models.TaxCategory.TaxCategoryReference;
import java.lang.String;
import com.commercetools.models.Product.ProductImpl;

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
@JsonDeserialize(as = ProductImpl.class)
public interface Product extends LoggedResource {

   
   
   @JsonProperty("key")
   public String getKey();
   
   @NotNull
   @Valid
   @JsonProperty("productType")
   public ProductTypeReference getProductType();
   
   @NotNull
   @Valid
   @JsonProperty("masterData")
   public ProductCatalogData getMasterData();
   
   @Valid
   @JsonProperty("taxCategory")
   public TaxCategoryReference getTaxCategory();
   
   @Valid
   @JsonProperty("state")
   public StateReference getState();
   
   @Valid
   @JsonProperty("reviewRatingStatistics")
   public ReviewRatingStatistics getReviewRatingStatistics();

   public void setKey(final String key);
   
   public void setProductType(final ProductTypeReference productType);
   
   public void setMasterData(final ProductCatalogData masterData);
   
   public void setTaxCategory(final TaxCategoryReference taxCategory);
   
   public void setState(final StateReference state);
   
   public void setReviewRatingStatistics(final ReviewRatingStatistics reviewRatingStatistics);

   public String toJson();
   
   public static ProductImpl of(){
      return new ProductImpl();
   }
   
   
   public static ProductImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, ProductImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
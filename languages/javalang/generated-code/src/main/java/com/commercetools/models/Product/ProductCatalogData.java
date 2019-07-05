package com.commercetools.models.Product;

import com.commercetools.models.Product.ProductData;
import java.lang.Boolean;
import com.commercetools.models.Product.ProductCatalogDataImpl;

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
@JsonDeserialize(as = ProductCatalogDataImpl.class)
public interface ProductCatalogData  {

   
   @NotNull
   @JsonProperty("published")
   public Boolean getPublished();
   
   @NotNull
   @Valid
   @JsonProperty("current")
   public ProductData getCurrent();
   
   @NotNull
   @Valid
   @JsonProperty("staged")
   public ProductData getStaged();
   
   @NotNull
   @JsonProperty("hasStagedChanges")
   public Boolean getHasStagedChanges();

   public void setPublished(final Boolean published);
   
   public void setCurrent(final ProductData current);
   
   public void setStaged(final ProductData staged);
   
   public void setHasStagedChanges(final Boolean hasStagedChanges);

   public String toJson();
   
   public static ProductCatalogDataImpl of(){
      return new ProductCatalogDataImpl();
   }
   
   
   public static ProductCatalogDataImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, ProductCatalogDataImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
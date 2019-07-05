package com.commercetools.models.Product;

import com.commercetools.models.Category.CategoryReference;
import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Product.CategoryOrderHints;
import com.commercetools.models.Product.ProductVariantDraft;
import com.commercetools.models.Product.SearchKeywords;
import com.commercetools.models.ProductType.ProductTypeReference;
import com.commercetools.models.State.StateReference;
import com.commercetools.models.TaxCategory.TaxCategoryReference;
import java.lang.Boolean;
import java.lang.String;
import com.commercetools.models.Product.ProductDraftImpl;

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
@JsonDeserialize(as = ProductDraftImpl.class)
public interface ProductDraft  {

   
   @NotNull
   @Valid
   @JsonProperty("productType")
   public ProductTypeReference getProductType();
   
   @NotNull
   @Valid
   @JsonProperty("name")
   public LocalizedString getName();
   
   @NotNull
   @Valid
   @JsonProperty("slug")
   public LocalizedString getSlug();
   
   
   @JsonProperty("key")
   public String getKey();
   
   @Valid
   @JsonProperty("description")
   public LocalizedString getDescription();
   
   @Valid
   @JsonProperty("categories")
   public List<CategoryReference> getCategories();
   
   @Valid
   @JsonProperty("categoryOrderHints")
   public CategoryOrderHints getCategoryOrderHints();
   
   @Valid
   @JsonProperty("metaTitle")
   public LocalizedString getMetaTitle();
   
   @Valid
   @JsonProperty("metaDescription")
   public LocalizedString getMetaDescription();
   
   @Valid
   @JsonProperty("metaKeywords")
   public LocalizedString getMetaKeywords();
   
   @Valid
   @JsonProperty("masterVariant")
   public ProductVariantDraft getMasterVariant();
   
   @Valid
   @JsonProperty("variants")
   public List<ProductVariantDraft> getVariants();
   
   @Valid
   @JsonProperty("taxCategory")
   public TaxCategoryReference getTaxCategory();
   
   @Valid
   @JsonProperty("searchKeywords")
   public SearchKeywords getSearchKeywords();
   
   @Valid
   @JsonProperty("state")
   public StateReference getState();
   
   
   @JsonProperty("publish")
   public Boolean getPublish();

   public void setProductType(final ProductTypeReference productType);
   
   public void setName(final LocalizedString name);
   
   public void setSlug(final LocalizedString slug);
   
   public void setKey(final String key);
   
   public void setDescription(final LocalizedString description);
   
   public void setCategories(final List<CategoryReference> categories);
   
   public void setCategoryOrderHints(final CategoryOrderHints categoryOrderHints);
   
   public void setMetaTitle(final LocalizedString metaTitle);
   
   public void setMetaDescription(final LocalizedString metaDescription);
   
   public void setMetaKeywords(final LocalizedString metaKeywords);
   
   public void setMasterVariant(final ProductVariantDraft masterVariant);
   
   public void setVariants(final List<ProductVariantDraft> variants);
   
   public void setTaxCategory(final TaxCategoryReference taxCategory);
   
   public void setSearchKeywords(final SearchKeywords searchKeywords);
   
   public void setState(final StateReference state);
   
   public void setPublish(final Boolean publish);

   public String toJson();
   
   public static ProductDraftImpl of(){
      return new ProductDraftImpl();
   }
   
   
   public static ProductDraftImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, ProductDraftImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
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
public class ProductDraftImpl implements ProductDraft {

   private com.commercetools.models.Product.SearchKeywords searchKeywords;
   
   private com.commercetools.models.Common.LocalizedString description;
   
   private List<ProductVariantDraft> variants;
   
   private com.commercetools.models.Common.LocalizedString metaDescription;
   
   private com.commercetools.models.TaxCategory.TaxCategoryReference taxCategory;
   
   private com.commercetools.models.Common.LocalizedString metaKeywords;
   
   private com.commercetools.models.Product.CategoryOrderHints categoryOrderHints;
   
   private Boolean publish;
   
   private com.commercetools.models.Common.LocalizedString metaTitle;
   
   private com.commercetools.models.Common.LocalizedString name;
   
   private com.commercetools.models.State.StateReference state;
   
   private com.commercetools.models.Product.ProductVariantDraft masterVariant;
   
   private List<CategoryReference> categories;
   
   private String key;
   
   private com.commercetools.models.Common.LocalizedString slug;
   
   private com.commercetools.models.ProductType.ProductTypeReference productType;

   @JsonCreator
   ProductDraftImpl(@JsonProperty("searchKeywords") final com.commercetools.models.Product.SearchKeywords searchKeywords, @JsonProperty("description") final com.commercetools.models.Common.LocalizedString description, @JsonProperty("variants") final List<ProductVariantDraft> variants, @JsonProperty("metaDescription") final com.commercetools.models.Common.LocalizedString metaDescription, @JsonProperty("taxCategory") final com.commercetools.models.TaxCategory.TaxCategoryReference taxCategory, @JsonProperty("metaKeywords") final com.commercetools.models.Common.LocalizedString metaKeywords, @JsonProperty("categoryOrderHints") final com.commercetools.models.Product.CategoryOrderHints categoryOrderHints, @JsonProperty("publish") final Boolean publish, @JsonProperty("metaTitle") final com.commercetools.models.Common.LocalizedString metaTitle, @JsonProperty("name") final com.commercetools.models.Common.LocalizedString name, @JsonProperty("state") final com.commercetools.models.State.StateReference state, @JsonProperty("masterVariant") final com.commercetools.models.Product.ProductVariantDraft masterVariant, @JsonProperty("categories") final List<CategoryReference> categories, @JsonProperty("key") final String key, @JsonProperty("slug") final com.commercetools.models.Common.LocalizedString slug, @JsonProperty("productType") final com.commercetools.models.ProductType.ProductTypeReference productType) {
      this.searchKeywords = searchKeywords;
      this.description = description;
      this.variants = variants;
      this.metaDescription = metaDescription;
      this.taxCategory = taxCategory;
      this.metaKeywords = metaKeywords;
      this.categoryOrderHints = categoryOrderHints;
      this.publish = publish;
      this.metaTitle = metaTitle;
      this.name = name;
      this.state = state;
      this.masterVariant = masterVariant;
      this.categories = categories;
      this.key = key;
      this.slug = slug;
      this.productType = productType;
   }
   public ProductDraftImpl() {
      
   }
   
   
   public com.commercetools.models.Product.SearchKeywords getSearchKeywords(){
      return this.searchKeywords;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getDescription(){
      return this.description;
   }
   
   
   public List<ProductVariantDraft> getVariants(){
      return this.variants;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getMetaDescription(){
      return this.metaDescription;
   }
   
   
   public com.commercetools.models.TaxCategory.TaxCategoryReference getTaxCategory(){
      return this.taxCategory;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getMetaKeywords(){
      return this.metaKeywords;
   }
   
   
   public com.commercetools.models.Product.CategoryOrderHints getCategoryOrderHints(){
      return this.categoryOrderHints;
   }
   
   
   public Boolean getPublish(){
      return this.publish;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getMetaTitle(){
      return this.metaTitle;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getName(){
      return this.name;
   }
   
   
   public com.commercetools.models.State.StateReference getState(){
      return this.state;
   }
   
   
   public com.commercetools.models.Product.ProductVariantDraft getMasterVariant(){
      return this.masterVariant;
   }
   
   
   public List<CategoryReference> getCategories(){
      return this.categories;
   }
   
   
   public String getKey(){
      return this.key;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getSlug(){
      return this.slug;
   }
   
   
   public com.commercetools.models.ProductType.ProductTypeReference getProductType(){
      return this.productType;
   }

   public void setSearchKeywords(final com.commercetools.models.Product.SearchKeywords searchKeywords){
      this.searchKeywords = searchKeywords;
   }
   
   public void setDescription(final com.commercetools.models.Common.LocalizedString description){
      this.description = description;
   }
   
   public void setVariants(final List<ProductVariantDraft> variants){
      this.variants = variants;
   }
   
   public void setMetaDescription(final com.commercetools.models.Common.LocalizedString metaDescription){
      this.metaDescription = metaDescription;
   }
   
   public void setTaxCategory(final com.commercetools.models.TaxCategory.TaxCategoryReference taxCategory){
      this.taxCategory = taxCategory;
   }
   
   public void setMetaKeywords(final com.commercetools.models.Common.LocalizedString metaKeywords){
      this.metaKeywords = metaKeywords;
   }
   
   public void setCategoryOrderHints(final com.commercetools.models.Product.CategoryOrderHints categoryOrderHints){
      this.categoryOrderHints = categoryOrderHints;
   }
   
   public void setPublish(final Boolean publish){
      this.publish = publish;
   }
   
   public void setMetaTitle(final com.commercetools.models.Common.LocalizedString metaTitle){
      this.metaTitle = metaTitle;
   }
   
   public void setName(final com.commercetools.models.Common.LocalizedString name){
      this.name = name;
   }
   
   public void setState(final com.commercetools.models.State.StateReference state){
      this.state = state;
   }
   
   public void setMasterVariant(final com.commercetools.models.Product.ProductVariantDraft masterVariant){
      this.masterVariant = masterVariant;
   }
   
   public void setCategories(final List<CategoryReference> categories){
      this.categories = categories;
   }
   
   public void setKey(final String key){
      this.key = key;
   }
   
   public void setSlug(final com.commercetools.models.Common.LocalizedString slug){
      this.slug = slug;
   }
   
   public void setProductType(final com.commercetools.models.ProductType.ProductTypeReference productType){
      this.productType = productType;
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
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
import com.commercetools.models.Product.ProductDraft;
import javax.annotation.Nullable;
import java.util.List;

public final class ProductDraftBuilder {
   
   @Nullable
   private SearchKeywords searchKeywords;
   
   @Nullable
   private LocalizedString description;
   
   @Nullable
   private List<ProductVariantDraft> variants;
   
   @Nullable
   private LocalizedString metaDescription;
   
   @Nullable
   private TaxCategoryReference taxCategory;
   
   @Nullable
   private LocalizedString metaKeywords;
   
   @Nullable
   private CategoryOrderHints categoryOrderHints;
   
   @Nullable
   private Boolean publish;
   
   @Nullable
   private LocalizedString metaTitle;
   
   
   private LocalizedString name;
   
   @Nullable
   private StateReference state;
   
   @Nullable
   private ProductVariantDraft masterVariant;
   
   @Nullable
   private List<CategoryReference> categories;
   
   @Nullable
   private String key;
   
   
   private LocalizedString slug;
   
   
   private ProductTypeReference productType;
   
   public ProductDraftBuilder searchKeywords(@Nullable final SearchKeywords searchKeywords) {
      this.searchKeywords = searchKeywords;
      return this;
   }
   
   public ProductDraftBuilder description(@Nullable final LocalizedString description) {
      this.description = description;
      return this;
   }
   
   public ProductDraftBuilder variants(@Nullable final List<ProductVariantDraft> variants) {
      this.variants = variants;
      return this;
   }
   
   public ProductDraftBuilder metaDescription(@Nullable final LocalizedString metaDescription) {
      this.metaDescription = metaDescription;
      return this;
   }
   
   public ProductDraftBuilder taxCategory(@Nullable final TaxCategoryReference taxCategory) {
      this.taxCategory = taxCategory;
      return this;
   }
   
   public ProductDraftBuilder metaKeywords(@Nullable final LocalizedString metaKeywords) {
      this.metaKeywords = metaKeywords;
      return this;
   }
   
   public ProductDraftBuilder categoryOrderHints(@Nullable final CategoryOrderHints categoryOrderHints) {
      this.categoryOrderHints = categoryOrderHints;
      return this;
   }
   
   public ProductDraftBuilder publish(@Nullable final Boolean publish) {
      this.publish = publish;
      return this;
   }
   
   public ProductDraftBuilder metaTitle(@Nullable final LocalizedString metaTitle) {
      this.metaTitle = metaTitle;
      return this;
   }
   
   public ProductDraftBuilder name( final LocalizedString name) {
      this.name = name;
      return this;
   }
   
   public ProductDraftBuilder state(@Nullable final StateReference state) {
      this.state = state;
      return this;
   }
   
   public ProductDraftBuilder masterVariant(@Nullable final ProductVariantDraft masterVariant) {
      this.masterVariant = masterVariant;
      return this;
   }
   
   public ProductDraftBuilder categories(@Nullable final List<CategoryReference> categories) {
      this.categories = categories;
      return this;
   }
   
   public ProductDraftBuilder key(@Nullable final String key) {
      this.key = key;
      return this;
   }
   
   public ProductDraftBuilder slug( final LocalizedString slug) {
      this.slug = slug;
      return this;
   }
   
   public ProductDraftBuilder productType( final ProductTypeReference productType) {
      this.productType = productType;
      return this;
   }
   
   @Nullable
   public SearchKeywords getSearchKeywords(){
      return this.searchKeywords;
   }
   
   @Nullable
   public LocalizedString getDescription(){
      return this.description;
   }
   
   @Nullable
   public List<ProductVariantDraft> getVariants(){
      return this.variants;
   }
   
   @Nullable
   public LocalizedString getMetaDescription(){
      return this.metaDescription;
   }
   
   @Nullable
   public TaxCategoryReference getTaxCategory(){
      return this.taxCategory;
   }
   
   @Nullable
   public LocalizedString getMetaKeywords(){
      return this.metaKeywords;
   }
   
   @Nullable
   public CategoryOrderHints getCategoryOrderHints(){
      return this.categoryOrderHints;
   }
   
   @Nullable
   public Boolean getPublish(){
      return this.publish;
   }
   
   @Nullable
   public LocalizedString getMetaTitle(){
      return this.metaTitle;
   }
   
   
   public LocalizedString getName(){
      return this.name;
   }
   
   @Nullable
   public StateReference getState(){
      return this.state;
   }
   
   @Nullable
   public ProductVariantDraft getMasterVariant(){
      return this.masterVariant;
   }
   
   @Nullable
   public List<CategoryReference> getCategories(){
      return this.categories;
   }
   
   @Nullable
   public String getKey(){
      return this.key;
   }
   
   
   public LocalizedString getSlug(){
      return this.slug;
   }
   
   
   public ProductTypeReference getProductType(){
      return this.productType;
   }

   public ProductDraft build() {
       return new ProductDraftImpl(searchKeywords, description, variants, metaDescription, taxCategory, metaKeywords, categoryOrderHints, publish, metaTitle, name, state, masterVariant, categories, key, slug, productType);
   }
   
   
   public static ProductDraftBuilder of() {
      return new ProductDraftBuilder();
   }
   
   
   public static ProductDraftBuilder of(final ProductDraft template) {
      ProductDraftBuilder builder = new ProductDraftBuilder();
      builder.searchKeywords = template.getSearchKeywords();
      builder.description = template.getDescription();
      builder.variants = template.getVariants();
      builder.metaDescription = template.getMetaDescription();
      builder.taxCategory = template.getTaxCategory();
      builder.metaKeywords = template.getMetaKeywords();
      builder.categoryOrderHints = template.getCategoryOrderHints();
      builder.publish = template.getPublish();
      builder.metaTitle = template.getMetaTitle();
      builder.name = template.getName();
      builder.state = template.getState();
      builder.masterVariant = template.getMasterVariant();
      builder.categories = template.getCategories();
      builder.key = template.getKey();
      builder.slug = template.getSlug();
      builder.productType = template.getProductType();
      return builder;
   }
   
}
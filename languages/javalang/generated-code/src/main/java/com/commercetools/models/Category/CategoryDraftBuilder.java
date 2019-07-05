package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryResourceIdentifier;
import com.commercetools.models.Common.AssetDraft;
import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Type.CustomFieldsDraft;
import java.lang.String;
import com.commercetools.models.Category.CategoryDraft;
import javax.annotation.Nullable;
import java.util.List;

public final class CategoryDraftBuilder {
   
   @Nullable
   private CategoryResourceIdentifier parent;
   
   @Nullable
   private List<AssetDraft> assets;
   
   @Nullable
   private LocalizedString metaKeywords;
   
   @Nullable
   private String orderHint;
   
   @Nullable
   private CustomFieldsDraft custom;
   
   @Nullable
   private LocalizedString metaTitle;
   
   
   private LocalizedString name;
   
   @Nullable
   private String externalId;
   
   @Nullable
   private LocalizedString description;
   
   @Nullable
   private LocalizedString metaDescription;
   
   @Nullable
   private String key;
   
   
   private LocalizedString slug;
   
   public CategoryDraftBuilder parent(@Nullable final CategoryResourceIdentifier parent) {
      this.parent = parent;
      return this;
   }
   
   public CategoryDraftBuilder assets(@Nullable final List<AssetDraft> assets) {
      this.assets = assets;
      return this;
   }
   
   public CategoryDraftBuilder metaKeywords(@Nullable final LocalizedString metaKeywords) {
      this.metaKeywords = metaKeywords;
      return this;
   }
   
   public CategoryDraftBuilder orderHint(@Nullable final String orderHint) {
      this.orderHint = orderHint;
      return this;
   }
   
   public CategoryDraftBuilder custom(@Nullable final CustomFieldsDraft custom) {
      this.custom = custom;
      return this;
   }
   
   public CategoryDraftBuilder metaTitle(@Nullable final LocalizedString metaTitle) {
      this.metaTitle = metaTitle;
      return this;
   }
   
   public CategoryDraftBuilder name( final LocalizedString name) {
      this.name = name;
      return this;
   }
   
   public CategoryDraftBuilder externalId(@Nullable final String externalId) {
      this.externalId = externalId;
      return this;
   }
   
   public CategoryDraftBuilder description(@Nullable final LocalizedString description) {
      this.description = description;
      return this;
   }
   
   public CategoryDraftBuilder metaDescription(@Nullable final LocalizedString metaDescription) {
      this.metaDescription = metaDescription;
      return this;
   }
   
   public CategoryDraftBuilder key(@Nullable final String key) {
      this.key = key;
      return this;
   }
   
   public CategoryDraftBuilder slug( final LocalizedString slug) {
      this.slug = slug;
      return this;
   }
   
   @Nullable
   public CategoryResourceIdentifier getParent(){
      return this.parent;
   }
   
   @Nullable
   public List<AssetDraft> getAssets(){
      return this.assets;
   }
   
   @Nullable
   public LocalizedString getMetaKeywords(){
      return this.metaKeywords;
   }
   
   @Nullable
   public String getOrderHint(){
      return this.orderHint;
   }
   
   @Nullable
   public CustomFieldsDraft getCustom(){
      return this.custom;
   }
   
   @Nullable
   public LocalizedString getMetaTitle(){
      return this.metaTitle;
   }
   
   
   public LocalizedString getName(){
      return this.name;
   }
   
   @Nullable
   public String getExternalId(){
      return this.externalId;
   }
   
   @Nullable
   public LocalizedString getDescription(){
      return this.description;
   }
   
   @Nullable
   public LocalizedString getMetaDescription(){
      return this.metaDescription;
   }
   
   @Nullable
   public String getKey(){
      return this.key;
   }
   
   
   public LocalizedString getSlug(){
      return this.slug;
   }

   public CategoryDraft build() {
       return new CategoryDraftImpl(parent, assets, metaKeywords, orderHint, custom, metaTitle, name, externalId, description, metaDescription, key, slug);
   }
   
   
   public static CategoryDraftBuilder of() {
      return new CategoryDraftBuilder();
   }
   
   
   public static CategoryDraftBuilder of(final CategoryDraft template) {
      CategoryDraftBuilder builder = new CategoryDraftBuilder();
      builder.parent = template.getParent();
      builder.assets = template.getAssets();
      builder.metaKeywords = template.getMetaKeywords();
      builder.orderHint = template.getOrderHint();
      builder.custom = template.getCustom();
      builder.metaTitle = template.getMetaTitle();
      builder.name = template.getName();
      builder.externalId = template.getExternalId();
      builder.description = template.getDescription();
      builder.metaDescription = template.getMetaDescription();
      builder.key = template.getKey();
      builder.slug = template.getSlug();
      return builder;
   }
   
}
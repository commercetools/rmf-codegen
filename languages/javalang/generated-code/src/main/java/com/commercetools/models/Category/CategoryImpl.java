package com.commercetools.models.Category;

import com.commercetools.models.Category.CategoryReference;
import com.commercetools.models.Common.Asset;
import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Common.LoggedResource;
import com.commercetools.models.Type.CustomFields;
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
public class CategoryImpl implements Category {

   private ZonedDateTime createdAt;
   
   private ZonedDateTime lastModifiedAt;
   
   private String id;
   
   private Long version;
   
   private com.commercetools.models.Common.CreatedBy createdBy;
   
   private com.commercetools.models.Common.LastModifiedBy lastModifiedBy;
   
   private com.commercetools.models.Category.CategoryReference parent;
   
   private List<Asset> assets;
   
   private com.commercetools.models.Common.LocalizedString metaKeywords;
   
   private String orderHint;
   
   private com.commercetools.models.Type.CustomFields custom;
   
   private com.commercetools.models.Common.LocalizedString metaTitle;
   
   private com.commercetools.models.Common.LocalizedString name;
   
   private String externalId;
   
   private com.commercetools.models.Common.LocalizedString description;
   
   private List<CategoryReference> ancestors;
   
   private com.commercetools.models.Common.LocalizedString metaDescription;
   
   private String key;
   
   private com.commercetools.models.Common.LocalizedString slug;

   @JsonCreator
   CategoryImpl(@JsonProperty("createdAt") final ZonedDateTime createdAt, @JsonProperty("lastModifiedAt") final ZonedDateTime lastModifiedAt, @JsonProperty("id") final String id, @JsonProperty("version") final Long version, @JsonProperty("createdBy") final com.commercetools.models.Common.CreatedBy createdBy, @JsonProperty("lastModifiedBy") final com.commercetools.models.Common.LastModifiedBy lastModifiedBy, @JsonProperty("parent") final com.commercetools.models.Category.CategoryReference parent, @JsonProperty("assets") final List<Asset> assets, @JsonProperty("metaKeywords") final com.commercetools.models.Common.LocalizedString metaKeywords, @JsonProperty("orderHint") final String orderHint, @JsonProperty("custom") final com.commercetools.models.Type.CustomFields custom, @JsonProperty("metaTitle") final com.commercetools.models.Common.LocalizedString metaTitle, @JsonProperty("name") final com.commercetools.models.Common.LocalizedString name, @JsonProperty("externalId") final String externalId, @JsonProperty("description") final com.commercetools.models.Common.LocalizedString description, @JsonProperty("ancestors") final List<CategoryReference> ancestors, @JsonProperty("metaDescription") final com.commercetools.models.Common.LocalizedString metaDescription, @JsonProperty("key") final String key, @JsonProperty("slug") final com.commercetools.models.Common.LocalizedString slug) {
      this.createdAt = createdAt;
      this.lastModifiedAt = lastModifiedAt;
      this.id = id;
      this.version = version;
      this.createdBy = createdBy;
      this.lastModifiedBy = lastModifiedBy;
      this.parent = parent;
      this.assets = assets;
      this.metaKeywords = metaKeywords;
      this.orderHint = orderHint;
      this.custom = custom;
      this.metaTitle = metaTitle;
      this.name = name;
      this.externalId = externalId;
      this.description = description;
      this.ancestors = ancestors;
      this.metaDescription = metaDescription;
      this.key = key;
      this.slug = slug;
   }
   public CategoryImpl() {
      
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
   
   
   public com.commercetools.models.Category.CategoryReference getParent(){
      return this.parent;
   }
   
   
   public List<Asset> getAssets(){
      return this.assets;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getMetaKeywords(){
      return this.metaKeywords;
   }
   
   
   public String getOrderHint(){
      return this.orderHint;
   }
   
   
   public com.commercetools.models.Type.CustomFields getCustom(){
      return this.custom;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getMetaTitle(){
      return this.metaTitle;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getName(){
      return this.name;
   }
   
   
   public String getExternalId(){
      return this.externalId;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getDescription(){
      return this.description;
   }
   
   
   public List<CategoryReference> getAncestors(){
      return this.ancestors;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getMetaDescription(){
      return this.metaDescription;
   }
   
   
   public String getKey(){
      return this.key;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getSlug(){
      return this.slug;
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
   
   public void setParent(final com.commercetools.models.Category.CategoryReference parent){
      this.parent = parent;
   }
   
   public void setAssets(final List<Asset> assets){
      this.assets = assets;
   }
   
   public void setMetaKeywords(final com.commercetools.models.Common.LocalizedString metaKeywords){
      this.metaKeywords = metaKeywords;
   }
   
   public void setOrderHint(final String orderHint){
      this.orderHint = orderHint;
   }
   
   public void setCustom(final com.commercetools.models.Type.CustomFields custom){
      this.custom = custom;
   }
   
   public void setMetaTitle(final com.commercetools.models.Common.LocalizedString metaTitle){
      this.metaTitle = metaTitle;
   }
   
   public void setName(final com.commercetools.models.Common.LocalizedString name){
      this.name = name;
   }
   
   public void setExternalId(final String externalId){
      this.externalId = externalId;
   }
   
   public void setDescription(final com.commercetools.models.Common.LocalizedString description){
      this.description = description;
   }
   
   public void setAncestors(final List<CategoryReference> ancestors){
      this.ancestors = ancestors;
   }
   
   public void setMetaDescription(final com.commercetools.models.Common.LocalizedString metaDescription){
      this.metaDescription = metaDescription;
   }
   
   public void setKey(final String key){
      this.key = key;
   }
   
   public void setSlug(final com.commercetools.models.Common.LocalizedString slug){
      this.slug = slug;
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
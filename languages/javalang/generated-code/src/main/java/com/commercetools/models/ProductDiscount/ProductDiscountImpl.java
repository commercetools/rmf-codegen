package com.commercetools.models.ProductDiscount;

import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Common.LoggedResource;
import com.commercetools.models.Common.Reference;
import com.commercetools.models.ProductDiscount.ProductDiscountValue;
import java.lang.Boolean;
import java.lang.String;
import java.time.ZonedDateTime;
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
public class ProductDiscountImpl implements ProductDiscount {

   private ZonedDateTime createdAt;
   
   private ZonedDateTime lastModifiedAt;
   
   private String id;
   
   private Long version;
   
   private com.commercetools.models.Common.CreatedBy createdBy;
   
   private com.commercetools.models.Common.LastModifiedBy lastModifiedBy;
   
   private String predicate;
   
   private List<Reference> references;
   
   private String sortOrder;
   
   private com.commercetools.models.Common.LocalizedString name;
   
   private ZonedDateTime validUntil;
   
   private com.commercetools.models.Common.LocalizedString description;
   
   private ZonedDateTime validFrom;
   
   private Boolean isActive;
   
   private com.commercetools.models.ProductDiscount.ProductDiscountValue value;
   
   private String key;

   @JsonCreator
   ProductDiscountImpl(@JsonProperty("createdAt") final ZonedDateTime createdAt, @JsonProperty("lastModifiedAt") final ZonedDateTime lastModifiedAt, @JsonProperty("id") final String id, @JsonProperty("version") final Long version, @JsonProperty("createdBy") final com.commercetools.models.Common.CreatedBy createdBy, @JsonProperty("lastModifiedBy") final com.commercetools.models.Common.LastModifiedBy lastModifiedBy, @JsonProperty("predicate") final String predicate, @JsonProperty("references") final List<Reference> references, @JsonProperty("sortOrder") final String sortOrder, @JsonProperty("name") final com.commercetools.models.Common.LocalizedString name, @JsonProperty("validUntil") final ZonedDateTime validUntil, @JsonProperty("description") final com.commercetools.models.Common.LocalizedString description, @JsonProperty("validFrom") final ZonedDateTime validFrom, @JsonProperty("isActive") final Boolean isActive, @JsonProperty("value") final com.commercetools.models.ProductDiscount.ProductDiscountValue value, @JsonProperty("key") final String key) {
      this.createdAt = createdAt;
      this.lastModifiedAt = lastModifiedAt;
      this.id = id;
      this.version = version;
      this.createdBy = createdBy;
      this.lastModifiedBy = lastModifiedBy;
      this.predicate = predicate;
      this.references = references;
      this.sortOrder = sortOrder;
      this.name = name;
      this.validUntil = validUntil;
      this.description = description;
      this.validFrom = validFrom;
      this.isActive = isActive;
      this.value = value;
      this.key = key;
   }
   public ProductDiscountImpl() {
      
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
   
   
   public String getPredicate(){
      return this.predicate;
   }
   
   
   public List<Reference> getReferences(){
      return this.references;
   }
   
   
   public String getSortOrder(){
      return this.sortOrder;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getName(){
      return this.name;
   }
   
   
   public ZonedDateTime getValidUntil(){
      return this.validUntil;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getDescription(){
      return this.description;
   }
   
   
   public ZonedDateTime getValidFrom(){
      return this.validFrom;
   }
   
   
   public Boolean getIsActive(){
      return this.isActive;
   }
   
   
   public com.commercetools.models.ProductDiscount.ProductDiscountValue getValue(){
      return this.value;
   }
   
   
   public String getKey(){
      return this.key;
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
   
   public void setPredicate(final String predicate){
      this.predicate = predicate;
   }
   
   public void setReferences(final List<Reference> references){
      this.references = references;
   }
   
   public void setSortOrder(final String sortOrder){
      this.sortOrder = sortOrder;
   }
   
   public void setName(final com.commercetools.models.Common.LocalizedString name){
      this.name = name;
   }
   
   public void setValidUntil(final ZonedDateTime validUntil){
      this.validUntil = validUntil;
   }
   
   public void setDescription(final com.commercetools.models.Common.LocalizedString description){
      this.description = description;
   }
   
   public void setValidFrom(final ZonedDateTime validFrom){
      this.validFrom = validFrom;
   }
   
   public void setIsActive(final Boolean isActive){
      this.isActive = isActive;
   }
   
   public void setValue(final com.commercetools.models.ProductDiscount.ProductDiscountValue value){
      this.value = value;
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
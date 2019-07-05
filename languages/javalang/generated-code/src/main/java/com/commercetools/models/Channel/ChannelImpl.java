package com.commercetools.models.Channel;

import com.commercetools.models.Channel.ChannelRoleEnum;
import com.commercetools.models.Common.Address;
import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Common.LoggedResource;
import com.commercetools.models.Review.ReviewRatingStatistics;
import com.commercetools.models.Type.CustomFields;
import java.lang.Object;
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
public class ChannelImpl implements Channel {

   private ZonedDateTime createdAt;
   
   private ZonedDateTime lastModifiedAt;
   
   private String id;
   
   private Long version;
   
   private com.commercetools.models.Common.CreatedBy createdBy;
   
   private com.commercetools.models.Common.LastModifiedBy lastModifiedBy;
   
   private com.commercetools.models.Common.Address address;
   
   private Object geoLocation;
   
   private com.commercetools.models.Type.CustomFields custom;
   
   private List<ChannelRoleEnum> roles;
   
   private com.commercetools.models.Common.LocalizedString name;
   
   private com.commercetools.models.Common.LocalizedString description;
   
   private com.commercetools.models.Review.ReviewRatingStatistics reviewRatingStatistics;
   
   private String key;

   @JsonCreator
   ChannelImpl(@JsonProperty("createdAt") final ZonedDateTime createdAt, @JsonProperty("lastModifiedAt") final ZonedDateTime lastModifiedAt, @JsonProperty("id") final String id, @JsonProperty("version") final Long version, @JsonProperty("createdBy") final com.commercetools.models.Common.CreatedBy createdBy, @JsonProperty("lastModifiedBy") final com.commercetools.models.Common.LastModifiedBy lastModifiedBy, @JsonProperty("address") final com.commercetools.models.Common.Address address, @JsonProperty("geoLocation") final Object geoLocation, @JsonProperty("custom") final com.commercetools.models.Type.CustomFields custom, @JsonProperty("roles") final List<ChannelRoleEnum> roles, @JsonProperty("name") final com.commercetools.models.Common.LocalizedString name, @JsonProperty("description") final com.commercetools.models.Common.LocalizedString description, @JsonProperty("reviewRatingStatistics") final com.commercetools.models.Review.ReviewRatingStatistics reviewRatingStatistics, @JsonProperty("key") final String key) {
      this.createdAt = createdAt;
      this.lastModifiedAt = lastModifiedAt;
      this.id = id;
      this.version = version;
      this.createdBy = createdBy;
      this.lastModifiedBy = lastModifiedBy;
      this.address = address;
      this.geoLocation = geoLocation;
      this.custom = custom;
      this.roles = roles;
      this.name = name;
      this.description = description;
      this.reviewRatingStatistics = reviewRatingStatistics;
      this.key = key;
   }
   public ChannelImpl() {
      
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
   
   
   public com.commercetools.models.Common.Address getAddress(){
      return this.address;
   }
   
   
   public Object getGeoLocation(){
      return this.geoLocation;
   }
   
   
   public com.commercetools.models.Type.CustomFields getCustom(){
      return this.custom;
   }
   
   
   public List<ChannelRoleEnum> getRoles(){
      return this.roles;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getName(){
      return this.name;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getDescription(){
      return this.description;
   }
   
   
   public com.commercetools.models.Review.ReviewRatingStatistics getReviewRatingStatistics(){
      return this.reviewRatingStatistics;
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
   
   public void setAddress(final com.commercetools.models.Common.Address address){
      this.address = address;
   }
   
   public void setGeoLocation(final Object geoLocation){
      this.geoLocation = geoLocation;
   }
   
   public void setCustom(final com.commercetools.models.Type.CustomFields custom){
      this.custom = custom;
   }
   
   public void setRoles(final List<ChannelRoleEnum> roles){
      this.roles = roles;
   }
   
   public void setName(final com.commercetools.models.Common.LocalizedString name){
      this.name = name;
   }
   
   public void setDescription(final com.commercetools.models.Common.LocalizedString description){
      this.description = description;
   }
   
   public void setReviewRatingStatistics(final com.commercetools.models.Review.ReviewRatingStatistics reviewRatingStatistics){
      this.reviewRatingStatistics = reviewRatingStatistics;
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
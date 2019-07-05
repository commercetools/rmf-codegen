package com.commercetools.models.Project;

import com.commercetools.models.Message.MessageConfiguration;
import com.commercetools.models.Project.ExternalOAuth;
import com.commercetools.models.Project.ShippingRateInputType;
import java.lang.Long;
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
public class ProjectImpl implements Project {

   private com.commercetools.models.Project.ExternalOAuth externalOAuth;
   
   private com.commercetools.models.Project.ShippingRateInputType shippingRateInputType;
   
   private ZonedDateTime createdAt;
   
   private List<String> languages;
   
   private String name;
   
   private com.commercetools.models.Message.MessageConfiguration messages;
   
   private List<String> countries;
   
   private String trialUntil;
   
   private Long version;
   
   private String key;
   
   private List<String> currencies;

   @JsonCreator
   ProjectImpl(@JsonProperty("externalOAuth") final com.commercetools.models.Project.ExternalOAuth externalOAuth, @JsonProperty("shippingRateInputType") final com.commercetools.models.Project.ShippingRateInputType shippingRateInputType, @JsonProperty("createdAt") final ZonedDateTime createdAt, @JsonProperty("languages") final List<String> languages, @JsonProperty("name") final String name, @JsonProperty("messages") final com.commercetools.models.Message.MessageConfiguration messages, @JsonProperty("countries") final List<String> countries, @JsonProperty("trialUntil") final String trialUntil, @JsonProperty("version") final Long version, @JsonProperty("key") final String key, @JsonProperty("currencies") final List<String> currencies) {
      this.externalOAuth = externalOAuth;
      this.shippingRateInputType = shippingRateInputType;
      this.createdAt = createdAt;
      this.languages = languages;
      this.name = name;
      this.messages = messages;
      this.countries = countries;
      this.trialUntil = trialUntil;
      this.version = version;
      this.key = key;
      this.currencies = currencies;
   }
   public ProjectImpl() {
      
   }
   
   
   public com.commercetools.models.Project.ExternalOAuth getExternalOAuth(){
      return this.externalOAuth;
   }
   
   
   public com.commercetools.models.Project.ShippingRateInputType getShippingRateInputType(){
      return this.shippingRateInputType;
   }
   
   
   public ZonedDateTime getCreatedAt(){
      return this.createdAt;
   }
   
   
   public List<String> getLanguages(){
      return this.languages;
   }
   
   
   public String getName(){
      return this.name;
   }
   
   
   public com.commercetools.models.Message.MessageConfiguration getMessages(){
      return this.messages;
   }
   
   
   public List<String> getCountries(){
      return this.countries;
   }
   
   
   public String getTrialUntil(){
      return this.trialUntil;
   }
   
   
   public Long getVersion(){
      return this.version;
   }
   
   
   public String getKey(){
      return this.key;
   }
   
   
   public List<String> getCurrencies(){
      return this.currencies;
   }

   public void setExternalOAuth(final com.commercetools.models.Project.ExternalOAuth externalOAuth){
      this.externalOAuth = externalOAuth;
   }
   
   public void setShippingRateInputType(final com.commercetools.models.Project.ShippingRateInputType shippingRateInputType){
      this.shippingRateInputType = shippingRateInputType;
   }
   
   public void setCreatedAt(final ZonedDateTime createdAt){
      this.createdAt = createdAt;
   }
   
   public void setLanguages(final List<String> languages){
      this.languages = languages;
   }
   
   public void setName(final String name){
      this.name = name;
   }
   
   public void setMessages(final com.commercetools.models.Message.MessageConfiguration messages){
      this.messages = messages;
   }
   
   public void setCountries(final List<String> countries){
      this.countries = countries;
   }
   
   public void setTrialUntil(final String trialUntil){
      this.trialUntil = trialUntil;
   }
   
   public void setVersion(final Long version){
      this.version = version;
   }
   
   public void setKey(final String key){
      this.key = key;
   }
   
   public void setCurrencies(final List<String> currencies){
      this.currencies = currencies;
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
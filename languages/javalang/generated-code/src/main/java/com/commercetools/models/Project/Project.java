package com.commercetools.models.Project;

import com.commercetools.models.Message.MessageConfiguration;
import com.commercetools.models.Project.ExternalOAuth;
import com.commercetools.models.Project.ShippingRateInputType;
import java.lang.Long;
import java.lang.String;
import java.time.ZonedDateTime;
import com.commercetools.models.Project.ProjectImpl;

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
@JsonDeserialize(as = ProjectImpl.class)
public interface Project  {

   
   @NotNull
   @JsonProperty("version")
   public Long getVersion();
   
   @NotNull
   @JsonProperty("key")
   public String getKey();
   
   @NotNull
   @JsonProperty("name")
   public String getName();
   
   @NotNull
   @JsonProperty("countries")
   public List<String> getCountries();
   
   @NotNull
   @JsonProperty("currencies")
   public List<String> getCurrencies();
   
   @NotNull
   @JsonProperty("languages")
   public List<String> getLanguages();
   
   @NotNull
   @JsonProperty("createdAt")
   public ZonedDateTime getCreatedAt();
   
   
   @JsonProperty("trialUntil")
   public String getTrialUntil();
   
   @NotNull
   @Valid
   @JsonProperty("messages")
   public MessageConfiguration getMessages();
   
   @Valid
   @JsonProperty("shippingRateInputType")
   public ShippingRateInputType getShippingRateInputType();
   
   @Valid
   @JsonProperty("externalOAuth")
   public ExternalOAuth getExternalOAuth();

   public void setVersion(final Long version);
   
   public void setKey(final String key);
   
   public void setName(final String name);
   
   public void setCountries(final List<String> countries);
   
   public void setCurrencies(final List<String> currencies);
   
   public void setLanguages(final List<String> languages);
   
   public void setCreatedAt(final ZonedDateTime createdAt);
   
   public void setTrialUntil(final String trialUntil);
   
   public void setMessages(final MessageConfiguration messages);
   
   public void setShippingRateInputType(final ShippingRateInputType shippingRateInputType);
   
   public void setExternalOAuth(final ExternalOAuth externalOAuth);

   public String toJson();
   
   public static ProjectImpl of(){
      return new ProjectImpl();
   }
   
   
   public static ProjectImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, ProjectImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
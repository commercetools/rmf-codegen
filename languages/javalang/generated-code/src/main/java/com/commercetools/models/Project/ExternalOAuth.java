package com.commercetools.models.Project;

import java.lang.String;
import com.commercetools.models.Project.ExternalOAuthImpl;

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
@JsonDeserialize(as = ExternalOAuthImpl.class)
public interface ExternalOAuth  {

   
   @NotNull
   @JsonProperty("url")
   public String getUrl();
   
   @NotNull
   @JsonProperty("authorizationHeader")
   public String getAuthorizationHeader();

   public void setUrl(final String url);
   
   public void setAuthorizationHeader(final String authorizationHeader);

   public String toJson();
   
   public static ExternalOAuthImpl of(){
      return new ExternalOAuthImpl();
   }
   
   
   public static ExternalOAuthImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, ExternalOAuthImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
package com.commercetools.models.Project;

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
public class ExternalOAuthImpl implements ExternalOAuth {

   private String authorizationHeader;
   
   private String url;

   @JsonCreator
   ExternalOAuthImpl(@JsonProperty("authorizationHeader") final String authorizationHeader, @JsonProperty("url") final String url) {
      this.authorizationHeader = authorizationHeader;
      this.url = url;
   }
   public ExternalOAuthImpl() {
      
   }
   
   
   public String getAuthorizationHeader(){
      return this.authorizationHeader;
   }
   
   
   public String getUrl(){
      return this.url;
   }

   public void setAuthorizationHeader(final String authorizationHeader){
      this.authorizationHeader = authorizationHeader;
   }
   
   public void setUrl(final String url){
      this.url = url;
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
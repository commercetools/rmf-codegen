package com.commercetools.models.Common;

import com.commercetools.models.Common.AssetDimensions;
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
public class AssetSourceImpl implements AssetSource {

   private String contentType;
   
   private String uri;
   
   private String key;
   
   private com.commercetools.models.Common.AssetDimensions dimensions;

   @JsonCreator
   AssetSourceImpl(@JsonProperty("contentType") final String contentType, @JsonProperty("uri") final String uri, @JsonProperty("key") final String key, @JsonProperty("dimensions") final com.commercetools.models.Common.AssetDimensions dimensions) {
      this.contentType = contentType;
      this.uri = uri;
      this.key = key;
      this.dimensions = dimensions;
   }
   public AssetSourceImpl() {
      
   }
   
   
   public String getContentType(){
      return this.contentType;
   }
   
   
   public String getUri(){
      return this.uri;
   }
   
   
   public String getKey(){
      return this.key;
   }
   
   
   public com.commercetools.models.Common.AssetDimensions getDimensions(){
      return this.dimensions;
   }

   public void setContentType(final String contentType){
      this.contentType = contentType;
   }
   
   public void setUri(final String uri){
      this.uri = uri;
   }
   
   public void setKey(final String key){
      this.key = key;
   }
   
   public void setDimensions(final com.commercetools.models.Common.AssetDimensions dimensions){
      this.dimensions = dimensions;
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
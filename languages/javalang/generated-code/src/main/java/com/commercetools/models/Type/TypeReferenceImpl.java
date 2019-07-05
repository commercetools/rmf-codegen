package com.commercetools.models.Type;

import com.commercetools.models.Common.Reference;
import com.commercetools.models.Common.ReferenceTypeId;
import com.commercetools.models.Type.Type;
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
public class TypeReferenceImpl implements TypeReference {

   private String id;
   
   private String key;
   
   private com.commercetools.models.Type.Type obj;

   @JsonCreator
   TypeReferenceImpl(@JsonProperty("id") final String id, @JsonProperty("key") final String key, @JsonProperty("obj") final com.commercetools.models.Type.Type obj) {
      this.id = id;
      this.key = key;
      this.obj = obj;
   }
   public TypeReferenceImpl() {
      
   }
   
   
   public String getId(){
      return this.id;
   }
   
   
   public String getKey(){
      return this.key;
   }
   
   
   public com.commercetools.models.Type.Type getObj(){
      return this.obj;
   }

   public void setId(final String id){
      this.id = id;
   }
   
   public void setKey(final String key){
      this.key = key;
   }
   
   public void setObj(final com.commercetools.models.Type.Type obj){
      this.obj = obj;
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
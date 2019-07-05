package com.commercetools.models.Error;

import com.commercetools.models.Error.ErrorObject;
import java.lang.Integer;
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
public class ErrorResponseImpl implements ErrorResponse {

   private String errorDescription;
   
   private String error;
   
   private String message;
   
   private List<ErrorObject> errors;
   
   private Integer statusCode;

   @JsonCreator
   ErrorResponseImpl(@JsonProperty("errorDescription") final String errorDescription, @JsonProperty("error") final String error, @JsonProperty("message") final String message, @JsonProperty("errors") final List<ErrorObject> errors, @JsonProperty("statusCode") final Integer statusCode) {
      this.errorDescription = errorDescription;
      this.error = error;
      this.message = message;
      this.errors = errors;
      this.statusCode = statusCode;
   }
   public ErrorResponseImpl() {
      
   }
   
   
   public String getErrorDescription(){
      return this.errorDescription;
   }
   
   
   public String getError(){
      return this.error;
   }
   
   
   public String getMessage(){
      return this.message;
   }
   
   
   public List<ErrorObject> getErrors(){
      return this.errors;
   }
   
   
   public Integer getStatusCode(){
      return this.statusCode;
   }

   public void setErrorDescription(final String errorDescription){
      this.errorDescription = errorDescription;
   }
   
   public void setError(final String error){
      this.error = error;
   }
   
   public void setMessage(final String message){
      this.message = message;
   }
   
   public void setErrors(final List<ErrorObject> errors){
      this.errors = errors;
   }
   
   public void setStatusCode(final Integer statusCode){
      this.statusCode = statusCode;
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
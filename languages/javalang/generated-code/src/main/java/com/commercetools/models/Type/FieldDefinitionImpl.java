package com.commercetools.models.Type;

import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Type.TypeTextInputHint;
import java.lang.Boolean;
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
public class FieldDefinitionImpl implements FieldDefinition {

   private String name;
   
   private TypeTextInputHint inputHint;
   
   private com.commercetools.models.Common.LocalizedString label;
   
   private java.lang.Object type;
   
   private Boolean required;

   @JsonCreator
   FieldDefinitionImpl(@JsonProperty("name") final String name, @JsonProperty("inputHint") final TypeTextInputHint inputHint, @JsonProperty("label") final com.commercetools.models.Common.LocalizedString label, @JsonProperty("type") final java.lang.Object type, @JsonProperty("required") final Boolean required) {
      this.name = name;
      this.inputHint = inputHint;
      this.label = label;
      this.type = type;
      this.required = required;
   }
   public FieldDefinitionImpl() {
      
   }
   
   
   public String getName(){
      return this.name;
   }
   
   
   public TypeTextInputHint getInputHint(){
      return this.inputHint;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getLabel(){
      return this.label;
   }
   
   
   public java.lang.Object getType(){
      return this.type;
   }
   
   
   public Boolean getRequired(){
      return this.required;
   }

   public void setName(final String name){
      this.name = name;
   }
   
   public void setInputHint(final TypeTextInputHint inputHint){
      this.inputHint = inputHint;
   }
   
   public void setLabel(final com.commercetools.models.Common.LocalizedString label){
      this.label = label;
   }
   
   public void setType(final java.lang.Object type){
      this.type = type;
   }
   
   public void setRequired(final Boolean required){
      this.required = required;
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
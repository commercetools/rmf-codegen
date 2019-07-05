package com.commercetools.models.ProductType;

import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.ProductType.AttributeConstraintEnum;
import com.commercetools.models.ProductType.TextInputHint;
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
public class AttributeDefinitionDraftImpl implements AttributeDefinitionDraft {

   private Boolean isRequired;
   
   private AttributeConstraintEnum attributeConstraint;
   
   private String name;
   
   private TextInputHint inputHint;
   
   private Boolean isSearchable;
   
   private com.commercetools.models.Common.LocalizedString label;
   
   private Object type;
   
   private com.commercetools.models.Common.LocalizedString inputTip;

   @JsonCreator
   AttributeDefinitionDraftImpl(@JsonProperty("isRequired") final Boolean isRequired, @JsonProperty("attributeConstraint") final AttributeConstraintEnum attributeConstraint, @JsonProperty("name") final String name, @JsonProperty("inputHint") final TextInputHint inputHint, @JsonProperty("isSearchable") final Boolean isSearchable, @JsonProperty("label") final com.commercetools.models.Common.LocalizedString label, @JsonProperty("type") final Object type, @JsonProperty("inputTip") final com.commercetools.models.Common.LocalizedString inputTip) {
      this.isRequired = isRequired;
      this.attributeConstraint = attributeConstraint;
      this.name = name;
      this.inputHint = inputHint;
      this.isSearchable = isSearchable;
      this.label = label;
      this.type = type;
      this.inputTip = inputTip;
   }
   public AttributeDefinitionDraftImpl() {
      
   }
   
   
   public Boolean getIsRequired(){
      return this.isRequired;
   }
   
   
   public AttributeConstraintEnum getAttributeConstraint(){
      return this.attributeConstraint;
   }
   
   
   public String getName(){
      return this.name;
   }
   
   
   public TextInputHint getInputHint(){
      return this.inputHint;
   }
   
   
   public Boolean getIsSearchable(){
      return this.isSearchable;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getLabel(){
      return this.label;
   }
   
   
   public Object getType(){
      return this.type;
   }
   
   
   public com.commercetools.models.Common.LocalizedString getInputTip(){
      return this.inputTip;
   }

   public void setIsRequired(final Boolean isRequired){
      this.isRequired = isRequired;
   }
   
   public void setAttributeConstraint(final AttributeConstraintEnum attributeConstraint){
      this.attributeConstraint = attributeConstraint;
   }
   
   public void setName(final String name){
      this.name = name;
   }
   
   public void setInputHint(final TextInputHint inputHint){
      this.inputHint = inputHint;
   }
   
   public void setIsSearchable(final Boolean isSearchable){
      this.isSearchable = isSearchable;
   }
   
   public void setLabel(final com.commercetools.models.Common.LocalizedString label){
      this.label = label;
   }
   
   public void setType(final Object type){
      this.type = type;
   }
   
   public void setInputTip(final com.commercetools.models.Common.LocalizedString inputTip){
      this.inputTip = inputTip;
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
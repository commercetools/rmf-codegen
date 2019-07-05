package com.commercetools.models.ProductType;

import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.ProductType.AttributeConstraintEnum;
import com.commercetools.models.ProductType.TextInputHint;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.String;
import com.commercetools.models.ProductType.AttributeDefinitionDraftImpl;

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
@JsonDeserialize(as = AttributeDefinitionDraftImpl.class)
public interface AttributeDefinitionDraft  {

   
   @NotNull
   @JsonProperty("type")
   public Object getType();
   
   @NotNull
   @JsonProperty("name")
   public String getName();
   
   @NotNull
   @Valid
   @JsonProperty("label")
   public LocalizedString getLabel();
   
   @NotNull
   @JsonProperty("isRequired")
   public Boolean getIsRequired();
   
   
   @JsonProperty("attributeConstraint")
   public AttributeConstraintEnum getAttributeConstraint();
   
   @Valid
   @JsonProperty("inputTip")
   public LocalizedString getInputTip();
   
   
   @JsonProperty("inputHint")
   public TextInputHint getInputHint();
   
   
   @JsonProperty("isSearchable")
   public Boolean getIsSearchable();

   public void setType(final Object type);
   
   public void setName(final String name);
   
   public void setLabel(final LocalizedString label);
   
   public void setIsRequired(final Boolean isRequired);
   
   public void setAttributeConstraint(final AttributeConstraintEnum attributeConstraint);
   
   public void setInputTip(final LocalizedString inputTip);
   
   public void setInputHint(final TextInputHint inputHint);
   
   public void setIsSearchable(final Boolean isSearchable);

   public String toJson();
   
   public static AttributeDefinitionDraftImpl of(){
      return new AttributeDefinitionDraftImpl();
   }
   
   
   public static AttributeDefinitionDraftImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, AttributeDefinitionDraftImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
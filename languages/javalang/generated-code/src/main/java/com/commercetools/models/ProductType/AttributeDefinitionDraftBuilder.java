package com.commercetools.models.ProductType;

import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.ProductType.AttributeConstraintEnum;
import com.commercetools.models.ProductType.TextInputHint;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.String;
import com.commercetools.models.ProductType.AttributeDefinitionDraft;
import javax.annotation.Nullable;
import java.util.List;

public final class AttributeDefinitionDraftBuilder {
   
   
   private Boolean isRequired;
   
   @Nullable
   private AttributeConstraintEnum attributeConstraint;
   
   
   private String name;
   
   @Nullable
   private TextInputHint inputHint;
   
   @Nullable
   private Boolean isSearchable;
   
   
   private LocalizedString label;
   
   
   private Object type;
   
   @Nullable
   private LocalizedString inputTip;
   
   public AttributeDefinitionDraftBuilder isRequired( final Boolean isRequired) {
      this.isRequired = isRequired;
      return this;
   }
   
   public AttributeDefinitionDraftBuilder attributeConstraint(@Nullable final AttributeConstraintEnum attributeConstraint) {
      this.attributeConstraint = attributeConstraint;
      return this;
   }
   
   public AttributeDefinitionDraftBuilder name( final String name) {
      this.name = name;
      return this;
   }
   
   public AttributeDefinitionDraftBuilder inputHint(@Nullable final TextInputHint inputHint) {
      this.inputHint = inputHint;
      return this;
   }
   
   public AttributeDefinitionDraftBuilder isSearchable(@Nullable final Boolean isSearchable) {
      this.isSearchable = isSearchable;
      return this;
   }
   
   public AttributeDefinitionDraftBuilder label( final LocalizedString label) {
      this.label = label;
      return this;
   }
   
   public AttributeDefinitionDraftBuilder type( final Object type) {
      this.type = type;
      return this;
   }
   
   public AttributeDefinitionDraftBuilder inputTip(@Nullable final LocalizedString inputTip) {
      this.inputTip = inputTip;
      return this;
   }
   
   
   public Boolean getIsRequired(){
      return this.isRequired;
   }
   
   @Nullable
   public AttributeConstraintEnum getAttributeConstraint(){
      return this.attributeConstraint;
   }
   
   
   public String getName(){
      return this.name;
   }
   
   @Nullable
   public TextInputHint getInputHint(){
      return this.inputHint;
   }
   
   @Nullable
   public Boolean getIsSearchable(){
      return this.isSearchable;
   }
   
   
   public LocalizedString getLabel(){
      return this.label;
   }
   
   
   public Object getType(){
      return this.type;
   }
   
   @Nullable
   public LocalizedString getInputTip(){
      return this.inputTip;
   }

   public AttributeDefinitionDraft build() {
       return new AttributeDefinitionDraftImpl(isRequired, attributeConstraint, name, inputHint, isSearchable, label, type, inputTip);
   }
   
   
   public static AttributeDefinitionDraftBuilder of() {
      return new AttributeDefinitionDraftBuilder();
   }
   
   
   public static AttributeDefinitionDraftBuilder of(final AttributeDefinitionDraft template) {
      AttributeDefinitionDraftBuilder builder = new AttributeDefinitionDraftBuilder();
      builder.isRequired = template.getIsRequired();
      builder.attributeConstraint = template.getAttributeConstraint();
      builder.name = template.getName();
      builder.inputHint = template.getInputHint();
      builder.isSearchable = template.getIsSearchable();
      builder.label = template.getLabel();
      builder.type = template.getType();
      builder.inputTip = template.getInputTip();
      return builder;
   }
   
}
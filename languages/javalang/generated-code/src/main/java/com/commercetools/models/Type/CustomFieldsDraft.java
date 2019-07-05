package com.commercetools.models.Type;

import com.commercetools.models.Type.FieldContainer;
import java.lang.Object;
import com.commercetools.models.Type.CustomFieldsDraftImpl;

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
@JsonDeserialize(as = CustomFieldsDraftImpl.class)
public interface CustomFieldsDraft  {

   
   @NotNull
   @JsonProperty("type")
   public Object getType();
   
   @Valid
   @JsonProperty("fields")
   public FieldContainer getFields();

   public void setType(final Object type);
   
   public void setFields(final FieldContainer fields);

   public String toJson();
   
   public static CustomFieldsDraftImpl of(){
      return new CustomFieldsDraftImpl();
   }
   
   
   public static CustomFieldsDraftImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, CustomFieldsDraftImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
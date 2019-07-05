package com.commercetools.models.CustomerGroup;

import com.commercetools.models.Common.LoggedResource;
import com.commercetools.models.Type.CustomFields;
import java.lang.String;
import com.commercetools.models.CustomerGroup.CustomerGroupImpl;

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
@JsonDeserialize(as = CustomerGroupImpl.class)
public interface CustomerGroup extends LoggedResource {

   
   
   @JsonProperty("key")
   public String getKey();
   
   @NotNull
   @JsonProperty("name")
   public String getName();
   
   @Valid
   @JsonProperty("custom")
   public CustomFields getCustom();

   public void setKey(final String key);
   
   public void setName(final String name);
   
   public void setCustom(final CustomFields custom);

   public String toJson();
   
   public static CustomerGroupImpl of(){
      return new CustomerGroupImpl();
   }
   
   
   public static CustomerGroupImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, CustomerGroupImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
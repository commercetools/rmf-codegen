package com.commercetools.models.Common;

import com.commercetools.models.Common.UpdateAction;
import java.lang.Long;
import com.commercetools.models.Common.UpdateImpl;

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
@JsonDeserialize(as = UpdateImpl.class)
public interface Update  {

   
   @NotNull
   @JsonProperty("version")
   public Long getVersion();
   
   @NotNull
   @Valid
   @JsonProperty("actions")
   public List<UpdateAction> getActions();

   public void setVersion(final Long version);
   
   public void setActions(final List<UpdateAction> actions);

   public String toJson();
   
   public static UpdateImpl of(){
      return new UpdateImpl();
   }
   
   
   public static UpdateImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, UpdateImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
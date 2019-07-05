package com.commercetools.models.Common;

import com.commercetools.models.Category.CategoryResourceIdentifier;
import com.commercetools.models.Common.ReferenceTypeId;
import java.lang.String;


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

@JsonSubTypes({
   @JsonSubTypes.Type(value = com.commercetools.models.Category.CategoryResourceIdentifierImpl.class, name = "category")
})
@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   include = JsonTypeInfo.As.PROPERTY,
   property = "typeId"
)
@Generated(
    value = "io.vrap.rmf.codegen.rendring.CoreCodeGenerator",
    comments = "https://github.com/vrapio/rmf-codegen"
)
public interface ResourceIdentifier  {

   
   
   @JsonProperty("id")
   public String getId();
   
   
   @JsonProperty("key")
   public String getKey();

   public void setId(final String id);
   
   public void setKey(final String key);

   public String toJson();
   
   

}
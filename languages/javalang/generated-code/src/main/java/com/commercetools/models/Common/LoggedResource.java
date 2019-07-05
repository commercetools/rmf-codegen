package com.commercetools.models.Common;

import com.commercetools.models.Category.Category;
import com.commercetools.models.Channel.Channel;
import com.commercetools.models.Common.BaseResource;
import com.commercetools.models.Common.CreatedBy;
import com.commercetools.models.Common.LastModifiedBy;
import com.commercetools.models.Customer.Customer;
import com.commercetools.models.CustomerGroup.CustomerGroup;
import com.commercetools.models.Product.Product;
import com.commercetools.models.ProductDiscount.ProductDiscount;
import com.commercetools.models.ProductType.ProductType;
import com.commercetools.models.State.State;
import com.commercetools.models.TaxCategory.TaxCategory;
import com.commercetools.models.Type.Type;
import com.commercetools.models.Common.LoggedResourceImpl;

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
@JsonDeserialize(as = LoggedResourceImpl.class)
public interface LoggedResource extends BaseResource {

   
   @Valid
   @JsonProperty("lastModifiedBy")
   public LastModifiedBy getLastModifiedBy();
   
   @Valid
   @JsonProperty("createdBy")
   public CreatedBy getCreatedBy();

   public void setLastModifiedBy(final LastModifiedBy lastModifiedBy);
   
   public void setCreatedBy(final CreatedBy createdBy);

   public String toJson();
   
   public static LoggedResourceImpl of(){
      return new LoggedResourceImpl();
   }
   
   
   public static LoggedResourceImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, LoggedResourceImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
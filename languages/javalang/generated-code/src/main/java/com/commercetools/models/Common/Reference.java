package com.commercetools.models.Common;

import com.commercetools.models.Category.CategoryReference;
import com.commercetools.models.Channel.ChannelReference;
import com.commercetools.models.Common.ReferenceTypeId;
import com.commercetools.models.Customer.CustomerReference;
import com.commercetools.models.CustomerGroup.CustomerGroupReference;
import com.commercetools.models.ProductDiscount.ProductDiscountReference;
import com.commercetools.models.ProductType.ProductTypeReference;
import com.commercetools.models.State.StateReference;
import com.commercetools.models.TaxCategory.TaxCategoryReference;
import com.commercetools.models.Type.TypeReference;
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
   @JsonSubTypes.Type(value = com.commercetools.models.Type.TypeReferenceImpl.class, name = "type"),
   @JsonSubTypes.Type(value = com.commercetools.models.Customer.CustomerReferenceImpl.class, name = "customer"),
   @JsonSubTypes.Type(value = com.commercetools.models.CustomerGroup.CustomerGroupReferenceImpl.class, name = "customer-group"),
   @JsonSubTypes.Type(value = com.commercetools.models.Category.CategoryReferenceImpl.class, name = "category"),
   @JsonSubTypes.Type(value = com.commercetools.models.ProductType.ProductTypeReferenceImpl.class, name = "product-type"),
   @JsonSubTypes.Type(value = com.commercetools.models.TaxCategory.TaxCategoryReferenceImpl.class, name = "tax-category"),
   @JsonSubTypes.Type(value = com.commercetools.models.State.StateReferenceImpl.class, name = "state"),
   @JsonSubTypes.Type(value = com.commercetools.models.Channel.ChannelReferenceImpl.class, name = "channel"),
   @JsonSubTypes.Type(value = com.commercetools.models.ProductDiscount.ProductDiscountReferenceImpl.class, name = "product-discount")
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
public interface Reference  {

   
   
   @JsonProperty("id")
   public String getId();
   
   
   @JsonProperty("key")
   public String getKey();

   public void setId(final String id);
   
   public void setKey(final String key);

   public String toJson();
   
   

}
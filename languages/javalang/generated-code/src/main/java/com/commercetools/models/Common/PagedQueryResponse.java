package com.commercetools.models.Common;

import com.commercetools.models.Common.BaseResource;
import java.lang.Long;
import com.commercetools.models.Common.PagedQueryResponseImpl;

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
@JsonDeserialize(as = PagedQueryResponseImpl.class)
public interface PagedQueryResponse  {

   
   @NotNull
   @JsonProperty("count")
   public Long getCount();
   
   
   @JsonProperty("total")
   public Long getTotal();
   
   @NotNull
   @JsonProperty("offset")
   public Long getOffset();
   
   @NotNull
   @Valid
   @JsonProperty("results")
   public List<BaseResource> getResults();

   public void setCount(final Long count);
   
   public void setTotal(final Long total);
   
   public void setOffset(final Long offset);
   
   public void setResults(final List<BaseResource> results);

   public String toJson();
   
   public static PagedQueryResponseImpl of(){
      return new PagedQueryResponseImpl();
   }
   
   
   public static PagedQueryResponseImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, PagedQueryResponseImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
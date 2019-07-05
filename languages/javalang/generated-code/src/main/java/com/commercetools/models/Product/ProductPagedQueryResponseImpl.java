package com.commercetools.models.Product;

import com.commercetools.models.Product.Product;
import java.lang.Long;
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
public class ProductPagedQueryResponseImpl implements ProductPagedQueryResponse {

   private Long total;
   
   private Long offset;
   
   private Long count;
   
   private List<Product> results;

   @JsonCreator
   ProductPagedQueryResponseImpl(@JsonProperty("total") final Long total, @JsonProperty("offset") final Long offset, @JsonProperty("count") final Long count, @JsonProperty("results") final List<Product> results) {
      this.total = total;
      this.offset = offset;
      this.count = count;
      this.results = results;
   }
   public ProductPagedQueryResponseImpl() {
      
   }
   
   
   public Long getTotal(){
      return this.total;
   }
   
   
   public Long getOffset(){
      return this.offset;
   }
   
   
   public Long getCount(){
      return this.count;
   }
   
   
   public List<Product> getResults(){
      return this.results;
   }

   public void setTotal(final Long total){
      this.total = total;
   }
   
   public void setOffset(final Long offset){
      this.offset = offset;
   }
   
   public void setCount(final Long count){
      this.count = count;
   }
   
   public void setResults(final List<Product> results){
      this.results = results;
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
package com.commercetools.models.Product;

import java.lang.Object;
import java.lang.String;
import com.commercetools.models.Product.SearchKeywordImpl;

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
@JsonDeserialize(as = SearchKeywordImpl.class)
public interface SearchKeyword  {

   
   @NotNull
   @JsonProperty("text")
   public String getText();
   
   
   @JsonProperty("suggestTokenizer")
   public Object getSuggestTokenizer();

   public void setText(final String text);
   
   public void setSuggestTokenizer(final Object suggestTokenizer);

   public String toJson();
   
   public static SearchKeywordImpl of(){
      return new SearchKeywordImpl();
   }
   
   
   public static SearchKeywordImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, SearchKeywordImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
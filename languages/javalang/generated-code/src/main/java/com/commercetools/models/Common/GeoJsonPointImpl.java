package com.commercetools.models.Common;

import com.commercetools.models.Common.GeoJson;
import java.lang.Integer;
import java.lang.String;
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
public class GeoJsonPointImpl implements GeoJsonPoint {

   private List<Integer> coordinates;

   @JsonCreator
   GeoJsonPointImpl(@JsonProperty("coordinates") final List<Integer> coordinates) {
      this.coordinates = coordinates;
   }
   public GeoJsonPointImpl() {
      
   }
   
   
   public List<Integer> getCoordinates(){
      return this.coordinates;
   }

   public void setCoordinates(final List<Integer> coordinates){
      this.coordinates = coordinates;
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
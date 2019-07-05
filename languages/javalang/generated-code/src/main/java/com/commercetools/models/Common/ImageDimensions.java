package com.commercetools.models.Common;

import java.lang.Integer;
import com.commercetools.models.Common.ImageDimensionsImpl;

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
@JsonDeserialize(as = ImageDimensionsImpl.class)
public interface ImageDimensions  {

   
   @NotNull
   @JsonProperty("w")
   public Integer getW();
   
   @NotNull
   @JsonProperty("h")
   public Integer getH();

   public void setW(final Integer w);
   
   public void setH(final Integer h);

   public String toJson();
   
   public static ImageDimensionsImpl of(){
      return new ImageDimensionsImpl();
   }
   
   
   public static ImageDimensionsImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, ImageDimensionsImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
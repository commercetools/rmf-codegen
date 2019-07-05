package com.commercetools.models.Common;

import com.commercetools.models.Common.AssetSource;
import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Type.CustomFields;
import java.lang.String;
import com.commercetools.models.Common.AssetImpl;

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
@JsonDeserialize(as = AssetImpl.class)
public interface Asset  {

   
   @NotNull
   @JsonProperty("id")
   public String getId();
   
   @NotNull
   @Valid
   @JsonProperty("sources")
   public List<AssetSource> getSources();
   
   @NotNull
   @Valid
   @JsonProperty("name")
   public LocalizedString getName();
   
   @Valid
   @JsonProperty("description")
   public LocalizedString getDescription();
   
   
   @JsonProperty("tags")
   public List<String> getTags();
   
   @Valid
   @JsonProperty("custom")
   public CustomFields getCustom();
   
   
   @JsonProperty("key")
   public String getKey();

   public void setId(final String id);
   
   public void setSources(final List<AssetSource> sources);
   
   public void setName(final LocalizedString name);
   
   public void setDescription(final LocalizedString description);
   
   public void setTags(final List<String> tags);
   
   public void setCustom(final CustomFields custom);
   
   public void setKey(final String key);

   public String toJson();
   
   public static AssetImpl of(){
      return new AssetImpl();
   }
   
   
   public static AssetImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, AssetImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
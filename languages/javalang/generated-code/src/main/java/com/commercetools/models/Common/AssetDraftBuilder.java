package com.commercetools.models.Common;

import com.commercetools.models.Common.AssetSource;
import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Type.CustomFieldsDraft;
import java.lang.String;
import com.commercetools.models.Common.AssetDraft;
import javax.annotation.Nullable;
import java.util.List;

public final class AssetDraftBuilder {
   
   
   private List<AssetSource> sources;
   
   @Nullable
   private CustomFieldsDraft custom;
   
   
   private LocalizedString name;
   
   @Nullable
   private LocalizedString description;
   
   @Nullable
   private String key;
   
   @Nullable
   private List<String> tags;
   
   public AssetDraftBuilder sources( final List<AssetSource> sources) {
      this.sources = sources;
      return this;
   }
   
   public AssetDraftBuilder custom(@Nullable final CustomFieldsDraft custom) {
      this.custom = custom;
      return this;
   }
   
   public AssetDraftBuilder name( final LocalizedString name) {
      this.name = name;
      return this;
   }
   
   public AssetDraftBuilder description(@Nullable final LocalizedString description) {
      this.description = description;
      return this;
   }
   
   public AssetDraftBuilder key(@Nullable final String key) {
      this.key = key;
      return this;
   }
   
   public AssetDraftBuilder tags(@Nullable final List<String> tags) {
      this.tags = tags;
      return this;
   }
   
   
   public List<AssetSource> getSources(){
      return this.sources;
   }
   
   @Nullable
   public CustomFieldsDraft getCustom(){
      return this.custom;
   }
   
   
   public LocalizedString getName(){
      return this.name;
   }
   
   @Nullable
   public LocalizedString getDescription(){
      return this.description;
   }
   
   @Nullable
   public String getKey(){
      return this.key;
   }
   
   @Nullable
   public List<String> getTags(){
      return this.tags;
   }

   public AssetDraft build() {
       return new AssetDraftImpl(sources, custom, name, description, key, tags);
   }
   
   
   public static AssetDraftBuilder of() {
      return new AssetDraftBuilder();
   }
   
   
   public static AssetDraftBuilder of(final AssetDraft template) {
      AssetDraftBuilder builder = new AssetDraftBuilder();
      builder.sources = template.getSources();
      builder.custom = template.getCustom();
      builder.name = template.getName();
      builder.description = template.getDescription();
      builder.key = template.getKey();
      builder.tags = template.getTags();
      return builder;
   }
   
}
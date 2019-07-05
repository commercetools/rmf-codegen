package com.commercetools.models.Common;

import com.commercetools.models.Channel.ChannelReference;
import com.commercetools.models.Common.Money;
import com.commercetools.models.Common.PriceTier;
import com.commercetools.models.CustomerGroup.CustomerGroupReference;
import com.commercetools.models.Type.CustomFieldsDraft;
import java.lang.String;
import java.time.ZonedDateTime;
import com.commercetools.models.Common.PriceDraft;
import javax.annotation.Nullable;
import java.util.List;

public final class PriceDraftBuilder {
   
   @Nullable
   private String country;
   
   @Nullable
   private List<PriceTier> tiers;
   
   @Nullable
   private CustomerGroupReference customerGroup;
   
   @Nullable
   private CustomFieldsDraft custom;
   
   @Nullable
   private ChannelReference channel;
   
   @Nullable
   private ZonedDateTime validUntil;
   
   @Nullable
   private ZonedDateTime validFrom;
   
   
   private Money value;
   
   public PriceDraftBuilder country(@Nullable final String country) {
      this.country = country;
      return this;
   }
   
   public PriceDraftBuilder tiers(@Nullable final List<PriceTier> tiers) {
      this.tiers = tiers;
      return this;
   }
   
   public PriceDraftBuilder customerGroup(@Nullable final CustomerGroupReference customerGroup) {
      this.customerGroup = customerGroup;
      return this;
   }
   
   public PriceDraftBuilder custom(@Nullable final CustomFieldsDraft custom) {
      this.custom = custom;
      return this;
   }
   
   public PriceDraftBuilder channel(@Nullable final ChannelReference channel) {
      this.channel = channel;
      return this;
   }
   
   public PriceDraftBuilder validUntil(@Nullable final ZonedDateTime validUntil) {
      this.validUntil = validUntil;
      return this;
   }
   
   public PriceDraftBuilder validFrom(@Nullable final ZonedDateTime validFrom) {
      this.validFrom = validFrom;
      return this;
   }
   
   public PriceDraftBuilder value( final Money value) {
      this.value = value;
      return this;
   }
   
   @Nullable
   public String getCountry(){
      return this.country;
   }
   
   @Nullable
   public List<PriceTier> getTiers(){
      return this.tiers;
   }
   
   @Nullable
   public CustomerGroupReference getCustomerGroup(){
      return this.customerGroup;
   }
   
   @Nullable
   public CustomFieldsDraft getCustom(){
      return this.custom;
   }
   
   @Nullable
   public ChannelReference getChannel(){
      return this.channel;
   }
   
   @Nullable
   public ZonedDateTime getValidUntil(){
      return this.validUntil;
   }
   
   @Nullable
   public ZonedDateTime getValidFrom(){
      return this.validFrom;
   }
   
   
   public Money getValue(){
      return this.value;
   }

   public PriceDraft build() {
       return new PriceDraftImpl(country, tiers, customerGroup, custom, channel, validUntil, validFrom, value);
   }
   
   
   public static PriceDraftBuilder of() {
      return new PriceDraftBuilder();
   }
   
   
   public static PriceDraftBuilder of(final PriceDraft template) {
      PriceDraftBuilder builder = new PriceDraftBuilder();
      builder.country = template.getCountry();
      builder.tiers = template.getTiers();
      builder.customerGroup = template.getCustomerGroup();
      builder.custom = template.getCustom();
      builder.channel = template.getChannel();
      builder.validUntil = template.getValidUntil();
      builder.validFrom = template.getValidFrom();
      builder.value = template.getValue();
      return builder;
   }
   
}
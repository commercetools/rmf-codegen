package com.commercetools.models.Customer;

import com.commercetools.models.Common.Address;
import com.commercetools.models.Common.LoggedResource;
import com.commercetools.models.CustomerGroup.CustomerGroupReference;
import com.commercetools.models.Type.CustomFields;
import java.lang.Boolean;
import java.lang.String;
import java.time.LocalDate;
import com.commercetools.models.Customer.CustomerImpl;

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
@JsonDeserialize(as = CustomerImpl.class)
public interface Customer extends LoggedResource {

   
   
   @JsonProperty("customerNumber")
   public String getCustomerNumber();
   
   @NotNull
   @JsonProperty("email")
   public String getEmail();
   
   @NotNull
   @JsonProperty("password")
   public String getPassword();
   
   
   @JsonProperty("firstName")
   public String getFirstName();
   
   
   @JsonProperty("lastName")
   public String getLastName();
   
   
   @JsonProperty("middleName")
   public String getMiddleName();
   
   
   @JsonProperty("title")
   public String getTitle();
   
   
   @JsonProperty("dateOfBirth")
   public LocalDate getDateOfBirth();
   
   
   @JsonProperty("companyName")
   public String getCompanyName();
   
   
   @JsonProperty("vatId")
   public String getVatId();
   
   @NotNull
   @Valid
   @JsonProperty("addresses")
   public List<Address> getAddresses();
   
   
   @JsonProperty("defaultShippingAddressId")
   public String getDefaultShippingAddressId();
   
   
   @JsonProperty("shippingAddressIds")
   public List<String> getShippingAddressIds();
   
   
   @JsonProperty("defaultBillingAddressId")
   public String getDefaultBillingAddressId();
   
   
   @JsonProperty("billingAddressIds")
   public List<String> getBillingAddressIds();
   
   @NotNull
   @JsonProperty("isEmailVerified")
   public Boolean getIsEmailVerified();
   
   
   @JsonProperty("externalId")
   public String getExternalId();
   
   @Valid
   @JsonProperty("customerGroup")
   public CustomerGroupReference getCustomerGroup();
   
   @Valid
   @JsonProperty("custom")
   public CustomFields getCustom();
   
   
   @JsonProperty("locale")
   public String getLocale();
   
   
   @JsonProperty("salutation")
   public String getSalutation();
   
   
   @JsonProperty("key")
   public String getKey();

   public void setCustomerNumber(final String customerNumber);
   
   public void setEmail(final String email);
   
   public void setPassword(final String password);
   
   public void setFirstName(final String firstName);
   
   public void setLastName(final String lastName);
   
   public void setMiddleName(final String middleName);
   
   public void setTitle(final String title);
   
   public void setDateOfBirth(final LocalDate dateOfBirth);
   
   public void setCompanyName(final String companyName);
   
   public void setVatId(final String vatId);
   
   public void setAddresses(final List<Address> addresses);
   
   public void setDefaultShippingAddressId(final String defaultShippingAddressId);
   
   public void setShippingAddressIds(final List<String> shippingAddressIds);
   
   public void setDefaultBillingAddressId(final String defaultBillingAddressId);
   
   public void setBillingAddressIds(final List<String> billingAddressIds);
   
   public void setIsEmailVerified(final Boolean isEmailVerified);
   
   public void setExternalId(final String externalId);
   
   public void setCustomerGroup(final CustomerGroupReference customerGroup);
   
   public void setCustom(final CustomFields custom);
   
   public void setLocale(final String locale);
   
   public void setSalutation(final String salutation);
   
   public void setKey(final String key);

   public String toJson();
   
   public static CustomerImpl of(){
      return new CustomerImpl();
   }
   
   
   public static CustomerImpl of(String json) {
      try{
          return CommercetoolsJsonUtils.fromJsonString(json, CustomerImpl.class);
      }catch (IOException e){
          e.printStackTrace();
      }
      return null;
   }

}
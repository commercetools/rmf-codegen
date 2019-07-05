package com.commercetools.models.Customer;

import com.commercetools.models.Common.Address;
import com.commercetools.models.Common.LoggedResource;
import com.commercetools.models.CustomerGroup.CustomerGroupReference;
import com.commercetools.models.Type.CustomFields;
import java.lang.Boolean;
import java.lang.String;
import java.time.LocalDate;
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
public class CustomerImpl implements Customer {

   private ZonedDateTime createdAt;
   
   private ZonedDateTime lastModifiedAt;
   
   private String id;
   
   private Long version;
   
   private com.commercetools.models.Common.CreatedBy createdBy;
   
   private com.commercetools.models.Common.LastModifiedBy lastModifiedBy;
   
   private String lastName;
   
   private List<Address> addresses;
   
   private com.commercetools.models.CustomerGroup.CustomerGroupReference customerGroup;
   
   private com.commercetools.models.Type.CustomFields custom;
   
   private String companyName;
   
   private String vatId;
   
   private String externalId;
   
   private LocalDate dateOfBirth;
   
   private String locale;
   
   private List<String> billingAddressIds;
   
   private String defaultShippingAddressId;
   
   private String title;
   
   private String customerNumber;
   
   private Boolean isEmailVerified;
   
   private String defaultBillingAddressId;
   
   private List<String> shippingAddressIds;
   
   private String firstName;
   
   private String password;
   
   private String middleName;
   
   private String salutation;
   
   private String key;
   
   private String email;

   @JsonCreator
   CustomerImpl(@JsonProperty("createdAt") final ZonedDateTime createdAt, @JsonProperty("lastModifiedAt") final ZonedDateTime lastModifiedAt, @JsonProperty("id") final String id, @JsonProperty("version") final Long version, @JsonProperty("createdBy") final com.commercetools.models.Common.CreatedBy createdBy, @JsonProperty("lastModifiedBy") final com.commercetools.models.Common.LastModifiedBy lastModifiedBy, @JsonProperty("lastName") final String lastName, @JsonProperty("addresses") final List<Address> addresses, @JsonProperty("customerGroup") final com.commercetools.models.CustomerGroup.CustomerGroupReference customerGroup, @JsonProperty("custom") final com.commercetools.models.Type.CustomFields custom, @JsonProperty("companyName") final String companyName, @JsonProperty("vatId") final String vatId, @JsonProperty("externalId") final String externalId, @JsonProperty("dateOfBirth") final LocalDate dateOfBirth, @JsonProperty("locale") final String locale, @JsonProperty("billingAddressIds") final List<String> billingAddressIds, @JsonProperty("defaultShippingAddressId") final String defaultShippingAddressId, @JsonProperty("title") final String title, @JsonProperty("customerNumber") final String customerNumber, @JsonProperty("isEmailVerified") final Boolean isEmailVerified, @JsonProperty("defaultBillingAddressId") final String defaultBillingAddressId, @JsonProperty("shippingAddressIds") final List<String> shippingAddressIds, @JsonProperty("firstName") final String firstName, @JsonProperty("password") final String password, @JsonProperty("middleName") final String middleName, @JsonProperty("salutation") final String salutation, @JsonProperty("key") final String key, @JsonProperty("email") final String email) {
      this.createdAt = createdAt;
      this.lastModifiedAt = lastModifiedAt;
      this.id = id;
      this.version = version;
      this.createdBy = createdBy;
      this.lastModifiedBy = lastModifiedBy;
      this.lastName = lastName;
      this.addresses = addresses;
      this.customerGroup = customerGroup;
      this.custom = custom;
      this.companyName = companyName;
      this.vatId = vatId;
      this.externalId = externalId;
      this.dateOfBirth = dateOfBirth;
      this.locale = locale;
      this.billingAddressIds = billingAddressIds;
      this.defaultShippingAddressId = defaultShippingAddressId;
      this.title = title;
      this.customerNumber = customerNumber;
      this.isEmailVerified = isEmailVerified;
      this.defaultBillingAddressId = defaultBillingAddressId;
      this.shippingAddressIds = shippingAddressIds;
      this.firstName = firstName;
      this.password = password;
      this.middleName = middleName;
      this.salutation = salutation;
      this.key = key;
      this.email = email;
   }
   public CustomerImpl() {
      
   }
   
   
   public ZonedDateTime getCreatedAt(){
      return this.createdAt;
   }
   
   
   public ZonedDateTime getLastModifiedAt(){
      return this.lastModifiedAt;
   }
   
   
   public String getId(){
      return this.id;
   }
   
   
   public Long getVersion(){
      return this.version;
   }
   
   
   public com.commercetools.models.Common.CreatedBy getCreatedBy(){
      return this.createdBy;
   }
   
   
   public com.commercetools.models.Common.LastModifiedBy getLastModifiedBy(){
      return this.lastModifiedBy;
   }
   
   
   public String getLastName(){
      return this.lastName;
   }
   
   
   public List<Address> getAddresses(){
      return this.addresses;
   }
   
   
   public com.commercetools.models.CustomerGroup.CustomerGroupReference getCustomerGroup(){
      return this.customerGroup;
   }
   
   
   public com.commercetools.models.Type.CustomFields getCustom(){
      return this.custom;
   }
   
   
   public String getCompanyName(){
      return this.companyName;
   }
   
   
   public String getVatId(){
      return this.vatId;
   }
   
   
   public String getExternalId(){
      return this.externalId;
   }
   
   
   public LocalDate getDateOfBirth(){
      return this.dateOfBirth;
   }
   
   
   public String getLocale(){
      return this.locale;
   }
   
   
   public List<String> getBillingAddressIds(){
      return this.billingAddressIds;
   }
   
   
   public String getDefaultShippingAddressId(){
      return this.defaultShippingAddressId;
   }
   
   
   public String getTitle(){
      return this.title;
   }
   
   
   public String getCustomerNumber(){
      return this.customerNumber;
   }
   
   
   public Boolean getIsEmailVerified(){
      return this.isEmailVerified;
   }
   
   
   public String getDefaultBillingAddressId(){
      return this.defaultBillingAddressId;
   }
   
   
   public List<String> getShippingAddressIds(){
      return this.shippingAddressIds;
   }
   
   
   public String getFirstName(){
      return this.firstName;
   }
   
   
   public String getPassword(){
      return this.password;
   }
   
   
   public String getMiddleName(){
      return this.middleName;
   }
   
   
   public String getSalutation(){
      return this.salutation;
   }
   
   
   public String getKey(){
      return this.key;
   }
   
   
   public String getEmail(){
      return this.email;
   }

   public void setCreatedAt(final ZonedDateTime createdAt){
      this.createdAt = createdAt;
   }
   
   public void setLastModifiedAt(final ZonedDateTime lastModifiedAt){
      this.lastModifiedAt = lastModifiedAt;
   }
   
   public void setId(final String id){
      this.id = id;
   }
   
   public void setVersion(final Long version){
      this.version = version;
   }
   
   public void setCreatedBy(final com.commercetools.models.Common.CreatedBy createdBy){
      this.createdBy = createdBy;
   }
   
   public void setLastModifiedBy(final com.commercetools.models.Common.LastModifiedBy lastModifiedBy){
      this.lastModifiedBy = lastModifiedBy;
   }
   
   public void setLastName(final String lastName){
      this.lastName = lastName;
   }
   
   public void setAddresses(final List<Address> addresses){
      this.addresses = addresses;
   }
   
   public void setCustomerGroup(final com.commercetools.models.CustomerGroup.CustomerGroupReference customerGroup){
      this.customerGroup = customerGroup;
   }
   
   public void setCustom(final com.commercetools.models.Type.CustomFields custom){
      this.custom = custom;
   }
   
   public void setCompanyName(final String companyName){
      this.companyName = companyName;
   }
   
   public void setVatId(final String vatId){
      this.vatId = vatId;
   }
   
   public void setExternalId(final String externalId){
      this.externalId = externalId;
   }
   
   public void setDateOfBirth(final LocalDate dateOfBirth){
      this.dateOfBirth = dateOfBirth;
   }
   
   public void setLocale(final String locale){
      this.locale = locale;
   }
   
   public void setBillingAddressIds(final List<String> billingAddressIds){
      this.billingAddressIds = billingAddressIds;
   }
   
   public void setDefaultShippingAddressId(final String defaultShippingAddressId){
      this.defaultShippingAddressId = defaultShippingAddressId;
   }
   
   public void setTitle(final String title){
      this.title = title;
   }
   
   public void setCustomerNumber(final String customerNumber){
      this.customerNumber = customerNumber;
   }
   
   public void setIsEmailVerified(final Boolean isEmailVerified){
      this.isEmailVerified = isEmailVerified;
   }
   
   public void setDefaultBillingAddressId(final String defaultBillingAddressId){
      this.defaultBillingAddressId = defaultBillingAddressId;
   }
   
   public void setShippingAddressIds(final List<String> shippingAddressIds){
      this.shippingAddressIds = shippingAddressIds;
   }
   
   public void setFirstName(final String firstName){
      this.firstName = firstName;
   }
   
   public void setPassword(final String password){
      this.password = password;
   }
   
   public void setMiddleName(final String middleName){
      this.middleName = middleName;
   }
   
   public void setSalutation(final String salutation){
      this.salutation = salutation;
   }
   
   public void setKey(final String key){
      this.key = key;
   }
   
   public void setEmail(final String email){
      this.email = email;
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
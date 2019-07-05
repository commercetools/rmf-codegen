package com.commercetools.models.Common;

import com.commercetools.models.Common.Money;
import com.commercetools.models.Common.MoneyType;
import java.lang.Integer;


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
public interface TypedMoney extends Money {

   
   @NotNull
   @JsonProperty("fractionDigits")
   public Integer getFractionDigits();

   public void setFractionDigits(final Integer fractionDigits);

   public String toJson();
   
   

}
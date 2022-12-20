package com.commercetools.rmf.validators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Apply {
    @JacksonXmlProperty(localName = "set")
    private String set;

    @JsonCreator
    public Apply() {
    }

    public Apply(String set) {
        this.set = set;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }
}

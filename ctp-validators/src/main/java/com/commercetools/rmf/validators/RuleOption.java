package com.commercetools.rmf.validators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;


public class RuleOption {
    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private String type;
    @JacksonXmlText
    private String value;

    @JsonCreator
    public RuleOption() {
    }

    public RuleOption(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

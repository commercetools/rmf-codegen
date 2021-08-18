package com.commercetools.rmf.validators;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;
import java.util.Map;

public class Rule {
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlElementWrapper(localName = "options")
    private List<RuleOption> options;

    @JsonCreator
    public Rule() {
    }

    public Rule(String name, List<RuleOption> options) {
        this.name = name;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RuleOption> getOptions() {
        return options;
    }

    public void setOptions(List<RuleOption> options) {
        this.options = options;
    }
}

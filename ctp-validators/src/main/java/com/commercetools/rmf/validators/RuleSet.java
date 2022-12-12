package com.commercetools.rmf.validators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "ruleset")
public class RuleSet {
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "apply")
    private List<Apply> apply = new ArrayList<>();

    @JacksonXmlElementWrapper(localName = "rules")
    @JacksonXmlProperty(localName = "rule")
    private List<Rule> rules;

    @JsonCreator
    public RuleSet() {
    }

    public RuleSet(String name, List<Rule> rules) {
        this.name = name;
        this.rules = rules;
    }

    public RuleSet(String name, List<Apply> apply, List<Rule> rules) {
        this.name = name;
        this.apply = apply;
        this.rules = rules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Apply> getApply() {
        return apply;
    }

    public void setApply(List<Apply> apply) {
        this.apply = apply;
    }
}


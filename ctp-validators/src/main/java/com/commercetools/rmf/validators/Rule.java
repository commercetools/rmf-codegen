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

    @JacksonXmlProperty(localName = "severity")
    private RuleSeverity severity;

    @JacksonXmlProperty(localName = "enabled")
    private Boolean enabled = true;

    @JsonCreator
    public Rule() {
    }

    public Rule(String name, RuleSeverity severity, List<RuleOption> options) {
        this.name = name;
        this.options = options;
        this.severity = severity;
    }

    public Rule(String name, RuleSeverity severity, List<RuleOption> options, Boolean enabled) {
        this.name = name;
        this.options = options;
        this.severity = severity;
        this.enabled = enabled;
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

    public RuleSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(RuleSeverity severity) {
        this.severity = severity;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}

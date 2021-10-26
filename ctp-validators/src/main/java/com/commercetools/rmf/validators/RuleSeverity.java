package com.commercetools.rmf.validators;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RuleSeverity {
    @JsonProperty("error")
    ERROR("error"),
    @JsonProperty("warn")
    WARN("warn"),
    @JsonProperty("info")
    INFO("info");

    private final String severity;

    RuleSeverity(String severity) {
        this.severity = severity;
    }

    public String getSeverity() {
        return severity;
    }
}

package io.vrap.rmf.codegen.cli.diff;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CheckSeverity {
    @JsonProperty("error")
    ERROR("error"),
    @JsonProperty("warn")
    WARN("warn"),
    @JsonProperty("info")
    INFO("info");

    private final String severity;

    CheckSeverity(String severity) {
        this.severity = severity;
    }

    public String getSeverity() {
        return severity;
    }
}

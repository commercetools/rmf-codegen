package com.commercetools.rmf.diff;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public enum CheckSeverity {
    @JsonProperty("info")
    INFO("info", 100),
    @JsonProperty("warn")
    WARN("warn", 200),
    @JsonProperty("error")
    ERROR("error", 400),
    @JsonProperty("fatal")
    FATAL("fatal", 500);

    public static final String VALID_VALUES = "info, warn, error";

    private final String severity;

    private final int value;

    CheckSeverity(String severity, int value) {
        this.severity = severity; this.value = value;
    }

    public String getSeverity() {
        return severity;
    }

    public int getValue() {
        return value;
    }
}

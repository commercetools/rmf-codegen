package com.commercetools.rmf.validators

import com.fasterxml.jackson.annotation.JsonProperty

enum class RuleSeverity(val severity: String) {
    @JsonProperty("error")
    ERROR("error"),
    @JsonProperty("warn")
    WARN("warn"),
    @JsonProperty("info")
    INFO("info")
}

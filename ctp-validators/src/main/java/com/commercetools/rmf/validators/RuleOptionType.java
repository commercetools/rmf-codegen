package com.commercetools.rmf.validators;

enum RuleOptionType {
    EXCLUDE("exclude"),
    ACTION_VERB("action-verb");

    private final String type;

    RuleOptionType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }


}

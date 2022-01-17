package com.commercetools.rmf.validators;

enum RuleOptionType {
    EXCLUDE("exclude");

    private final String type;

    RuleOptionType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }


}

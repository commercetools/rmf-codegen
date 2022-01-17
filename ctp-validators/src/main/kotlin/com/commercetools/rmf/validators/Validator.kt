package com.commercetools.rmf.validators

interface Validator<T> {
    fun ValidatorType(): Class<T>

}

interface ValidatorFactory<out T> {
    fun create(options: List<RuleOption>? = null): T
    fun create(severity: RuleSeverity, options: List<RuleOption>? = null): T
}

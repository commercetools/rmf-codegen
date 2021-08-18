package com.commercetools.rmf.validators

interface Validator<T> {
    fun ValidatorType(): Class<T>
}

interface ValidatorFactory<T> {
    fun create(options: List<RuleOption>): T

}

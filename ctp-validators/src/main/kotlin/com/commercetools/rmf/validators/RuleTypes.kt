package com.commercetools.rmf.validators

annotation class ValidatorSets(val value: Array<ValidatorSet>)

@JvmRepeatable(ValidatorSets::class)
annotation class ValidatorSet(val name: String = "default", val severity: RuleSeverity = RuleSeverity.ERROR)

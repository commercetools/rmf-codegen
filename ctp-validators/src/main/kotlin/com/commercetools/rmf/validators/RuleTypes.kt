package com.commercetools.rmf.validators

annotation class RulesSets(val value: Array<RulesSet>)

@JvmRepeatable(RulesSets::class)
annotation class RulesSet(val name: String = "default", val severity: RuleSeverity = RuleSeverity.INFO)

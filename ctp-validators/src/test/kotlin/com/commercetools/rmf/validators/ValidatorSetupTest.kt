package com.commercetools.rmf.validators

import org.junit.Test

class ValidatorSetupTest : ValidatorFixtures {
    @Test
    fun setup() {
        val validators = ValidatorSetup.setup(fileFromClasspath("/ruleset.xml"))
    }
}

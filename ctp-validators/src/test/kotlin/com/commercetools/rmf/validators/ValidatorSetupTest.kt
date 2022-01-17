package com.commercetools.rmf.validators

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test


class ValidatorSetupTest : ValidatorFixtures {
    @Test
    fun setup() {
        val validators = ValidatorSetup.setup(fileFromClasspath("/ruleset.xml"))
        Assertions.assertThat(validators).isNotEmpty
    }
}

package com.commercetools.rmf.diff

import com.commercetools.rmf.validators.ValidatorFixtures
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ValidatorSetupTest : ValidatorFixtures {
    @Test
    fun setup() {
        val validators = DiffSetup.setup(fileFromClasspath("/diff.xml"))
        Assertions.assertThat(validators).isNotEmpty
    }
}

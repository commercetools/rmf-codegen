package io.vrap.rmf.codegen.cli.diff

import com.commercetools.rmf.diff.DiffSetup
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ValidatorSetupTest : ValidatorFixtures {
    @Test
    fun setup() {
        val validators = DiffSetup.setup(fileFromClasspath("/diff.xml"))
        Assertions.assertThat(validators).isNotEmpty
    }
}

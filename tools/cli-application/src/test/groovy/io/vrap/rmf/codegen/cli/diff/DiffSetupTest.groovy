package io.vrap.rmf.codegen.cli.diff

import io.vrap.rmf.codegen.cli.ValidateSubcommand
import spock.lang.Specification

class DiffSetupTest extends Specification implements ValidatorFixtures {
    def "default set"() {
        when:
        def checks = DiffSetup.setup(ValidateSubcommand.class.getResourceAsStream("/diff.xml"))
        then:
        checks.size() > 0
    }

}

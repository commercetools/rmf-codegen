package com.commercetools.rmf.diff

import com.commercetools.rmf.validators.ValidatorFixtures
import spock.lang.Specification

class DiffSetupTest extends Specification implements ValidatorFixtures {
    def "default set"() {
        when:
        def checks = DiffSetup.setup(DiffSetup.class.getResourceAsStream("/diff.xml"))
        then:
        checks.size() > 0
    }

}

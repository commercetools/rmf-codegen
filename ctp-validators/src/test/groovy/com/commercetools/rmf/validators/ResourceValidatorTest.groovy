package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.validation.RamlValidationSetup
import spock.lang.Specification

class ResourceValidatorTest extends Specification implements ValidatorFixtures {
    def "plural rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator())
        RamlValidationSetup.setup(validators)
        def uri = uriFromClasspath("/plural-resource.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators)
        then:
        result.validationResults.size == 0
    }
}

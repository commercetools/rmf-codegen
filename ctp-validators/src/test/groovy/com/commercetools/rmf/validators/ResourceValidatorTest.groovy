package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.validation.RamlValidator
import spock.lang.Specification

class ResourceValidatorTest extends Specification implements ValidatorFixtures {
    def "plural rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(new PluralRule())))
        def uri = uriFromClasspath("/plural-resource.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 0
    }

    def "plural rule invalid"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(new PluralRule())))
        def uri = uriFromClasspath("/plural-resource-invalid.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
    }
}

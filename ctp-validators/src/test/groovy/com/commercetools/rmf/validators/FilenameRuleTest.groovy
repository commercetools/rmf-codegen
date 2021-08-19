package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.validation.RamlValidator
import spock.lang.Specification

class FilenameRuleTest extends Specification implements ValidatorFixtures {
    def "test filename rule"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(new FilenameRule())))
        def uri = uriFromClasspath("/filename-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "Type FileNameInvalid must have the same file name as type itself"
    }
}

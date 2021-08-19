package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.validation.RamlValidator
import spock.lang.Specification

class ValidatorRulesTest extends Specification implements ValidatorFixtures {
    def "property camel case rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(new CamelCaseRule())))
        def uri = uriFromClasspath("/camelcase-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "Property \"invalid_case\" should be lower camel cased"
    }

    def "discriminator name rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(new DiscriminatorNameRule())))
        def uri = uriFromClasspath("/discriminatorname-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "Update action type \"BazFoo\" must have a discriminator value set"
        result.validationResults[1].message == "Update action type \"InvalidFoo\" name must contain the discriminator value \"boz\""
    }


    def "filename rule"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(new FilenameRule())))
        def uri = uriFromClasspath("/filename-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "Type \"FileNameInvalid\" must have the same file name as type itself"
    }

    def "property plural rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(new PropertyPluralRule())))
        def uri = uriFromClasspath("/propertyplural-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "Array property \"invalidItem\" must be plural"
    }

    def "property singular rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(new PropertySingularRule())))
        def uri = uriFromClasspath("/propertysingular-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "Non array property \"invalidItems\" must be singular"
    }

    def "resource plural rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(new ResourcePluralRule())))
        def uri = uriFromClasspath("/plural-resource.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 0
    }

    def "resource plural rule invalid"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(new ResourcePluralRule())))
        def uri = uriFromClasspath("/plural-resource-invalid.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "Resource \"invalid\" should be plural"
    }


    def "update action name rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(new UpdateActionNameRule())))
        def uri = uriFromClasspath("/updateaction-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "Update action type \"InvalidUpdate\" name must end with \"Action\""
    }

}

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
        result.validationResults[0].message == "Property \"invalid_case\" must be lower camel cased"
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

    def "name string enum rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(new NamedStringEnumRule())))
        def uri = uriFromClasspath("/namedstringenum-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "Named string type \"InvalidString\" must define enum values"
        result.validationResults[1].message == "Named string type \"InvalidStringDesc\" must define enum values"
    }

    def "method post body rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(new PostBodyRule())))
        def uri = uriFromClasspath("/method-post-body-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "Method \"POST /invalid\" must have body type defined"
        result.validationResults[1].message == "Method \"POST /invalid-all\" must have body type defined"
    }

//    def "named body type rule"() {
//        when:
//        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(new NamedBodyTypeRule())))
//        def uri = uriFromClasspath("/named-body-type-rule.raml")
//        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
//        then:
//        result.validationResults.size == 6
//        result.validationResults[0].message == "Method \"POST /invalid\" must have body type defined"
//        result.validationResults[1].message == "Method \"POST /invalid-all\" must have body type defined"
//        result.validationResults[2].message == "Method \"POST /invalid-all\" must have body type defined"
//        result.validationResults[3].message == "Method \"POST /invalid-all\" must have body type defined"
//        result.validationResults[4].message == "Method \"POST /invalid-all\" must have body type defined"
//        result.validationResults[5].message == "Method \"POST /invalid-all\" must have body type defined"
//    }

    def "package defined rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(new PackageDefinedRule())))
        def uri = uriFromClasspath("/packagedefined-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "Type \"InvalidString\" must have package annotation defined"
        result.validationResults[1].message == "Type \"InvalidStringDesc\" must have package annotation defined"
    }

    def "library package defined rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(new PackageDefinedRule())))
        def uri = uriFromClasspath("/librarypackagedefined-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "Library type \"InvalidString\" must have package annotation defined"
        result.validationResults[1].message == "Library type \"InvalidStringDesc\" must have package annotation defined"
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
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(new StringPropertySingularRule())))
        def uri = uriFromClasspath("/propertysingular-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "Non array property \"invalidItems\" must be singular"
    }

    def "valid baseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(new SdkBaseUriRule())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-valid-baseuri.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 0
    }

    def "valid sdkBaseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(new SdkBaseUriRule())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-valid-sdkbaseuri.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 0
    }

    def "missing baseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(new SdkBaseUriRule())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-missing-baseuri.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "baseUri must not be empty"
    }

    def "missing sdkBaseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(new SdkBaseUriRule())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-missing-sdkbaseuri.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "sdkBaseUri must be declared as baseUri \"https://api.{region}.commercetools.com\" contains baseUriParameters"
    }

    def "invalid sdkBaseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(new SdkBaseUriRule())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-invalid-sdkbaseuri.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "sdkBaseUri \"https://api.{region}.commercetools.com\" must not contain uriParameters"
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
        result.validationResults[0].message == "Resource \"invalid\" must be plural"
    }

    def "success body rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(new SuccessBodyRule())))
        def uri = uriFromClasspath("/success-body-rule.raml")
        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
        then:
        result.validationResults.size == 3
        result.validationResults[0].message == "Method \"POST /invalid\" must have body type for success response(s) defined"
        result.validationResults[1].message == "Method \"POST /invalid-mixed\" must have body type for success response(s) defined"
        result.validationResults[2].message == "Method \"POST /invalid-all\" must have body type for success response(s) defined"
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

package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.validation.RamlValidator
import spock.lang.Specification

class ValidatorRulesTest extends Specification implements ValidatorFixtures {
    def "property camel case rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(CamelCaseRule.create())))
        def uri = uriFromClasspath("/camelcase-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "CamelCaseRule: Property \"invalid_case\" must be lower camel cased"
    }

    def "discriminator name rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(DiscriminatorNameRule.create())))
        def uri = uriFromClasspath("/discriminatorname-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "DiscriminatorNameRule: Update action type \"BazFoo\" must have a discriminator value set"
        result.validationResults[1].message == "DiscriminatorNameRule: Update action type \"InvalidFoo\" name must contain the discriminator value \"boz\""
    }

    def "discriminator parent rule"() {
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(DiscriminatorParentRule.create())))
        def uri = uriFromClasspath("/discriminatorparent-rule.raml")
        when:
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "DiscriminatorParentRule: Discriminator property \"type\" must defined in the type InvalidBar only"
    }


    def "filename rule"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(FilenameRule.create())))
        def uri = uriFromClasspath("/filename-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "FilenameRule: Type \"FileNameInvalid\" must have the same file name as type itself"
    }

    def "name string enum rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(NamedStringEnumRule.create())))
        def uri = uriFromClasspath("/namedstringenum-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "NamedStringEnumRule: Named string type \"InvalidString\" must define enum values"
        result.validationResults[1].message == "NamedStringEnumRule: Named string type \"InvalidStringDesc\" must define enum values"
    }

    def "method post body rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(PostBodyRule.create())))
        def uri = uriFromClasspath("/method-post-body-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "PostBodyRule: Method \"POST /invalid\" must have body type defined"
        result.validationResults[1].message == "PostBodyRule: Method \"POST /invalid-all\" must have body type defined"
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
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(PackageDefinedRule.create())))
        def uri = uriFromClasspath("/packagedefined-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "PackageDefinedRule: Type \"InvalidString\" must have package annotation defined"
        result.validationResults[1].message == "PackageDefinedRule: Type \"InvalidStringDesc\" must have package annotation defined"
    }

    def "library package defined rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(PackageDefinedRule.create())))
        def uri = uriFromClasspath("/librarypackagedefined-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "PackageDefinedRule: Library type \"InvalidString\" must have package annotation defined"
        result.validationResults[1].message == "PackageDefinedRule: Library type \"InvalidStringDesc\" must have package annotation defined"
    }

    def "property plural rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(PropertyPluralRule.create())))
        def uri = uriFromClasspath("/propertyplural-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "PropertyPluralRule: Array property \"invalidItem\" must be plural"
        result.validationResults[1].message == "PropertyPluralRule: Array property \"invalidItemDesc\" must be plural"
    }

    def "property singular rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(StringPropertySingularRule.create())))
        def uri = uriFromClasspath("/propertysingular-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "StringPropertySingularRule: Non array property \"invalidItems\" must be singular"
    }

    def "resource catch all rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(ResourceCatchAllRule.create())))
        def uri = uriFromClasspath("/resource-catchall-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "ResourceCatchAllRule: Resource \"invalid\" define only one catch all sub resource"
    }

    def "resource lower case hyphen rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(ResourceLowerCaseHyphenRule.create())))
        def uri = uriFromClasspath("/resource-lowercasehyphen-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "ResourceLowerCaseHyphenRule: Resource \"/{projectKey}/invalidResource\" must be lower case hyphen separated"
        result.validationResults[1].message == "ResourceLowerCaseHyphenRule: Resource \"/{projectKey}/another_invalid_resource\" must be lower case hyphen separated"
    }

    def "valid baseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-valid-baseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 0
    }

    def "valid sdkBaseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-valid-sdkbaseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 0
    }

    def "missing baseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-missing-baseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "SdkBaseUriRule: baseUri must not be empty"
    }

    def "missing sdkBaseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-missing-sdkbaseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "SdkBaseUriRule: sdkBaseUri must be declared as baseUri \"https://api.{region}.commercetools.com\" contains baseUriParameters"
    }

    def "invalid sdkBaseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create())))
        def uri = uriFromClasspath("/sdkbaseuri-rule-invalid-sdkbaseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "SdkBaseUriRule: sdkBaseUri \"https://api.{region}.commercetools.com\" must not contain uriParameters"
    }


    def "resource plural rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(ResourcePluralRule.create())))
        def uri = uriFromClasspath("/plural-resource.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 0
    }

    def "resource plural rule invalid"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(ResourcePluralRule.create())))
        def uri = uriFromClasspath("/plural-resource-invalid.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "ResourcePluralRule: Resource \"invalid\" must be plural"
    }

    def "success body rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(SuccessBodyRule.create())))
        def uri = uriFromClasspath("/success-body-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 3
        result.validationResults[0].message == "SuccessBodyRule: Method \"POST /invalid\" must have body type for success response(s) defined"
        result.validationResults[1].message == "SuccessBodyRule: Method \"POST /invalid-mixed\" must have body type for success response(s) defined"
        result.validationResults[2].message == "SuccessBodyRule: Method \"POST /invalid-all\" must have body type for success response(s) defined"
    }

    def "union type property rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(UnionTypePropertyRule.create())))
        def uri = uriFromClasspath("/uniontype-property-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 3
        result.validationResults[0].message == "UnionTypePropertyRule: Usage of union type is not allowed for property \"invalidItem\""
        result.validationResults[1].message == "UnionTypePropertyRule: Usage of union type is not allowed for property \"invalidItemDesc\""
        result.validationResults[2].message == "UnionTypePropertyRule: Usage of union type is not allowed for property \"/invalid[a-z]/\""
    }

    def "update action name rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(UpdateActionNameRule.create())))
        def uri = uriFromClasspath("/updateaction-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "UpdateActionNameRule: Update action type \"InvalidUpdate\" name must end with \"Action\""
    }

    def "uriparameters declared"() {
        when:
        def validators = Arrays.asList(new ResolvedResourcesValidator(Arrays.asList(UriParameterDeclaredRule.create())))
        def uri = uriFromClasspath("/uriparametersdeclared-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "UriParameterDeclaredRule: Resource \"/{test}\" must define all uri parameters"
        result.rootObject.resources[0].resources[0].resources[0].uriParameters[0].name == "id"
    }

    def "nested type rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(NestedTypeRule.create())))
        def uri = uriFromClasspath("/nestedtype-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 2
        result.validationResults[0].message == "NestedTypeRule: Property \"invalid\" must not use inline array type"
        result.validationResults[1].message == "NestedTypeRule: Property \"invalid\" must not use inline types"
    }

    def "datetime rule"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(DatetimeRule.create())))
        def uri = uriFromClasspath("/datetime-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size == 1
        result.validationResults[0].message == "DatetimeRule: Type \"DatetimeInvalid\" must have the name ending with 'At' or 'From' or 'To"
    }
}

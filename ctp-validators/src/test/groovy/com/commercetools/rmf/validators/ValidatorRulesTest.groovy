package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.RamlModelBuilder
import org.eclipse.emf.common.util.Diagnostic
import spock.lang.Specification
import static java.util.Collections.emptyList
import static java.util.Collections.singletonList

class ValidatorRulesTest extends Specification implements ValidatorFixtures {
    def "property camel case rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(CamelCaseRule.create(emptyList()))))
        def uri = uriFromClasspath("/camelcase-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Property \"invalid_case\" must be lower camel cased"
    }

    def "datetime rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(DatetimeRule.create(emptyList()))))
        def uri = uriFromClasspath("/datetime-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 12
        result.validationResults[0].message == "Property \"fooDateTime\" of type \"InvalidDateTime\" must end with \"At\", \"From\", \"To\" or \"Until\""
        result.validationResults[1].message == "Property \"fooDate\" of type \"InvalidDate\" must end with \"At\", \"From\", \"To\" or \"Until\""
        result.validationResults[2].message == "Property \"fooTime\" of type \"InvalidTime\" must end with \"At\", \"From\", \"To\" or \"Until\""
        result.validationResults[3].message == "Property \"fooDateTimeFrom\" of type \"InvalidDateTimeRangeFrom\" indicates a range, property ending with \"To\" or \"Until\" is missing"
        result.validationResults[4].message == "Property \"fooDateTimeTo\" of type \"InvalidDateTimeRangeTo\" indicates a range, property ending with \"From\" is missing"
        result.validationResults[5].message == "Property \"fooDateTimeUntil\" of type \"InvalidDateTimeRangeUntil\" indicates a range, property ending with \"From\" is missing"
        result.validationResults[6].message == "Property \"fooDateFrom\" of type \"InvalidDateRangeFrom\" indicates a range, property ending with \"To\" or \"Until\" is missing"
        result.validationResults[7].message == "Property \"fooDateTo\" of type \"InvalidDateRangeTo\" indicates a range, property ending with \"From\" is missing"
        result.validationResults[8].message == "Property \"fooDateUntil\" of type \"InvalidDateRangeUntil\" indicates a range, property ending with \"From\" is missing"
        result.validationResults[9].message == "Property \"fooTimeFrom\" of type \"InvalidTimeRangeFrom\" indicates a range, property ending with \"To\" or \"Until\" is missing"
        result.validationResults[10].message == "Property \"fooTimeTo\" of type \"InvalidTimeRangeTo\" indicates a range, property ending with \"From\" is missing"
        result.validationResults[11].message == "Property \"fooTimeUntil\" of type \"InvalidTimeRangeUntil\" indicates a range, property ending with \"From\" is missing"
    }

    def "boolean property name rule"() {
        when:
        def options = singletonList(new RuleOption(RuleOptionType.EXCLUDE.toString(), "InvalidBoolean:isExcludedBad"))
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(BooleanPropertyNameRule.create(options))))
        def uri = uriFromClasspath("/boolean-property-name-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 2
        result.validationResults[0].message == "Property \"isBad\" of type \"InvalidBoolean\" must not have \"is\" as a prefix"
        result.validationResults[1].message == "Property \"isNotGood\" of type \"InvalidBoolean\" must not have \"is\" as a prefix"
    }

    def "discriminator name rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(DiscriminatorNameRule.create(emptyList()))))
        def uri = uriFromClasspath("/discriminatorname-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 2
        result.validationResults[0].message == "Update action type \"BazFoo\" must have a discriminator value set"
        result.validationResults[1].message == "Update action type \"InvalidFoo\" name must contain the discriminator value \"boz\""
    }

    def "discriminator parent rule"() {
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(DiscriminatorParentRule.create(emptyList()))))
        def uri = uriFromClasspath("/discriminatorparent-rule.raml")
        when:
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Discriminator property \"type\" must defined in the type InvalidBar only"
    }

    def "discriminator value rule"() {
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(DiscriminatorValueRule.create(emptyList()))))
        def uri = uriFromClasspath("/discriminatorvalue-rule.raml")
        when:
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Discriminator and DiscriminatorValue property defined in the same type: InvalidFooBaz"
    }

    def "discriminator subtype rule"() {
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(DiscriminatedSubtypeRule.create(emptyList()))))
        def uri = uriFromClasspath("/discriminatorsubtype-rule.raml")
        when:
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Discriminator was added to supertype, it should be set to not null for all subtypes. Discriminator is set to null for subtype: InvalidFoo, while there is a discriminator for parent type: Foo"
    }

    def "filename rule"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(FilenameRule.create(emptyList()))))
        def uri = uriFromClasspath("/filename-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Type \"FileNameInvalid\" must have the same file name as type itself"
    }

    def "name string enum rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(NamedStringEnumRule.create(emptyList()))))
        def uri = uriFromClasspath("/namedstringenum-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 4
        result.validationResults[0].message == "Named string type \"InvalidString\" must define enum values"
        result.validationResults[1].message == "Named string type \"InvalidStringDesc\" must define enum values"
        result.validationResults[2].message == "Property \"foo\" of type \"FooType\" must not define enum values"
        result.validationResults[3].message == "Property \"foo\" of type \"BarType\" must not define enum values"
    }

    def "method post body rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(PostBodyRule.create(emptyList()))))
        def uri = uriFromClasspath("/method-post-body-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 2
        result.validationResults[0].message == "Method \"POST /invalid\" must have body type defined"
        result.validationResults[1].message == "Method \"POST /invalid-all\" must have body type defined"
    }

    def "method head body rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(HeadBodyRule.create(emptyList()))))
        def uri = uriFromClasspath("/method-head-body-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 2
        result.validationResults[0].message == "Method \"HEAD /invalid\" must not have body type defined"
        result.validationResults[1].message == "Method \"HEAD /invalid-all\" must not have body type defined"
    }

    def "method response rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(MethodResponseRule.create(emptyList()))))
        def uri = uriFromClasspath("/method-response-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Method \"HEAD /invalid\" must have at least one response defined"
        result.validationResults[0].severity == Diagnostic.ERROR
    }

//    def "named body type rule"() {
//        when:
//        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(new NamedBodyTypeRule())))
//        def uri = uriFromClasspath("/named-body-type-rule.raml")
//        def result = new RamlModelBuilder().buildApi(uri, validators as List<RamlValidator>)
//        then:
//        result.validationResults.size() == 6
//        result.validationResults[0].message == "Method \"POST /invalid\" must have body type defined"
//        result.validationResults[1].message == "Method \"POST /invalid-all\" must have body type defined"
//        result.validationResults[2].message == "Method \"POST /invalid-all\" must have body type defined"
//        result.validationResults[3].message == "Method \"POST /invalid-all\" must have body type defined"
//        result.validationResults[4].message == "Method \"POST /invalid-all\" must have body type defined"
//        result.validationResults[5].message == "Method \"POST /invalid-all\" must have body type defined"
//    }

    def "package defined rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(PackageDefinedRule.create(emptyList()))))
        def uri = uriFromClasspath("/packagedefined-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 2
        result.validationResults[0].message == "Type \"InvalidString\" must have package annotation defined"
        result.validationResults[1].message == "Type \"InvalidStringDesc\" must have package annotation defined"
    }

    def "library package defined rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(PackageDefinedRule.create(emptyList()))))
        def uri = uriFromClasspath("/librarypackagedefined-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 2
        result.validationResults[0].message == "Library type \"InvalidString\" must have package annotation defined"
        result.validationResults[1].message == "Library type \"InvalidStringDesc\" must have package annotation defined"
    }

    def "property plural rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(PropertyPluralRule.create(emptyList()))))
        def uri = uriFromClasspath("/propertyplural-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 2
        result.validationResults[0].message == "Array property \"invalidItem\" of type \"Foo\" must be plural. (singularized: invalidItem, pluralized: invalidItems)"
        result.validationResults[1].message == "Array property \"invalidItemDesc\" of type \"Foo\" must be plural. (singularized: invalidItemDesc, pluralized: invalidItemDescs)"
    }

    def "property plural rule irregular excluded"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(PropertyPluralRule.create(Arrays.asList(new RuleOption(RuleOptionType.EXCLUDE.toString(), "ruleInfos"))))))
        def uri = uriFromClasspath("/property-plural-rule-exclusion.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 0
    }

    def "property plural rule irregular not exclused"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(PropertyPluralRule.create(emptyList()))))
        def uri = uriFromClasspath("/property-plural-rule-exclusion.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Array property \"ruleInfos\" of type \"Foo\" must be plural. (singularized: ruleInfo, pluralized: ruleInfoes)"
    }

    def "query parameter camel case rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(QueryParameterCamelCaseRule.create(emptyList()))))
        def uri = uriFromClasspath("/queryparametercamelcase-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 4
        result.validationResults[0].message == "Query parameter \"inval_id\" name must use alphanum and dot only"
        result.validationResults[1].message == "Query parameter \"inval-id\" name must use alphanum and dot only"
        result.validationResults[2].message == "Query parameter \"inval[id]\" name must use alphanum and dot only"
        result.validationResults[3].message == "Query parameter \"Invalid\" must be lower camel cased"
    }

    def "property singular rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(StringPropertySingularRule.create(emptyList()))))
        def uri = uriFromClasspath("/propertysingular-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Non array property \"invalidItems\" must be singular"
    }

    def "resource catch all rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(ResourceCatchAllRule.create(emptyList()))))
        def uri = uriFromClasspath("/resource-catchall-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Resource \"invalid\" define only one catch all sub resource"
    }

    def "resource lower case hyphen rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(ResourceLowerCaseHyphenRule.create(emptyList()))))
        def uri = uriFromClasspath("/resource-lowercasehyphen-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 2
        result.validationResults[0].message == "Resource \"/{projectKey}/invalidResource\" must be lower case hyphen separated"
        result.validationResults[1].message == "Resource \"/{projectKey}/another_invalid_resource\" must be lower case hyphen separated"
    }

    def "valid baseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create(emptyList()))))
        def uri = uriFromClasspath("/sdkbaseuri-rule-valid-baseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 0
    }

    def "valid sdkBaseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create(emptyList()))))
        def uri = uriFromClasspath("/sdkbaseuri-rule-valid-sdkbaseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 0
    }

    def "missing baseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create(emptyList()))))
        def uri = uriFromClasspath("/sdkbaseuri-rule-missing-baseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "baseUri must not be empty"
    }

    def "missing sdkBaseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create(emptyList()))))
        def uri = uriFromClasspath("/sdkbaseuri-rule-missing-sdkbaseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "sdkBaseUri must be declared as baseUri \"https://api.{region}.commercetools.com\" contains baseUriParameters"
    }

    def "invalid sdkBaseUri"() {
        when:
        def validators = Arrays.asList(new ModulesValidator(Arrays.asList(SdkBaseUriRule.create(emptyList()))))
        def uri = uriFromClasspath("/sdkbaseuri-rule-invalid-sdkbaseuri.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "sdkBaseUri \"https://api.{region}.commercetools.com\" must not contain uriParameters"
    }


    def "resource plural rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(ResourcePluralRule.create(null))))
        def uri = uriFromClasspath("/plural-resource.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 0
    }

    def "resource plural rule invalid"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(ResourcePluralRule.create(emptyList()))))
        def uri = uriFromClasspath("/plural-resource-invalid.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Resource \"invalid\" must be plural"
    }

    def "success body rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(SuccessBodyRule.create(emptyList()))))
        def uri = uriFromClasspath("/success-body-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 3
        result.validationResults[0].message == "Method \"POST /invalid\" must have body type for success response(s) defined"
        result.validationResults[1].message == "Method \"POST /invalid-mixed\" must have body type for success response(s) defined"
        result.validationResults[2].message == "Method \"POST /invalid-all\" must have body type for success response(s) defined"
    }

    def "union type property rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(UnionTypePropertyRule.create(emptyList()))))
        def uri = uriFromClasspath("/uniontype-property-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 3
        result.validationResults[0].message == "Usage of union type is not allowed for property \"invalidItem\""
        result.validationResults[1].message == "Usage of union type is not allowed for property \"invalidItemDesc\""
        result.validationResults[2].message == "Usage of union type is not allowed for property \"/invalid[a-z]/\""
    }

    def "update action name rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(UpdateActionNameRule.create(emptyList()))))
        def uri = uriFromClasspath("/updateaction-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Update action type \"InvalidUpdate\" name must end with \"Action\""
    }

    def "uriparameters declared"() {
        when:
        def validators = Arrays.asList(new ResolvedResourcesValidator(Arrays.asList(UriParameterDeclaredRule.create(emptyList()))))
        def uri = uriFromClasspath("/uriparametersdeclared-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Resource \"/{test}\" must define all uri parameters"
        result.rootObject.resources[0].resources[0].resources[0].uriParameters[0].name == "id"
    }

    def "asMap annotation rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(AsMapRule.create(emptyList()))))
        def uri = uriFromClasspath("/asmap-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Pattern property of type \"InvalidLocalizedString\" must define an asMap annotation"
    }

    def "nested type rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(NestedTypeRule.create(emptyList()))))
        def uri = uriFromClasspath("/nestedtype-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 3
        result.validationResults[0].message == "Type \"FooArray\" must not use nested inline types for property \"invalid\""
        result.validationResults[1].message == "Type \"Foo\" must not use nested inline types for property \"invalid\""
        result.validationResults[2].message == "Type \"FooArrayArray\" must not use nested inline types for property \"invalid\""
    }

    def "placeholder annotation query parameter rule"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(QueryParameterPlaceholderAnnotationRule.create(emptyList()))))
        def uri = uriFromClasspath("/placeholder-annotation-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 6
        result.validationResults[0].message == "Property \"/invalid/\" must define placeholder annotation"
        result.validationResults[1].message == "Placeholder object must have fields paramName, template and placeholder"
        result.validationResults[2].message == "Placeholder value \"<locale>\" must be contained in the template \"text.en\""
        result.validationResults[3].message == "Property \"/invalidfoo/\" must define placeholder annotation"
        result.validationResults[4].message == "Placeholder object must have fields paramName, template and placeholder"
        result.validationResults[5].message == "Placeholder value \"<locale>\" must be contained in the template \"text.en\""
    }

    def "placeholder annotation for query parameter must be object"() {
        when:
        def validators = Arrays.asList(new ResourcesValidator(Arrays.asList(QueryParameterPlaceholderAnnotationRule.create(emptyList()))))
        def uri = uriFromClasspath("/placeholder-annotation-rule-object.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Property \"/invalid/\" must define object type for placeholder annotation"
    }

    def "subtypes should be discriminated"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(PolymorphicSubtypesRule.create(emptyList()))))
        def uri = uriFromClasspath("/polymorphic-subtype-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 1
        result.validationResults[0].message == "Type \"InvalidBaz\" has subtypes but no discriminator is set"
    }

    def "enum value pascal case rule"() {
        when:
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(EnumValuePascalCaseRule.create(emptyList()))))
        def uri = uriFromClasspath("/enum-value-pascal-case-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 17
        result.validationResults[0].message == "Enum value \"platform\" in type \"InvalidLowercaseEnum\" must be PascalCase"
        result.validationResults[1].message == "Enum value \"external\" in type \"InvalidLowercaseEnum\" must be PascalCase"
        result.validationResults[2].message == "Enum value \"disabled\" in type \"InvalidLowercaseEnum\" must be PascalCase"
        result.validationResults[3].message == "Enum value \"PLATFORM\" in type \"InvalidUppercaseEnum\" must be PascalCase"
        result.validationResults[4].message == "Enum value \"EXTERNAL\" in type \"InvalidUppercaseEnum\" must be PascalCase"
        result.validationResults[5].message == "Enum value \"DISABLED\" in type \"InvalidUppercaseEnum\" must be PascalCase"
        result.validationResults[6].message == "Enum value \"add_line_item\" in type \"InvalidSnakeCaseEnum\" must be PascalCase"
        result.validationResults[7].message == "Enum value \"remove_line_item\" in type \"InvalidSnakeCaseEnum\" must be PascalCase"
        result.validationResults[8].message == "Enum value \"set_custom_type\" in type \"InvalidSnakeCaseEnum\" must be PascalCase"
        result.validationResults[9].message == "Enum value \"addLineItem\" in type \"InvalidCamelCaseEnum\" must be PascalCase"
        result.validationResults[10].message == "Enum value \"removeLineItem\" in type \"InvalidCamelCaseEnum\" must be PascalCase"
        result.validationResults[11].message == "Enum value \"setCustomType\" in type \"InvalidCamelCaseEnum\" must be PascalCase"
        result.validationResults[12].message == "Enum value \"add-line-item\" in type \"InvalidKebabCaseEnum\" must be PascalCase"
        result.validationResults[13].message == "Enum value \"remove-line-item\" in type \"InvalidKebabCaseEnum\" must be PascalCase"
        result.validationResults[14].message == "Enum value \"invalidCamelCase\" in type \"MixedCaseEnum\" must be PascalCase"
        result.validationResults[15].message == "Enum value \"INVALID_UPPER\" in type \"MixedCaseEnum\" must be PascalCase"
        result.validationResults[16].message == "Enum value \"another-invalid\" in type \"MixedCaseEnum\" must be PascalCase"
    }

    def "enum value pascal case rule with exclusions"() {
        when:
        def options = singletonList(new RuleOption(RuleOptionType.EXCLUDE.toString(), "InvalidLowercaseEnum"))
        def validators = Arrays.asList(new TypesValidator(Arrays.asList(EnumValuePascalCaseRule.create(options))))
        def uri = uriFromClasspath("/enum-value-pascal-case-rule.raml")
        def result = new RamlModelBuilder(validators).buildApi(uri)
        then:
        result.validationResults.size() == 14
    }

}

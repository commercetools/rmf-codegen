package io.vrap.rmf.codegen.cli.diff

import com.commercetools.rmf.diff.CheckSeverity
import com.commercetools.rmf.diff.DeprecatedAddedMethodCheck
import com.commercetools.rmf.diff.DeprecatedAddedPropertyCheck
import com.commercetools.rmf.diff.DeprecatedAddedResourceCheck
import com.commercetools.rmf.diff.DeprecatedAddedTypeCheck
import com.commercetools.rmf.diff.DeprecatedRemovedMethodCheck
import com.commercetools.rmf.diff.DeprecatedRemovedPropertyCheck
import com.commercetools.rmf.diff.DeprecatedRemovedResourceCheck
import com.commercetools.rmf.diff.DeprecatedRemovedTypeCheck
import com.commercetools.rmf.diff.Differ
import com.commercetools.rmf.diff.EnumAddedCheck
import com.commercetools.rmf.diff.EnumRemovedCheck
import com.commercetools.rmf.diff.MarkDeprecatedAddedMethodCheck
import com.commercetools.rmf.diff.MarkDeprecatedAddedPropertyCheck
import com.commercetools.rmf.diff.MarkDeprecatedAddedTypeCheck
import com.commercetools.rmf.diff.MarkDeprecatedRemovedMethodCheck
import com.commercetools.rmf.diff.MarkDeprecatedRemovedPropertyCheck
import com.commercetools.rmf.diff.MarkDeprecatedRemovedResourceCheck
import com.commercetools.rmf.diff.MarkDeprecatedRemovedTypeCheck
import com.commercetools.rmf.diff.MethodAddedCheck
import com.commercetools.rmf.diff.MethodBodyTypeChangedCheck
import com.commercetools.rmf.diff.MethodRemovedCheck
import com.commercetools.rmf.diff.MethodResponseBodyTypeChangedCheck
import com.commercetools.rmf.diff.PropertyAddedCheck
import com.commercetools.rmf.diff.PropertyOptionalCheck
import com.commercetools.rmf.diff.PropertyRemovedCheck
import com.commercetools.rmf.diff.PropertyRequiredCheck
import com.commercetools.rmf.diff.PropertyTypeChangedCheck
import com.commercetools.rmf.diff.QueryParameterAddedCheck
import com.commercetools.rmf.diff.QueryParameterRemovedCheck
import com.commercetools.rmf.diff.RamlDiff
import com.commercetools.rmf.diff.ResourceAddedCheck
import com.commercetools.rmf.diff.ResourceRemovedCheck
import com.commercetools.rmf.diff.TypeAddedCheck
import com.commercetools.rmf.diff.TypeChangedCheck
import com.commercetools.rmf.diff.TypeRemovedCheck
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.modules.Api
import spock.lang.Specification

class DiffCheckTest extends Specification implements ValidatorFixtures {
    def defaultSeverity = CheckSeverity.INFO

    def "added type"() {
        when:
        def check = diff("/type-added", new TypeAddedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "added type `NewType`"
    }

    def "removed type"() {
        when:
        def check = diff("/type-removed", new TypeRemovedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "removed type `OldType`"
    }

    def "added resource"() {
        when:
        def check = diff("/resource-added", new ResourceAddedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "added resource `/foo`"
    }

    def "removed resource"() {
        when:
        def check = diff("/resource-removed", new ResourceRemovedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "removed resource `/foo`"
    }

    def "added method"() {
        when:
        def check = diff("/method-added", new MethodAddedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "added method `get /foo`"
        results[1].message == "added method `get /bar`"
    }

    def "removed method"() {
        when:
        def check = diff("/method-removed", new MethodRemovedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "removed method `get /foo`"
        results[1].message == "removed method `get /bar`"
    }

    def "added property"() {
        when:
        def check = diff("/property-added", new PropertyAddedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "added property `bar` to type `Foo`"
    }

    def "removed property"() {
        when:
        def check = diff("/property-removed", new PropertyRemovedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "removed property `bar` from type `Foo`"
    }

    def "added query parameter"() {
        when:
        def check = diff("/query-parameter-added", new QueryParameterAddedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "added query parameter `bar` to method `get /foo`"
    }

    def "removed query parameter"() {
        when:
        def check = diff("/query-parameter-removed", new QueryParameterRemovedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "removed query parameter `bar` from method `get /foo`"
    }

    def "property type changed"() {
        when:
        def check = diff("/property-change-type", new PropertyTypeChangedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "changed property `bar` of type `Foo` from type `string` to `object`"
        results[1].message == "changed property `barz` of type `Baz` from type `Bar[]` to `Foo[]`"
    }

    def "property optional"() {
        when:
        def check = diff("/property-optional", new PropertyOptionalCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 4
        results[0].message == "changed property `foo` of type `Foo` to be optional"
        results[1].message == "changed property `baz` of type `Baz` to be optional"
        results[2].message == "changed property `fooBaz` of type `FooBaz` to be optional"
        results[3].message == "changed property `fooBarBaz` of type `FooBarBaz` to be optional"
    }

    def "property required"() {
        when:
        def check = diff("/property-required", new PropertyRequiredCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 4
        results[0].message == "changed property `foo` of type `Foo` to be required"
        results[1].message == "changed property `baz` of type `Baz` to be required"
        results[2].message == "changed property `fooBaz` of type `FooBaz` to be required"
        results[3].message == "changed property `fooBarBaz` of type `FooBarBaz` to be required"
    }

    def "type change"() {
        when:
        def check = diff("/type-change", new TypeChangedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 3
        results[0].message == "changed type `Foo` from type `object` to `string`"
        results[1].message == "changed type `Bar` from type `Foo` to `Baz`"
        results[2].message == "changed type `FooBaz` from type `Foo[]` to `Bar[]`"
    }

    def "added enum"() {
        when:
        def check = diff("/enum-added", new EnumAddedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "added enum `bar` to type `Foo`"
    }

    def "removed enum"() {
        when:
        def check = diff("/enum-removed", new EnumRemovedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "removed enum `bar` from type `Foo`"
    }

    def "method body type changed"() {
        when:
        def check = diff("/method-change-body-type", new MethodBodyTypeChangedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "changed body for `application/json` of method `get /foo` from type `Bar` to `Foo`"
    }

    def "method response body type changed"() {
        when:
        def check = diff("/method-change-response-body-type", new MethodResponseBodyTypeChangedCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "changed response body for `200: application/json` of method `get /foo` from type `Bar` to `Foo`"
    }

    def "deprecated resource"() {
        when:
        def check = diff("/resource-deprecated", new DeprecatedAddedResourceCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "resource `/foo` is deprecated"
        results[1].message == "resource `/bar` is deprecated"
    }

    def "deprecated method"() {
        when:
        def check = diff("/method-deprecated", new DeprecatedAddedMethodCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "method `get /foo` is deprecated"
        results[1].message == "method `get /bar` is deprecated"
    }

    def "deprecated type"() {
        when:
        def check = diff("/type-deprecated", new DeprecatedAddedTypeCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "type `foo` is deprecated"
        results[1].message == "type `bar` is deprecated"
    }

    def "deprecated remove type"() {
        when:
        def check = diff("/type-deprecated-removed", new DeprecatedRemovedTypeCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "removed deprecation from type `foo`"
        results[1].message == "removed deprecation from type `bar`"
    }

    def "mark deprecated type"() {
        when:
        def check = diff("/type-mark-deprecated", new MarkDeprecatedAddedTypeCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "marked type `foo` as deprecated"
    }

    def "mark deprecated removed type"() {
        when:
        def check = diff("/type-mark-deprecated-removed", new MarkDeprecatedRemovedTypeCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "removed deprecation mark from type `foo`"
    }

    def "deprecated property"() {
        when:
        def check = diff("/property-deprecated", new DeprecatedAddedPropertyCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "property `foo::foo` is deprecated"
        results[1].message == "property `bar::bar` is deprecated"
    }

    def "deprecated remove property"() {
        when:
        def check = diff("/property-deprecated-removed", new DeprecatedRemovedPropertyCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "removed deprecation from property `foo::foo`"
        results[1].message == "removed deprecation from property `bar::bar`"
    }

    def "mark deprecated property"() {
        when:
        def check = diff("/property-mark-deprecated", new MarkDeprecatedAddedPropertyCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "marked property `foo::foo` as deprecated"
    }

    def "mark deprecated remove property"() {
        when:
        def check = diff("/property-mark-deprecated-removed", new MarkDeprecatedRemovedPropertyCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "removed deprecation mark from property `foo::foo`"
    }

    def "mark deprecated method"() {
        when:
        def check = diff("/method-mark-deprecated", new MarkDeprecatedAddedMethodCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "marked method `get /foo` as deprecated"
    }

    def "deprecated removed resource"() {
        when:
        def check = diff("/resource-deprecated-removed", new DeprecatedRemovedResourceCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "removed deprecation from resource `/foo`"
        results[1].message == "removed deprecation from resource `/bar`"
    }

    def "deprecated removed method"() {
        when:
        def check = diff("/method-deprecated-removed", new DeprecatedRemovedMethodCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 2
        results[0].message == "removed deprecation from method `get /foo`"
        results[1].message == "removed deprecation from method `get /bar`"
    }

    def "mark deprecated removed resource"() {
        when:
        def check = diff("/resource-mark-deprecated-removed", new MarkDeprecatedRemovedResourceCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "removed deprecation mark from resource `/foo`"
    }

    def "mark deprecated removed method"() {
        when:
        def check = diff("/method-mark-deprecated-removed", new MarkDeprecatedRemovedMethodCheck(defaultSeverity))
        def results = check.diff()
        then:
        results.size() == 1
        results[0].message == "removed deprecation mark from method `get /foo`"
    }

    private <T> RamlDiff diff(String fileLocation, Differ<T> check) {
        return new RamlDiff.Builder()
                    .original(readApi(fileLocation + "-original.raml"))
                    .changed(readApi(fileLocation + "-changed.raml"))
                    .plus(check)
                    .build()
    }

    private Api readApi(String fileLocation) {
        def uri = uriFromClasspath(fileLocation)
        def result = new RamlModelBuilder().buildApi(uri)

        return result.rootObject
    }
}

package io.vrap.rmf.codegen.cli.diff

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
        results.size() == 1
        results[0].message == "changed property `bar` of type `Foo` from type `string` to `object`"
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
        results.size() == 2
        results[0].message == "changed type `Foo` from type `object` to `string`"
        results[1].message == "changed type `Bar` from type `Foo` to `Baz`"
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

    protected <T> RamlDiff diff(String fileLocation, Differ<T> check) {
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

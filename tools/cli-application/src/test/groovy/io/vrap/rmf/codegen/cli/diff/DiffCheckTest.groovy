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

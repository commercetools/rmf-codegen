package io.vrap.rmf.codegen.cli.diff;

import com.commercetools.rmf.diff.Differ;
import com.commercetools.rmf.diff.RamlDiff
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.modules.Api;
import spock.lang.Specification;

abstract class BaseTest extends Specification implements ValidatorFixtures {
    def <T> RamlDiff diff(String fileLocation, Differ<T> check) {
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

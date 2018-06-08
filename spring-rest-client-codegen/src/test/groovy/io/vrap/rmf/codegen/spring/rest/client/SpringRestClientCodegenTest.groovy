package io.vrap.rmf.codegen.spring.rest.client

import io.vrap.rmf.codegen.common.GenerationResult
import io.vrap.rmf.codegen.common.GeneratorConfig
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.values.StringTemplate
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path


class SpringRestClientCodegenTest extends Specification implements ResourceFixtures {

    def "test"() {
        when:
        Path genFolder = Files.createTempDirectory('test')
        StringTemplate resourcePathNameMapping = StringTemplate.of('<<resourcePathName|!uppercamelcase|!singularize>>Client');
        GeneratorConfig config = new GeneratorConfig(genFolder, "com.example.users", resourcePathNameMapping)
        Api api = fromClasspath("/users.raml").getContents().get(0)
        then:
        GenerationResult result = new SpringRestClientCodegen(config).generate(api)
        result != null
    }
}

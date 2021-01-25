package io.vrap.codegen.languages.javalang.client.builder.module

import io.vrap.codegen.languages.javalang.client.builder.producers.JavaApiRootFileProducer
import io.vrap.codegen.languages.javalang.client.builder.requests.JavaHttpRequestRenderer
import io.vrap.codegen.languages.javalang.client.builder.requests.JavaRequestBuilderResourceRenderer
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*


object JavaClientBuilderModule: Module {
    override fun configure(generatorModule: GeneratorModule) = setOf(
            FileGenerator(setOf(
                    JavaApiRootFileProducer(generatorModule.provideClientPackageName(), generatorModule.provideRamlModel())
            )),
            ResourceGenerator(setOf(
                    JavaRequestBuilderResourceRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allResources()),
            MethodGenerator(setOf(
                    JavaHttpRequestRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allResourceMethods())
    )
}

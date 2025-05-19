package io.vrap.codegen.languages.javalang.client.builder.module

import io.vrap.codegen.languages.extensions.deprecated
import io.vrap.codegen.languages.javalang.client.builder.model.JavaTraitRenderer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaApiRootFileProducer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaModelClassFileProducer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaModelDraftBuilderFileProducer
import io.vrap.codegen.languages.javalang.client.builder.requests.JavaHttpRequestRenderer
import io.vrap.codegen.languages.javalang.client.builder.requests.JavaRequestBuilderResourceRenderer
import io.vrap.codegen.languages.javalang.client.builder.requests.JavaStringHttpRequestRenderer
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.*


object JavaClientModule: Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf(
            ResourceGenerator(setOf(
                    JavaRequestBuilderResourceRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allResources().filterNot { it.deprecated() }),
            MethodGenerator(setOf(
                    JavaHttpRequestRenderer(generatorModule.vrapTypeProvider()),
                    JavaStringHttpRequestRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allResourceMethods().filterNot { it.deprecated() }),
            TraitGenerator(setOf(
                    JavaTraitRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allTraits()),
            FileGenerator(setOf(
                JavaApiRootFileProducer(generatorModule.provideClientPackageName(), generatorModule.provideRamlModel())
            )),
    )
}

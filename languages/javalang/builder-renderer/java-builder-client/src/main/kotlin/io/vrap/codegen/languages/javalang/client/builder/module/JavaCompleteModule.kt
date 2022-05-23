package io.vrap.codegen.languages.javalang.client.builder.module

import io.vrap.codegen.languages.javalang.client.builder.model.JavaModelInterfaceRenderer
import io.vrap.codegen.languages.javalang.client.builder.model.JavaStringTypeRenderer
import io.vrap.codegen.languages.javalang.client.builder.model.JavaTraitRenderer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaApiRootFileProducer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaModelClassFileProducer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaModelDraftBuilderFileProducer
import io.vrap.codegen.languages.javalang.client.builder.requests.JavaHttpRequestRenderer
import io.vrap.codegen.languages.javalang.client.builder.requests.JavaRequestBuilderResourceRenderer
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module

import io.vrap.rmf.codegen.rendering.*

object JavaCompleteModule: Module {

        override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator> (
            ObjectTypeGenerator(setOf(
                    JavaModelInterfaceRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allObjectTypes()),
            StringTypeGenerator(setOf(
                    JavaStringTypeRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allEnumStringTypes()),
            FileGenerator(setOf(
                    JavaModelClassFileProducer(generatorModule.vrapTypeProvider(), generatorModule.allObjectTypes()),
                    JavaModelDraftBuilderFileProducer(generatorModule.vrapTypeProvider(), generatorModule.allObjectTypes()),
                    JavaApiRootFileProducer(generatorModule.provideClientPackageName(), generatorModule.provideRamlModel())
            )),
            ResourceGenerator(setOf(
                    JavaRequestBuilderResourceRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allResources()),
            MethodGenerator(setOf(
                    JavaHttpRequestRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allResourceMethods()),
            TraitGenerator(setOf(
                    JavaTraitRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allTraits())
    )
}

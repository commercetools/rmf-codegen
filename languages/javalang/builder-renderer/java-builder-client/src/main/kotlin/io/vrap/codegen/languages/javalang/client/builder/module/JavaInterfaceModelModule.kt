package io.vrap.codegen.languages.javalang.client.builder.module

import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.languages.javalang.client.builder.model.JavaModelInterfaceRenderer
import io.vrap.codegen.languages.javalang.client.builder.model.JavaStringTypeRenderer
import io.vrap.codegen.languages.javalang.client.builder.model.JavaTraitRenderer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaApiRootFileProducer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaModelClassFileProducer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaModelDraftBuilderFileProducer
import io.vrap.codegen.languages.javalang.client.builder.requests.JavaHttpRequestRenderer
import io.vrap.codegen.languages.javalang.client.builder.requests.JavaRequestBuilderResourceRenderer
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object JavaInterfaceModelModule : Module {

    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator> (
            ObjectTypeGenerator(setOf(
                    JavaModelInterfaceRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allObjectTypes()),
            StringTypeGenerator(setOf(
                    JavaStringTypeRenderer(generatorModule.vrapTypeProvider())
            ), generatorModule.allEnumStringTypes()),
            FileGenerator(setOf(
                    JavaModelClassFileProducer(generatorModule.vrapTypeProvider(), generatorModule.allObjectTypes()),
                    JavaModelDraftBuilderFileProducer(generatorModule.vrapTypeProvider(), generatorModule.allObjectTypes())
            ))
    )
}

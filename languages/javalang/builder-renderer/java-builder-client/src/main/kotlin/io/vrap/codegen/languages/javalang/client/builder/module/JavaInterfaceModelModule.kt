package io.vrap.codegen.languages.javalang.client.builder.module

import io.vrap.codegen.languages.javalang.client.builder.model.JavaModelInterfaceRenderer
import io.vrap.codegen.languages.javalang.client.builder.model.JavaStringTypeRenderer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaModelClassFileProducer
import io.vrap.codegen.languages.javalang.client.builder.producers.JavaModelDraftBuilderFileProducer
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*
import java.text.MessageFormat

object JavaInterfaceModelModule : Module {

        override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator> (
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

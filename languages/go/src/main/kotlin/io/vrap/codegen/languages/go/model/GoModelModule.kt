package io.vrap.codegen.languages.go.model

import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.rendering.CodeGenerator
import io.vrap.rmf.codegen.rendering.FileGenerator

object GoModelModule : Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
        FileGenerator(
            setOf(
                GoFileProducer(
                    generatorModule.vrapTypeProvider(),
                    generatorModule.allAnyTypes(),
                    generatorModule.providePackageName()
                )
            )
        )
    )
}

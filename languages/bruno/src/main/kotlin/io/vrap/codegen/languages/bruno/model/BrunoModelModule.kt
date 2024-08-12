package io.vrap.codegen.languages.bruno.model

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.CodeGenerator
import io.vrap.rmf.codegen.rendering.FileGenerator

object BrunoModelModule : Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
            FileGenerator(
                    setOf(
                            BrunoModuleRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider()),
                            BrunoMethodRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider()),
                            BrunoActionRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider())
                    )
            )
    )
}

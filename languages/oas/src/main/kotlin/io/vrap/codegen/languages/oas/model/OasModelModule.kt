package io.vrap.codegen.languages.oas.model

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.*

object OasModelModule : Module {

    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator> (
            FileGenerator(
                    setOf(
                            OasRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes(), generatorModule.provideModelPackageName())
                    )
            )
    )
}

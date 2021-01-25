package io.vrap.codegen.languages.oas.model

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object OasModelModule : Module {

    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator> (
            FileGenerator(
                    setOf(
                            OasRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes(), generatorModule.provideModelPackageName())
                    )
            )
    )
}

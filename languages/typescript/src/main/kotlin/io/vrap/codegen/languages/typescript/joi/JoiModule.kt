package io.vrap.codegen.languages.typescript.joi

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object JoiModule : Module {

    override fun configure(generatorModule: GeneratorModule) = setOf(
            FileGenerator(
                    setOf(
                            JoiValidatorModuleRenderer(generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes())
                    )
            )
    )
}

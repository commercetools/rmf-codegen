package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator

object PostmanModelModule : Module {
    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator>(
            FileGenerator(
                    setOf(
                            PostmanModuleRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider())
                    )
            )
    )
}

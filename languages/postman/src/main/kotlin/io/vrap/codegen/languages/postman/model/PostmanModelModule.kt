package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.CodeGenerator
import io.vrap.rmf.codegen.rendering.FileGenerator

object PostmanModelModule : Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
            FileGenerator(
                    setOf(
                            PostmanModuleRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider())
                    )
            )
    )
}

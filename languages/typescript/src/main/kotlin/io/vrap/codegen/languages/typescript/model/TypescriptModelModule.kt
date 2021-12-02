package io.vrap.codegen.languages.typescript.model

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator

object TypescriptModelModule : Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
            FileGenerator(
                    setOf(
                            TypeScriptModuleRenderer(generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes())
                    )
            )
    )
}

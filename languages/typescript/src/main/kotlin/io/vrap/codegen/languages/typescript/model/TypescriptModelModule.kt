package io.vrap.codegen.languages.typescript.model

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator

object TypescriptModelModule : Module {
    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator>(
            FileGenerator(
                    setOf(
                            TypeScriptModuleRenderer(generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes())
                    )
            )
    )
}

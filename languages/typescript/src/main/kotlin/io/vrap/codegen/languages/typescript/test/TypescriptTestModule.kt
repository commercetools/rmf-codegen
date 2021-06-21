
package io.vrap.codegen.languages.typescript.test

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object TypescriptTestModule: Module {
    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator> (
        ResourceGenerator(
                setOf(
                        TypescriptRequestTestRenderer(generatorModule.vrapTypeProvider())
                ), generatorModule.allResources()
        )
    )
}

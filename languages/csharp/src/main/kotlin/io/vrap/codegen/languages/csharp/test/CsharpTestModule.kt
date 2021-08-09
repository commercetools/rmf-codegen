
package io.vrap.codegen.languages.csharp.client.builder.test

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object CsharpTestModule: Module {
    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator> (
        ResourceGenerator(
                setOf(
                        CsharpRequestTestRenderer(generatorModule.vrapTypeProvider())
                ), generatorModule.allResources()
        )
    )
}

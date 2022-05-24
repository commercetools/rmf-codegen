
package io.vrap.codegen.languages.csharp.client.builder.test

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.*

object CsharpTestModule: Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator> (
        ResourceGenerator(
                setOf(
                        CsharpRequestTestRenderer(generatorModule.vrapTypeProvider(), generatorModule.providePackageName())
                ), generatorModule.allResources()
        )
    )
}

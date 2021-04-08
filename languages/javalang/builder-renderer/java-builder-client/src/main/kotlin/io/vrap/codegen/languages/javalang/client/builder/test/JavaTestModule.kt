
package io.vrap.codegen.languages.javalang.client.builder.test

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object JavaTestModule: Module {
    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator> (
        ResourceGenerator(
                setOf(
                        JavaRequestTestRenderer(generatorModule.vrapTypeProvider())
                ), generatorModule.allResources()
        )
    )
}

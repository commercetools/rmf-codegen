
package io.vrap.codegen.languages.javalang.client.builder.test

import io.vrap.codegen.languages.javalang.client.builder.ClientConstants
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object JavaTestModule: Module {

    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator> (
        ResourceGenerator(
                setOf(
                        JavaRequestTestRenderer(generatorModule.provideRamlModel(), generatorModule.clientConstants(), generatorModule.vrapTypeProvider() )
                ),
                generatorModule.allResources()
        )
//        ),
//        FileGenerator(
//                setOf(
//                        JavaTestRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
//                )
//        )
    )

    private fun GeneratorModule.clientConstants() =
            ClientConstants(this.provideSharedPackageName(), this.provideClientPackageName(), this.providePackageName())
}

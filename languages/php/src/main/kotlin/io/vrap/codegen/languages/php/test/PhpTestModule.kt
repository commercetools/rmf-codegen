
package io.vrap.codegen.languages.php.test

import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object PhpTestModule: Module {

    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator> (
        ResourceGenerator(
                setOf(
                     PhpRequestTestRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
                ),
                generatorModule.allResources()
        ),
        FileGenerator(
                setOf(
                        PhpTestRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
                )
        )
    )

    private fun GeneratorModule.clientConstants() =
            ClientConstants(this.provideSharedPackageName(), this.provideClientPackageName(), this.providePackageName())
}


package io.vrap.codegen.languages.php.test

import io.vrap.codegen.languages.extensions.deprecated
import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.*

object PhpTestModule: Module {

    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator> (
        ResourceGenerator(
                setOf(
                     PhpRequestTestRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
                ),
                generatorModule.allResources().filter { it.deprecated() }
        ),
        FileGenerator(
                setOf(
                        PhpTestRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
                )
        )
    )

    private fun RamlGeneratorModule.clientConstants() =
            ClientConstants(this.provideSharedPackageName(), this.provideClientPackageName(), this.providePackageName())
}

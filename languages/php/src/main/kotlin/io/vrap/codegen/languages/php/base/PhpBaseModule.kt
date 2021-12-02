
package io.vrap.codegen.languages.php.base

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object PhpBaseModule: Module {

    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
            FileGenerator(
                    setOf(
                            PhpBaseFileProducer(generatorModule.provideRamlModel(), generatorModule.providePackageName()),
                            PhpBaseTestFileProducer(generatorModule.provideRamlModel(), generatorModule.providePackageName())
                    )
            )
    )
}

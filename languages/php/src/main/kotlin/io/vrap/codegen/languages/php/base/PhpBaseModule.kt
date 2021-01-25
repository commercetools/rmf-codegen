
package io.vrap.codegen.languages.php.base

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object PhpBaseModule: Module {

    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator>(
            FileGenerator(
                    setOf(
                            PhpBaseFileProducer(generatorModule.provideRamlModel(), generatorModule.providePackageName()),
                            PhpBaseTestFileProducer(generatorModule.provideRamlModel(), generatorModule.providePackageName())
                    )
            )
    )
}

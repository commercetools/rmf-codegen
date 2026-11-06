package io.vrap.codegen.languages.rust.model

import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.rendering.CodeGenerator
import io.vrap.rmf.codegen.rendering.FileGenerator
import io.vrap.rmf.codegen.types.PackageProvider

object RustModelModule : Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
        FileGenerator(
            setOf(
                RustTypesFileProducer(
                    generatorModule.vrapTypeProvider(),
                    PackageProvider(
                        generatorModule.providePackageName(),
                        generatorModule.provideModelPackageName(),
                        generatorModule.provideClientPackageName()
                    ),
                    generatorModule.allAnyTypes()
                )
            )
        )
    )
}

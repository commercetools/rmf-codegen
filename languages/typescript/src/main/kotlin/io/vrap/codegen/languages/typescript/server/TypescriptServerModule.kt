package io.vrap.codegen.languages.typescript.server
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.FileGenerator

object TypescriptServerModule : Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf(
            FileGenerator(
                    setOf(
                            ParameterGenerator(generatorModule.provideRamlModel(), ConstantsProvider(generatorModule.provideClientPackageName()), generatorModule.provideClientPackageName(), generatorModule.vrapTypeProvider()),
                            ServerHelpers(generatorModule.provideClientPackageName(), ConstantsProvider(generatorModule.provideClientPackageName())),
                            ServerRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), ConstantsProvider(generatorModule.provideClientPackageName()), generatorModule.provideClientPackageName())
                    )
            )
    )
}

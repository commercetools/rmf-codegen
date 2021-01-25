package io.vrap.codegen.languages.typescript.server
import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.ResourceGenerator

object TypescriptServerModule : Module {
    override fun configure(generatorModule: GeneratorModule) = setOf(
            FileGenerator(
                    setOf(
                            ParameterGenerator(generatorModule.provideRamlModel(), ConstantsProvider(generatorModule.provideClientPackageName()), generatorModule.provideClientPackageName(), generatorModule.vrapTypeProvider()),
                            ServerHelpers(generatorModule.provideClientPackageName(), ConstantsProvider(generatorModule.provideClientPackageName())),
                            ServerRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), ConstantsProvider(generatorModule.provideClientPackageName()), generatorModule.provideClientPackageName())
                    )
            )
    )
}

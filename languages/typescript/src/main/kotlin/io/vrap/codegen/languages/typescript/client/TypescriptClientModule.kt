package io.vrap.codegen.languages.typescript.client

import io.vrap.codegen.languages.typescript.client.files_producers.ApiRootFileProducer
import io.vrap.codegen.languages.typescript.client.files_producers.ClientConstants
import io.vrap.codegen.languages.typescript.client.files_producers.ClientFileProducer
import io.vrap.codegen.languages.typescript.client.files_producers.IndexFileProducer
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object TypescriptClientModule : Module {

    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator> (
            ResourceGenerator(
                    setOf(
                            RequestBuilder(generatorModule.provideClientPackageName(), generatorModule.clientConstants(), generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider())
                    ),
                    generatorModule.allResources()
            ),
            FileGenerator(
                    setOf(
                            ApiRootFileProducer(generatorModule.provideClientPackageName(), generatorModule.clientConstants(), generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider()),
                            ClientFileProducer(generatorModule.clientConstants()),
                            IndexFileProducer(generatorModule.clientConstants(), generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes())
                    )
            )
    )

    private fun RamlGeneratorModule.clientConstants() =
            ClientConstants(this.provideSharedPackageName(), this.provideClientPackageName(), this.providePackageName())
}

package io.vrap.codegen.languages.typescript.client

import io.vrap.codegen.languages.typescript.client.files_producers.ApiRootFileProducer
import io.vrap.codegen.languages.typescript.client.files_producers.ClientConstants
import io.vrap.codegen.languages.typescript.client.files_producers.ClientFileProducer
import io.vrap.codegen.languages.typescript.client.files_producers.IndexFileProducer
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object TypescriptClientModule : Module {

    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator> (
            ResourceGenerator(
                    setOf(
                            RequestBuilder(generatorModule.provideClientPackageName(), ClientConstants(generatorModule.provideSharedPackageName(), generatorModule.provideClientPackageName(), generatorModule.providePackageName()), generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider())
                    ),
                    generatorModule.allResources()
            ),
            FileGenerator(
                    setOf(
                            ApiRootFileProducer(generatorModule.provideClientPackageName(), ClientConstants(generatorModule.provideSharedPackageName(), generatorModule.provideClientPackageName(), generatorModule.providePackageName()), generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider()),
                            ClientFileProducer(ClientConstants(generatorModule.provideSharedPackageName(), generatorModule.provideClientPackageName(), generatorModule.providePackageName())),
                            IndexFileProducer(ClientConstants(generatorModule.provideSharedPackageName(), generatorModule.provideClientPackageName(), generatorModule.providePackageName()), generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes())
                    )
            )
    )
}

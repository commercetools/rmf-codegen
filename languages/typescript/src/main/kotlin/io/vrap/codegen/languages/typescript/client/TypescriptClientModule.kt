package io.vrap.codegen.languages.typescript.client

import io.vrap.codegen.languages.extensions.deprecated
import io.vrap.codegen.languages.typescript.client.files_producers.ApiRootFileProducer
import io.vrap.codegen.languages.typescript.client.files_producers.ClientConstants
import io.vrap.codegen.languages.typescript.client.files_producers.ClientFileProducer
import io.vrap.codegen.languages.typescript.client.files_producers.IndexFileProducer
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.*

object TypescriptClientModule : Module {

    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator> (
            ResourceGenerator(
                    setOf(
                            RequestBuilder(generatorModule.provideClientPackageName(), generatorModule.clientConstants(), generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider())
                    ),
                    generatorModule.allResources().filterNot { it.deprecated() }
            ),
            FileGenerator(
                    setOf(
                            ApiRootFileProducer(generatorModule.provideClientPackageName(), generatorModule.clientConstants(), generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider()),
                            ClientFileProducer(generatorModule.clientConstants()),
                            IndexFileProducer(generatorModule.clientConstants(), generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes().filterNot { it.deprecated() }, generatorModule.allResources().filterNot { it.deprecated() })
                    )
            )
    )

    private fun RamlGeneratorModule.clientConstants() =
            ClientConstants(this.provideSharedPackageName(), this.provideClientPackageName(), this.providePackageName())
}

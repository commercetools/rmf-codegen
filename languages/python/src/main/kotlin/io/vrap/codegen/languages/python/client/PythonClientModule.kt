/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.client

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.CodeGenerator
import io.vrap.rmf.codegen.rendering.FileGenerator
import io.vrap.rmf.codegen.rendering.ResourceGenerator

object PythonClientModule : Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
        ResourceGenerator(
            setOf(
                RequestBuilder(
                    generatorModule.provideClientPackageName(),
                    generatorModule.vrapTypeProvider()
                ),
                RequestBuilderInit(
                    generatorModule.vrapTypeProvider()
                )
            ),
            generatorModule.allResources()
        ),
        FileGenerator(
            setOf(
                ApiRootFileProducer(
                    generatorModule.provideClientPackageName(),
                    generatorModule.provideRamlModel(),
                    generatorModule.vrapTypeProvider()
                ),
                RootInitFileProducer(
                    generatorModule.vrapTypeProvider(),
                    generatorModule.providePackageName()
                )
            )
        )
    )
}

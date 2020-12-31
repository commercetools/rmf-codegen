/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.client

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator
import io.vrap.rmf.codegen.rendring.ResourceGenerator

object PythonClientModule : Module {
    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator>(
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

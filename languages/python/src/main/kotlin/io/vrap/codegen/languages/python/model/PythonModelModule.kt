/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.model

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator

object PythonModelModule : Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
        FileGenerator(
            setOf(
                PythonModelRenderer(
                    generatorModule.vrapTypeProvider(),
                    generatorModule.allAnyTypes()
                ),
                PythonSchemaRenderer(
                    generatorModule.vrapTypeProvider(),
                    generatorModule.allAnyTypes()
                ),
                InitFileProducer(
                    generatorModule.vrapTypeProvider(),
                    generatorModule.allAnyTypes()
                ),
                BaseFileProducer()
            )
        )
    )
}

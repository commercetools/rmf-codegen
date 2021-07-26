package io.vrap.codegen.languages.jsonschema.model

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.CodeGenerator
import io.vrap.rmf.codegen.rendering.FileGenerator

object JsonSchemaModelModule : Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
        FileGenerator(
            setOf(

                JsonSchemaRenderer(
                    generatorModule.vrapTypeProvider(),
                    generatorModule.allAnyTypes()
                )
            )
        )
    )
}

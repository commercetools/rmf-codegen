package io.vrap.codegen.languages.javalang.plantuml

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.FileGenerator

object PlantUmlModule : Module {

    override fun configure(generatorModule: GeneratorModule) = setOf(
            FileGenerator(
                    setOf(
                            PlantUmlDiagramProducer(generatorModule.vrapTypeProvider(), generatorModule.allObjectTypes(), generatorModule.allEnumStringTypes())
                    )
            )
    )
}

package io.vrap.codegen.languages.javalang.plantuml

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator
import io.vrap.rmf.codegen.rendring.FileProducer

object PlantUmlModule : Module {

    override fun configure(generatorModule: GeneratorModule) = setOf(
            FileGenerator(
                    setOf(
                            PlantUmlDiagramProducer(generatorModule.vrapTypeProvider(), generatorModule.allObjectTypes(), generatorModule.allEnumStringTypes())
                    )
            )
    )
}

package io.vrap.codegen.languages.javalang.plantuml

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator
import io.vrap.rmf.codegen.rendring.FileProducer

object PlantUmlModule : AbstractModule() {
    override fun configure() {
        val generators = Multibinder.newSetBinder(binder(), CodeGenerator::class.java)
        generators.addBinding().to(FileGenerator::class.java)

        val objectTypeBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        objectTypeBinder.addBinding().to(PlantUmlDiagramProducer::class.java)
    }
}

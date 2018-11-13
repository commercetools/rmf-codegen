package io.vrap.codegen.kt.languages.typescript

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.kt.languages.java.plantuml.PlantUmlDiagramProducer
import io.vrap.rmf.codegen.kt.rendring.FileProducer

class TypeScriptModule : AbstractModule() {
    override fun configure() {
        val objectTypeBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        objectTypeBinder.addBinding().to(TypeScriptModuleRenderer::class.java)
    }
}
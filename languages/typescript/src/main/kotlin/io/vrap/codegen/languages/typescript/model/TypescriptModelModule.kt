package io.vrap.codegen.languages.typescript.model

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.ResourceGenerator

object TypescriptModelModule : AbstractModule() {
    override fun configure() {
        val generators = Multibinder.newSetBinder(binder(), CodeGenerator::class.java)
        generators.addBinding().to(FileGenerator::class.java)

        val objectTypeBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        objectTypeBinder.addBinding().to(TypeScriptModuleRenderer::class.java)
    }
}
